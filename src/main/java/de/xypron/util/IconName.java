/*
 * Copyright 2011 Heinrich Schuchardt.
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
package de.xypron.util;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Path to the icon for the annotated class. The icon can be retrieved with
 * <code>IconBuffer.getIcon(Class cls)</code>.<p>
 * Example:
 * <pre>
 * &#64;IconName("de/xypron/ui/components/user.png")
 * public class MyClass {
 * ...
 * }
 * </pre>
 * @see IconBuffer
 * @see javax.swing.Icon
 */
@Documented
@Target({java.lang.annotation.ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface IconName {

    /**
     * Path to icon.
     * @return path to icon
     */
    String value();
}
