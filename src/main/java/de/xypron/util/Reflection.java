/*
 *  Copyright 2009 Heinrich Schuchardt.
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
 * Enhanced reflectivty
 */
public class Reflection {

    /**
     * Determine if a class is a subclass of another class
     * @param cls
     * @param superClass
     * @return true if cls is a subclass of superClass
     */
    public static boolean isSubclassOf(Class<?> cls, Class<?> superClass) {
        Class<?> thisClass;

        thisClass = cls;
        while (thisClass != null) {
            if (thisClass == superClass) {
                return true;
            }
            thisClass = thisClass.getSuperclass();
        }
        return false;
    }

    /**
     * Create number from string
     * @param cls class of object to create
     * @param value value
     * @return number
     */
    public static Number fromString(Class<?> cls, String value) {
        if (cls == Long.class) {
            return new Long(value);
        } else if (cls == long.class) {
            return (long) new Long(value);
        } else if (cls == Integer.class) {
            return new Integer(value);
        } else if (cls == int.class) {
            return (int) new Integer(value);
        } else if (cls == Double.class) {
            return new Double(value);
        } else if (cls == double.class) {
            return (double) new Double(value);
        } else {
            throw new UnsupportedOperationException(
                    Reflection.class.getName()
                    + ".fromString("
                    + cls.getName()
                    + ","
                    + value
                    + ")");
        }
    }
}