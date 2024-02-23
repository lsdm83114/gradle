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

package org.gradle.internal.logging.events;

import com.google.common.collect.Lists;
import org.apache.commons.lang.BooleanUtils;
import org.gradle.internal.Either;

import java.util.List;

public class YesNoQuestionPromptEvent extends PromptOutputEvent {
    public static final List<String> YES_NO_CHOICES = Lists.newArrayList("yes", "no");

    public YesNoQuestionPromptEvent(long timestamp, String prompt) {
        super(timestamp, prompt, true);
    }

    @Override
    public Either<Boolean, String> convert(String text) {
        if (YES_NO_CHOICES.contains(text)) {
            return Either.left(BooleanUtils.toBoolean(text));
        }
        return Either.right("Please enter 'yes' or 'no': ");
    }
}
