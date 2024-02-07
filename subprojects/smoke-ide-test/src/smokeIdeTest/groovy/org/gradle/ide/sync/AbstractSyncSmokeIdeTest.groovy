/*
 * Copyright 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.ide.sync

import org.gradle.integtests.fixtures.AbstractIntegrationSpec
import org.gradle.internal.UncheckedException
import org.gradle.internal.jvm.Jvm
import org.gradle.profiler.BuildAction
import org.gradle.profiler.BuildMutator
import org.gradle.profiler.GradleBuildConfiguration
import org.gradle.profiler.InvocationSettings
import org.gradle.profiler.Logging
import org.gradle.profiler.Profiler
import org.gradle.profiler.ScenarioContext
import org.gradle.profiler.gradle.DaemonControl
import org.gradle.profiler.gradle.GradleBuildInvoker
import org.gradle.profiler.gradle.GradleScenarioDefinition
import org.gradle.profiler.gradle.GradleScenarioInvoker
import org.gradle.profiler.instrument.PidInstrumentation
import org.gradle.profiler.studio.AndroidStudioSyncAction
import org.gradle.profiler.studio.invoker.StudioBuildInvocationResult
import org.gradle.profiler.studio.invoker.StudioGradleScenarioDefinition
import org.gradle.profiler.studio.invoker.StudioGradleScenarioInvoker

import java.nio.file.Path
import java.util.function.Consumer

/**
 * Tests that runs a project import to IDE, with an optional provisioning of the desired IDE.
 *
 * Provisioned IDEs are cached in the `ideHome` directory.
 */
abstract class AbstractSyncSmokeIdeTest extends AbstractIntegrationSpec {

    private static final String INTELLIJ_COMMUNITY_TYPE = "IC"

    private static final String ANDROID_STUDIO_TYPE = "AI"

    private final Path ideHome = buildContext.gradleUserHomeDir.file("ide").toPath()

    protected StudioBuildInvocationResult syncResult

    /**
     * Downloads, if it absent in `ideHome` dir, Android Studio with a passed version
     * and runs a project import to it.
     *
     * Requires ANDROID_HOME env. variable set with Android SDK (normally on MacOS it's installed in "$HOME/Library/Android/sdk").
     *
     * Local Android Studio installation can be passed via `studioHome` system property and it takes precedence over
     * a version passed as a parameter.
     */
    protected void androidStudioSync(String version) {
        assert System.getenv("ANDROID_HOME") != null
        String androidHomePath = System.getenv("ANDROID_HOME")

        def invocationSettings =
            syncInvocationSettingsBuilder(getIdeInstallDirFromSystemProperty("studioHome")).build()

        sync(
            ANDROID_STUDIO_TYPE,
            version,
            null,
            ideHome,
            "Android Studio sync",
            invocationSettings,
            [new LocalPropertiesMutator(invocationSettings, androidHomePath)]
        )
    }

    /**
     * Downloads, if it absent in `ideHome` dir, Intellij IDEA with a passed version and a build type,
     * and runs a project import to it.
     *
     * Available build types are: release, eap, rc

     * Local IDEA installation can be passed via `ideaHome` system property and it takes precedence over
     * a version passed as a parameter.
     */
    protected void ideaSync(String buildType, String version) {
        def invocationSettings =
            syncInvocationSettingsBuilder(getIdeInstallDirFromSystemProperty("ideaHome")).build()

        sync(
            INTELLIJ_COMMUNITY_TYPE,
            version,
            buildType,
            ideHome,
            "IDEA sync",
            invocationSettings,
            Collections.emptyList()
        )
    }

    private File getIdeInstallDirFromSystemProperty(String propertyName) {
        def dir = System.getProperty(propertyName)
        return dir != null ? new File(dir) : null
    }

    private InvocationSettings.InvocationSettingsBuilder syncInvocationSettingsBuilder(File ideInstallDir) {
        def ideSandbox = file("ide-sandbox")
        def profilerOutput = file('profiler-output')

        return new InvocationSettings.InvocationSettingsBuilder()
            .setProjectDir(testDirectory)
            .setProfiler(Profiler.NONE)
            .setStudioInstallDir(ideInstallDir)
            .setStudioSandboxDir(ideSandbox)
            .setGradleUserHome(buildContext.gradleUserHomeDir)
            .setVersions([distribution.version.version])
            .setScenarioFile(null)
            .setBenchmark(true)
            .setOutputDir(profilerOutput)
            .setWarmupCount(1)
            .setIterations(1)
    }

    private void sync(
        String ideType,
        String ideVersion,
        String ideBuildType,
        Path ideHome,
        String scenarioName,
        InvocationSettings invocationSettings,
        List<BuildMutator> buildMutators
    ) {
        // TODO consider passing jvmArgs where it's sane
        def syncScenario = new StudioGradleScenarioDefinition(
            new GradleScenarioDefinition(
                scenarioName,
                scenarioName,
                GradleBuildInvoker.AndroidStudio,
                new GradleBuildConfiguration(
                    distribution.getVersion(),
                    distribution.gradleHomeDir,
                    Jvm.current().getJavaHome(),
                    Collections.emptyList(), // daemon args
                    false,
                    Collections.emptyList() // client args
                ),
                new AndroidStudioSyncAction(),
                BuildAction.NO_OP,
                Collections.emptyList(),
                new HashMap<String, String>(),
                buildMutators,
                invocationSettings.warmUpCount,
                invocationSettings.buildCount,
                invocationSettings.outputDir,
                Collections.emptyList(),
                Collections.emptyList()
            ),
            Collections.emptyList(),
            Collections.emptyList(),
            ideType,
            ideVersion,
            ideBuildType,
            ideHome
        )

        def scenarioInvoker = new StudioGradleScenarioInvoker(
            new GradleScenarioInvoker(
                new DaemonControl(invocationSettings.getGradleUserHome()),
                createPidInstrumentation()
            )
        )

        Logging.setupLogging(invocationSettings.outputDir)

        try {
            scenarioInvoker.run(
                syncScenario,
                invocationSettings,
                new Consumer<StudioBuildInvocationResult>() {
                    @Override
                    void accept(StudioBuildInvocationResult studioBuildInvocationResult) {
                        syncResult = studioBuildInvocationResult
                    }
                })
        } catch (IOException | InterruptedException e) {
            throw UncheckedException.throwAsUncheckedException(e)
        } finally {
            try {
                Logging.resetLogging()
            } catch (IOException e) {
                e.printStackTrace()
            }
        }
    }

    private static PidInstrumentation createPidInstrumentation() {
        try {
            return new PidInstrumentation()
        } catch (IOException e) {
            throw new RuntimeException(e)
        }
    }

    private static class LocalPropertiesMutator implements BuildMutator {
        private final String androidSdkRootPath
        private final InvocationSettings invocation

        LocalPropertiesMutator(InvocationSettings invocation, String androidSdkRootPath) {
            this.invocation = invocation
            this.androidSdkRootPath = androidSdkRootPath
        }

        @Override
        void beforeScenario(ScenarioContext context) {
            def localProperties = new File(invocation.projectDir, "local.properties")
            localProperties << "\nsdk.dir=$androidSdkRootPath\n"
        }
    }
}
