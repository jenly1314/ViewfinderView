/*
 * Copyright (C) Jenly
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
package com.king.view.viewfinderview.app

import androidx.annotation.IntDef

/**
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
@IntDef(
    ExampleType.VIEWFINDER_CLASSIC_STYLE,
    ExampleType.VIEWFINDER_POPULAR_STYLE,
    ExampleType.SHOW_RESULT_POINT,
    ExampleType.SHOW_CUSTOM_RESULT_POINT,
)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class ExampleType {
    companion object {
        const val VIEWFINDER_CLASSIC_STYLE = 0
        const val VIEWFINDER_POPULAR_STYLE = 1
        const val SHOW_RESULT_POINT = 2
        const val SHOW_CUSTOM_RESULT_POINT = 3
    }
}
