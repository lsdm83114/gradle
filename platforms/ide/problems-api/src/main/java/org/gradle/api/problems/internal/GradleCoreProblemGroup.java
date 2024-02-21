/*
 * Copyright 2024 the original author or authors.
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

package org.gradle.api.problems.internal;

import org.gradle.api.problems.ProblemGroup;
import org.gradle.api.problems.SharedProblemGroup;

import javax.annotation.Nullable;

public enum GradleCoreProblemGroup implements ProblemGroup {

    /**
     * TODO javadoc.
     *
     * @since 8.8
     */
    VALIDATION("validation", "Validation"),
    /**
     * TODO javadoc.
     *
     * @since 8.8
     */
    TYPE_VALIDATION("gradle-type-validation", "Gradle type validation", VALIDATION),
    /**
     * TODO javadoc.
     *
     * @since 8.8
     */
    PROPERTY_VALIDATION("gradle-property-validation", "Gradle property validation", VALIDATION),
    /**
     * TODO javadoc.
     *
     * @since 8.8
     */
    GROOVY_DSL_COMPILATION("groovy-dsl", "Groovy DSL script compilation", SharedProblemGroup.COMPILATION),
    /**
     * TODO javadoc.
     *
     * @since 8.8
     */
    TASK_SELECTION("task-selection", "Task selection"),
    /**
     * TODO javadoc.
     *
     * @since 8.8
     */
    DEPENDENCY_VERSION_CATALOG("dependency-version-catalog", "Version catalog", null);

    private final String id;
    private final String displayName;
    private final ProblemGroup parent;

    GradleCoreProblemGroup(String id, String displayName) {
        this(id, displayName, null);
    }

    GradleCoreProblemGroup(String id, String displayName, @Nullable ProblemGroup parent) {
        this.id = id;
        this.displayName = displayName;
        this.parent = parent;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Nullable
    @Override
    public ProblemGroup getParent() {
        return parent;
    }
}
