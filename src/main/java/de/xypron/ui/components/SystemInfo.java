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
package de.xypron.ui.components;

/**
 * Determine system data
 */
public class SystemInfo {
    private final static long BYTES_PER_MB = 1048576;

    /**
     * Returns a multiline string with information about the Java release
     * and the available heap space
     * @return information
     */
    public static String info() {
        String bits = null;
        String info = "";

        try {
            bits = System.getProperty("com.ibm.vm.bitmode");
            if (bits == null) {
                bits = System.getProperty("sun.arch.data.model");
            }

            info = "Java vendor: " + System.getProperty("java.vendor")
                    + "\nJava version: " + System.getProperty("java.version");
            if (bits != null) {
                info += ", " + bits + " bit";
            }
            info += "\ntotal heap: "
                    + Runtime.getRuntime().totalMemory() / BYTES_PER_MB + " MB"
                    + "\nfree heap: "
                    + Runtime.getRuntime().freeMemory() / BYTES_PER_MB + " MB"
                    + "\nmax heap: "
                    + Runtime.getRuntime().maxMemory() / BYTES_PER_MB + " MB";
            info += "\noperating system: " + System.getProperty("os.name")
                    + ", " + System.getProperty("os.version")
                    + ", " + System.getProperty("os.arch");
        } catch (SecurityException ex) {
            info += "\n";
            info += ex.getMessage();
        }
        return info;
    }
}
