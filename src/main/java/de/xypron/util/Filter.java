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
 *
 * @author Heinrich Schuchardt
 */
public interface Filter {
    public enum ComparisonType {
        IGNORE,
        LESS_THAN,
        LESS_OR_EQUAL,
        EQUALS,
        GREATER_OR_EQUAL,
        GREATER_THAN,
        NOT_EQUALS,
        STARTS_WITH,
        STARTS_NOT_WITH,
        ENDS_WITH,
        ENDS_NOT_WITH,
        CONTAINS,
        NOT_CONTAINS
    }
}
