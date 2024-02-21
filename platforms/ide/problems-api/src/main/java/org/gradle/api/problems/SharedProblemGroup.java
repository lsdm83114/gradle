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

package org.gradle.api.problems;

import org.apache.commons.lang.StringUtils;
import org.gradle.api.Incubating;

import javax.annotation.Nullable;
import java.util.Locale;

/**
 * TODO javadoc.
 *
 * @since 8.8
 */
@Incubating
public enum SharedProblemGroup implements ProblemGroup {

    /**
     * A generic problem category. All problems IDs not configuring any group will be automatically use this group.
     *
     * @since 8.8
     */
    GENERIC();

    private final String id;
    private final String displayName;
    private final ProblemGroup parent;

    SharedProblemGroup() {
        this(null);
    }

    SharedProblemGroup(@Nullable ProblemGroup parent) {
        this.id = StringUtils.lowerCase(name(), Locale.ROOT);
        this.displayName = StringUtils.capitalize(this.id);
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
