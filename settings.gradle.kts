
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
]import org.gradle.api.internal.FeaturePreviews
import java.io.PrintWriter
import java.io.Serializable

pluginManagement {
    repositories {
        maven {
            url = uri("https://repo.gradle.org/gradle/enterprise-libs-release-candidates")
            content {
                val rcAndMilestonesPattern = "\\d{1,2}?\\.\\d{1,2}?(\\.\\d{1,2}?)?-((rc-\\d{1,2}?)|(milestone-\\d{1,2}?))"
                // GE plugin marker artifact
                includeVersionByRegex("com.gradle.enterprise", "com.gradle.enterprise.gradle.plugin", rcAndMilestonesPattern)
                // GE plugin jar
                includeVersionByRegex("com.gradle", "gradle-enterprise-gradle-plugin", rcAndMilestonesPattern)
            }
        }
        maven {
            name = "Gradle public repository"
            url = uri("https://repo.gradle.org/gradle/public")
            content {
                includeModule("org.openmbee.junit", "junit-xml-parser")
            }
        }
        gradlePluginPortal()
    }
}

plugins {
    id("com.gradle.enterprise").version("3.16.2") // Sync with `build-logic-commons/build-platform/build.gradle.kts`
    id("io.github.gradle.gradle-enterprise-conventions-plugin").version("0.9.1")
    id("org.gradle.toolchains.foojay-resolver-convention") version ("0.8.0")
//    id("net.ltgt.errorprone").version("3.1.0")
}

includeBuild("build-logic-commons")
includeBuild("build-logic")

apply(from = "gradle/shared-with-buildSrc/mirrors.settings.gradle.kts")

val architectureElements = mutableListOf<ArchitectureElementBuilder>()

// If you include a new subproject here, you will need to execute the
// ./gradlew generateSubprojectsInfo
// task to update metadata about the build for CI

unassigned {
    subproject("distributions-dependencies") // platform for dependency versions
    subproject("core-platform")              // platform for Gradle distribution core
}

// Gradle Distributions - for testing and for publishing a full distribution
unassigned {
    subproject("distributions-full")
}

// Gradle implementation projects
unassigned {
    subproject("core")
    subproject("plugins")
    subproject("build-events")
    subproject("diagnostics")
    subproject("composite-builds")
    subproject("core-api")
}

// Core platform
val core = platform("core") {

    // Core Runtime Module
    module("core-runtime") {
        subproject("base-annotations")
        subproject("base-services")
        subproject("bootstrap")
        subproject("build-operations")
        subproject("build-option")
        subproject("build-profile")
        subproject("cli")
        subproject("distributions-basics")
        subproject("distributions-core")
        subproject("file-temp")
        subproject("files")
        subproject("functional")
        subproject("installation-beacon")
        subproject("instrumentation-agent")
        subproject("instrumentation-declarations")
        subproject("internal-instrumentation-api")
        subproject("internal-instrumentation-processor")
        subproject("launcher")
        subproject("logging")
        subproject("logging-api")
        subproject("messaging")
        subproject("native")
        subproject("process-services")
        subproject("worker-services")
        subproject("wrapper")
        subproject("wrapper-shared")
    }

    // Core Configuration Module
    module("core-configuration") {
        subproject("api-metadata")
        subproject("base-services-groovy")
        subproject("configuration-cache")
        subproject("file-collections")
        subproject("input-tracking")
        subproject("kotlin-dsl")
        subproject("kotlin-dsl-provider-plugins")
        subproject("kotlin-dsl-tooling-builders")
        subproject("kotlin-dsl-tooling-models")
        subproject("kotlin-dsl-plugins")
        subproject("kotlin-dsl-integ-tests")
        subproject("model-core")
        subproject("model-groovy")
        subproject("declarative-dsl-api")
        subproject("declarative-dsl-provider")
        subproject("declarative-dsl-core")
    }

    // Core Execution Module
    module("core-execution") {
        subproject("build-cache")
        subproject("build-cache-base")
        subproject("build-cache-local")
        subproject("build-cache-http")
        subproject("build-cache-packaging")
        subproject("build-cache-spi")
        subproject("file-watching")
        subproject("execution")
        subproject("hashing")
        subproject("persistent-cache")
        subproject("snapshots")
        subproject("worker-processes")
        subproject("workers")
    }
}

// Documentation Module
module("documentation") {
    subproject("docs")
    subproject("docs-asciidoctor-extensions-base")
    subproject("docs-asciidoctor-extensions")
    subproject("samples")
}

// IDE Module
module("ide") {
    subproject("base-ide-plugins")
    subproject("ide")
    subproject("ide-native")
    subproject("ide-plugins")
    subproject("problems")
    subproject("problems-api")
    subproject("tooling-api")
    subproject("tooling-api-builders")
}

// Software Platform
val software = platform("software") {
    uses(core)
    subproject("antlr")
    subproject("build-init")
    subproject("dependency-management")
    subproject("plugins-distribution")
    subproject("distributions-publishing")
    subproject("ivy")
    subproject("maven")
    subproject("platform-base")
    subproject("plugins-version-catalog")
    subproject("publish")
    subproject("resources")
    subproject("resources-http")
    subproject("resources-gcs")
    subproject("resources-s3")
    subproject("resources-sftp")
    subproject("reporting")
    subproject("security")
    subproject("signing")
    subproject("testing-base")
    subproject("test-suites-base")
    subproject("version-control")
}

// JVM Platform
val jvm = platform("jvm") {
    uses(core)
    uses(software)
    subproject("code-quality")
    subproject("core-jvm")
    subproject("distributions-jvm")
    subproject("ear")
    subproject("jacoco")
    subproject("jvm-services")
    subproject("language-groovy")
    subproject("language-java")
    subproject("language-jvm")
    subproject("toolchains-jvm")
    subproject("java-compiler-plugin")
    subproject("java-platform")
    subproject("normalization-java")
    subproject("platform-jvm")
    subproject("plugins-groovy")
    subproject("plugins-java")
    subproject("plugins-java-base")
    subproject("plugins-java-library")
    subproject("plugins-jvm-test-fixtures")
    subproject("plugins-jvm-test-suite")
    subproject("plugins-test-report-aggregation")
    subproject("scala")
    subproject("testing-jvm")
    subproject("testing-jvm-infrastructure")
    subproject("testing-junit-platform")
    subproject("war")
}

// Extensibility Platform
platform("extensibility") {
    uses(core)
    uses(jvm)
    subproject("plugin-use")
    subproject("plugin-development")
    subproject("test-kit")
}

// Native Platform
platform("native") {
    uses(core)
    uses(software)
    subproject("distributions-native")
    subproject("platform-native")
    subproject("language-native")
    subproject("tooling-native")
    subproject("testing-native")
}


// Develocity Module
module("enterprise") {
    subproject("enterprise")
    subproject("enterprise-logging")
    subproject("enterprise-operations")
    subproject("enterprise-plugin-performance")
    subproject("enterprise-workers")
}

module("build-infrastructure") {
    subproject("precondition-tester")
}

// Internal utility and verification projects
unassigned {
    subproject("architecture-test")
    subproject("internal-testing")
    subproject("internal-integ-testing")
    subproject("internal-performance-testing")
    subproject("internal-architecture-testing")
    subproject("internal-build-reports")
    subproject("integ-test")
    subproject("distributions-integ-tests")
    subproject("soak")
    subproject("smoke-test")
    subproject("performance")
    subproject("smoke-ide-test") // eventually should be owned by IDEX team
}

rootProject.name = "gradle"

FeaturePreviews.Feature.values().forEach { feature ->
    if (feature.isActive) {
        enableFeaturePreview(feature.name)
    }
}

fun remoteBuildCacheEnabled(settings: Settings) = settings.buildCache.remote?.isEnabled == true

fun getBuildJavaHome() = System.getProperty("java.home")

gradle.settingsEvaluated {
    if ("true" == System.getProperty("org.gradle.ignoreBuildJavaVersionCheck")) {
        return@settingsEvaluated
    }

    if (!JavaVersion.current().isJava11) {
        throw GradleException("This build requires JDK 11. It's currently ${getBuildJavaHome()}. You can ignore this check by passing '-Dorg.gradle.ignoreBuildJavaVersionCheck=true'.")
    }
}

// region platform include DSL

gradle.rootProject {
    tasks.register("architectureDoc", GeneratorTask::class.java) {
        description = "Generates the architecture documentation"
        outputFile = layout.projectDirectory.file("architecture/README.md")
        elements = provider { architectureElements.map { it.build() } }
    }
}

abstract class GeneratorTask : DefaultTask() {
    private val markerComment = "<!-- This diagram is generated. Use `./gradlew :architectureDoc` to update it -->"
    private val startDiagram = "```mermaid"
    private val endDiagram = "```"

    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    @get:Input
    abstract val elements: ListProperty<ArchitectureElement>

    @TaskAction
    fun generate() {
        val markdownFile = outputFile.asFile.get()
        val content = markdownFile.readText().lines()
        val markerPos = content.indexOfFirst { it.contains(markerComment) }
        if (markerPos < 0) {
            throw IllegalArgumentException("Could not locate the generated diagram in $markdownFile")
        }
        val endPos = content.subList(markerPos, content.size).indexOfFirst { it.contains(endDiagram) && !it.contains(startDiagram) }
        if (endPos < 0) {
            throw IllegalArgumentException("Could not locate the end of the generated diagram in $markdownFile")
        }
        val head = content.subList(0, markerPos)

        markdownFile.bufferedWriter().use {
            PrintWriter(it).run {
                for (line in head) {
                    println(line)
                }
                graph(elements.get())
            }
        }
    }

    private fun PrintWriter.graph(elements: List<ArchitectureElement>) {
        println(
            """
            $markerComment
            $startDiagram
        """.trimIndent()
        )
        val writer = NodeWriter(this, "    ")
        writer.node("graph TD")
        for (element in elements) {
            if (element is Platform) {
                writer.platform(element)
            } else {
                writer.element(element)
            }
        }
        println(endDiagram)
    }

    private fun NodeWriter.platform(platform: Platform) {
        println()
        node("subgraph ${platform.id}[\"${platform.name} platform\"]") {
            for (child in platform.children) {
                element(child)
            }
        }
        node("end")
        node("style ${platform.id} fill:#c2e0f4,stroke:#3498db,stroke-width:2px,color:#000;")
        for (dep in platform.uses) {
            node("${platform.id} --> $dep")
        }
    }

    private fun NodeWriter.element(element: ArchitectureElement) {
        println()
        node("${element.id}[\"${element.name} module\"]")
        node("style ${element.id} stroke:#1abc9c,fill:#b1f4e7,stroke-width:2px,color:#000;")
    }

    private class NodeWriter(private val writer: PrintWriter, private val indent: String) {
        fun println() {
            writer.println()
        }

        fun node(node: String) {
            writer.print(indent)
            writer.println(node)
        }

        fun node(node: String, builder: NodeWriter.() -> Unit) {
            writer.print(indent)
            writer.println(node)
            builder(NodeWriter(writer, "$indent    "))
        }
    }
}

/**
 * Defines a top-level architecture module.
 */
fun module(moduleName: String, moduleConfiguration: ArchitectureModuleBuilder.() -> Unit) {
    val module = ArchitectureModuleBuilder(moduleName)
    architectureElements.add(module)
    module.moduleConfiguration()
}

/**
 * Defines a platform.
 */
fun platform(platformName: String, platformConfiguration: PlatformBuilder.() -> Unit): PlatformBuilder {
    val platform = PlatformBuilder(platformName)
    architectureElements.add(platform)
    platform.platformConfiguration()
    return platform
}

/**
 * Defines a bucket of unassigned projects.
 */
fun unassigned(moduleConfiguration: ProjectScope.() -> Unit) =
    ProjectScope("subprojects").moduleConfiguration()

class ProjectScope(
    private val basePath: String
) {
    fun subproject(projectName: String) {
        include(projectName)
        project(":$projectName").projectDir = file("$basePath/$projectName")
    }
}

class ElementId(val id: String) : Serializable {
    override fun toString(): String {
        return id
    }
}

sealed class ArchitectureElement(
    val name: String,
    val id: ElementId
) : Serializable

class Platform(name: String, id: ElementId, val uses: List<ElementId>, val children: List<ArchitectureModule>) : ArchitectureElement(name, id)

class ArchitectureModule(name: String, id: ElementId) : ArchitectureElement(name, id)

sealed class ArchitectureElementBuilder(
    val name: String
) {
    val id: ElementId = ElementId(name.replace("-", "_"))

    abstract fun build(): ArchitectureElement
}

class ArchitectureModuleBuilder(
    name: String,
    private val projectScope: ProjectScope
) : ArchitectureElementBuilder(name) {
    constructor(name: String) : this(name, ProjectScope("platforms/$name"))

    fun subproject(projectName: String) {
        projectScope.subproject(projectName)
    }

    override fun build(): ArchitectureModule {
        return ArchitectureModule(name, id)
    }
}

class PlatformBuilder(
    name: String,
    private val projectScope: ProjectScope
) : ArchitectureElementBuilder(name) {
    private val modules = mutableListOf<ArchitectureModuleBuilder>()
    private val uses = mutableListOf<PlatformBuilder>()

    constructor(name: String) : this(name, ProjectScope("platforms/$name"))

    fun subproject(projectName: String) {
        projectScope.subproject(projectName)
    }

    fun uses(platform: PlatformBuilder) {
        uses.add(platform)
    }

    fun module(platformName: String, moduleConfiguration: ArchitectureModuleBuilder.() -> Unit) {
        val module = ArchitectureModuleBuilder(platformName)
        modules.add(module)
        module.moduleConfiguration()
    }

    override fun build(): Platform {
        return Platform(name, id, uses.map { it.id }, modules.map { it.build() })
    }
}

// endregion
