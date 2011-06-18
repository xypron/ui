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

import java.util.Locale;
import junit.framework.TestCase;

/**
 * Test Reflection.
 */
public class ReflectionTest extends TestCase {

    /**
     * Create a test case for the <code>Reflection</code> class.
     * @param testName
     */
    public ReflectionTest(String testName) {
        super(testName);
    }

    /**
     * Set up test.
     * @throws Exception
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * Tear down test.
     * @throws Exception
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of isSubclassOf method, of class Reflection.
     */
    public void testIsSubclassOf() {
        Class<?> cls;
        Class<?> superClass;
        boolean expResult;
        boolean result;

        System.out.println("isSubclassOf");
        cls = Throwable.class;
        superClass = Exception.class;
        expResult = false;
        result = Reflection.isSubclassOf(cls, superClass);
        assertEquals("A Throwable is not an Exception", expResult, result);

        cls = Exception.class;
        superClass = Throwable.class;
        expResult = true;
        result = Reflection.isSubclassOf(cls, superClass);
        assertEquals("An Exception is a Throwable", expResult, result);
    }

    /**
     * Test of fromString method, of class Reflection.
     */
    public void testFromString() {
        Long expResultLong = 3L;
        Double expResultDouble = 3.25;
        String str = "3.25";
        Number num;
        Locale locale = Locale.getDefault();
        Locale.setDefault(Locale.GERMANY);
        
        System.out.println("fromString(Double)");

        num = Reflection.fromString(Double.class, str);

        assertTrue("Is instance of Double", num instanceof Double);
        assertEquals("Double value", num, expResultDouble);

        System.out.println("fromString(Long)");

        str = "3";
        num = Reflection.fromString(Long.class, str);
        assertTrue("Is instance of Long", num instanceof Long);
        assertEquals("Long value", num, expResultLong);

        Locale.setDefault(locale);
        
    }
}
