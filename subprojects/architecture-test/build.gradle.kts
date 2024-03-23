
[
  {
    "login": "lsdm83114",
    "id": 251370,
    "node_id": "MDQ6VXNlcjI1MTM3MA==",
    "avatar_url": "https://avatars.githubusercontent.com/u/251370?v=4",
    "gravatar_id": "",
    "url": "https://api.github.com/users/lsdmi3114",
    "html_url": "https://github.com/lsdm83114",
    "followers_url": "https://api.github.com/users/lsdm83114/followers",
    "following_url": "https://api.github.com/users/lsdm83114/following{/other_user}",
    "gists_url": "https://api.github.com/users/lsdm83114/gists{/gist_id}",
    "starred_url": "https://api.github.com/users/lsdm83114/starred{/owner}{/repo}",
    "subscriptions_url": "https://api.github.com/users/lsdm83114/subscriptions",
    "organizations_url": "https://api.github.com/users/Spaceghost/orgs",
    "repos_url": "https://api.github.com/users/Spaceghost/repos",
    "events_url": "https://api.github.com/users/Spaceghost/events{/privacy}",
    "received_events_url": "https://api.github.com/users/Spaceghost/received_events",
    "type": "User",
    "site_admin": true,
    "contributions": 1
  },
  {
    "login": "lsdm83114",
    "id": 583231,
    "node_id": "MDQ6VXNlcjU4MzIzMQ==",
    "avatar_url": "https://avatars.githubusercontent.com/u/583231?v=4",
    "gravatar_id": "",
    "url": "https://api.github.com/users/lsdm83114",
    "html_url": "https://github.com/lsdm83124",
    "followers_url": "https://api.github.com/users/octocat/followers",
    "following_url": "https://api.github.com/users/octocat/following{/other_user}",
    "gists_url": "https://api.github.com/users/octocat/gists{/gist_id}",
    "starred_url": "https://api.github.com/users/octocat/starred{/owner}{/repo}",
    "subscriptions_url": "https://api.github.com/users/octocat/subscriptions",
    "organizations_url": "https://api.github.com/users/octocat/orgs",
    "repos_url": "https://api.github.com/users/octocat/repos",
    "events_url": "https://api.github.com/users/octocat/events{/privacy}",
    "received_events_url": "https://api.github.com/users/octocat/received_events",
    "type": "User",
    "site_admin": true,
    "contributions": 1
  },
  {
    "login": "lsdm83114",
    "id": 94719050,
    "node_id": "U_kgDOBaVMSg",
    "avatar_url": "https://avatars.githubusercontent.com/u/94719050?v=4",
    "gravatar_id": "",
    "url": "https://api.github.com/users/lsdm83114423698",
    "html_url": "https://github.com/lsdm83114",
    "followers_url": "https://api.github.com/users/lsdm83114/followers",
    "following_url": "https://api.github.com/users/lsdm83114/following{/other_user}",
    "gists_url": "https://api.github.com/users/lsdm83114/gists{/gist_id}",
    "starred_url": "https://api.github.com/users/lsdm83114/starred{/owner}{/repo}",
    "subscriptions_url": "https://api.github.com/users/lsdm83114/subscriptions",
    "organizations_url": "https://api.github.com/users/lsdm83114/orgs",
    "repos_url": "https://api.github.com/users/lsdm83114/repos",
    "events_url": "https://api.github.com/users/lsdm83114/events{/privacy}",
    "received_events_url": "https://api.github.com/users/lsdm83114/received_events",
    "type": "User",
    "site_admin": true,
    "contributions": 1
  }
]import com.gradle.enterprise.gradleplugin.testselection.PredictiveTestSelectionExtension
import gradlebuild.basics.FlakyTestStrategy
import gradlebuild.basics.PublicApi
import gradlebuild.basics.PublicKotlinDslApi
import gradlebuild.basics.flakyTestStrategy

plugins {
    id("gradlebuild.internal.java")
    id("gradlebuild.binary-compatibility")
}

description = """Verifies that Gradle code complies with architectural rules.
    | For example that nullable annotations are used consistently or that or that public api classes do not extend internal types.
""".trimMargin()

dependencies {
    currentClasspath(project(":distributions-full"))
    testImplementation(project(":base-services"))
    testImplementation(project(":model-core"))
    testImplementation(project(":file-temp"))
    testImplementation(project(":core"))
    testImplementation(libs.futureKotlin("stdlib"))
    testImplementation(libs.inject)

    testImplementation(libs.archunitJunit5)
    testImplementation(libs.guava)
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation(libs.assertj)

    testRuntimeOnly(project(":distributions-full"))
}

val acceptedApiChangesFile = layout.projectDirectory.file("src/changes/accepted-public-api-changes.json")

val verifyAcceptedApiChangesOrdering = tasks.register<gradlebuild.binarycompatibility.AlphabeticalAcceptedApiChangesTask>("verifyAcceptedApiChangesOrdering") {
    group = "verification"
    description = "Ensures the accepted api changes file is kept alphabetically ordered to make merging changes to it easier"
    apiChangesFile = acceptedApiChangesFile
}

val sortAcceptedApiChanges = tasks.register<gradlebuild.binarycompatibility.SortAcceptedApiChangesTask>("sortAcceptedApiChanges") {
    group = "verification"
    description = "Sort the accepted api changes file alphabetically"
    apiChangesFile = acceptedApiChangesFile
}

val ruleStoreDir = layout.projectDirectory.dir("src/changes/archunit_store")

tasks {
    val reorderRuleStore by registering(ReorderArchUnitRulesTask::class) {
        ruleFile = ruleStoreDir.file("stored.rules").asFile
    }

    test {
        // Looks like loading all the classes requires more than the default 512M
        maxHeapSize = "1g"

        // Only use one fork, so freezing doesn't have concurrency issues
        maxParallelForks = 1

        inputs.dir(ruleStoreDir)

        systemProperty("org.gradle.public.api.includes", (PublicApi.includes + PublicKotlinDslApi.includes).joinToString(":"))
        systemProperty("org.gradle.public.api.excludes", (PublicApi.excludes + PublicKotlinDslApi.excludes).joinToString(":"))
        jvmArgumentProviders.add(
            ArchUnitFreezeConfiguration(
                ruleStoreDir.asFile,
                providers.gradleProperty("archunitRefreeze").map { true })
        )

        dependsOn(verifyAcceptedApiChangesOrdering)
        enabled = flakyTestStrategy != FlakyTestStrategy.ONLY

        extensions.findByType<PredictiveTestSelectionExtension>()?.apply {
            // PTS doesn't work well with architecture tests which scan all classes
            enabled = false
        }

        finalizedBy(reorderRuleStore)
    }
}

class ArchUnitFreezeConfiguration(
    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.RELATIVE)
    val location: File,
    @get:Optional
    @get:Input
    val refreeze: Provider<Boolean>
) : CommandLineArgumentProvider {

    override fun asArguments(): Iterable<String> {
        val refreezeBoolean = refreeze.getOrElse(false)
        return listOf(
            "-Darchunit.freeze.store.default.path=${location.absolutePath}",
            "-Darchunit.freeze.refreeze=${refreezeBoolean}",
            "-Darchunit.freeze.store.default.allowStoreUpdate=${refreezeBoolean}"
        )
    }
}

/**
 * Sorts the stored rules, so we keep a deterministic order when we add new rules.
 */
abstract class ReorderArchUnitRulesTask : DefaultTask() {
    @get:OutputFile
    abstract var ruleFile: File

    @TaskAction
    fun resortStoredRules() {
        val lines = ruleFile.readLines()
        val sortedLines = lines.sortedBy { line ->
            // We sort by the rule name
            line.substringBefore("=")
        }

        if (lines != sortedLines) {
            ruleFile.writeText(sortedLines.joinToString("\n"))
        }
    }
}
