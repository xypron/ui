/*
 *  Copyright 2010 Heinrich Schuchardt.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package de.xypron.util;

/**
 * Definition of comparison operators.
 * @author Heinrich Schuchardt
 */
public interface Filter {
    public enum ComparisonType {
        /**
         * Filter is not active.
         */
        IGNORE,
        /**
         * Less than.
         */
        LESS_THAN,
        /**
         * Less or equal.
         */
        LESS_OR_EQUAL,
        /**
         * Equal.
         */
        EQUALS,
        /**
         * Greater or equal.
         */
        GREATER_OR_EQUAL,
        /**
         * Greater than.
         */
        GREATER_THAN,
        /**
         * Not equal.
         */
        NOT_EQUALS,
        STARTS_WITH,
        STARTS_NOT_WITH,
        ENDS_WITH,
        ENDS_NOT_WITH,
        CONTAINS,
        NOT_CONTAINS
    }
}
