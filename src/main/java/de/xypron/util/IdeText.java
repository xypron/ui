/**
 * Copyright 2009, Heinrich Schuchardt
 *
 * @author Heinrich Schuchardt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package de.xypron.util;

import java.util.HashMap;
import javax.swing.JOptionPane;

/**
 * Helper class to retrieve string resources.
 */
public final class IdeText {
    private static HashMap<Class, java.util.ResourceBundle> resources 
            = new HashMap<Class, java.util.ResourceBundle>();

    /**
     * Show an error message
     * @param key
     */
    public static void errorMessage(Class cls, String key) {
        JOptionPane.showMessageDialog(null,
                getText(cls, key),
                getText(IdeText.class,
                "IdeComponent.Error"),
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * This method returns a language dependent string. The name of the
     * resource bundle is obtained by appending ".strings" to the path
     * of the class. If the string is not found for the provided class
     * its superclasses are searched.
     *
     * @param cls Class for which the property file is read
     * @param key String used as key in properties file.
     * @return String
     */
    public static String getText(Class cls, String key) {
        java.util.ResourceBundle resource;
        Class clas;
        
        if (cls == null) {
            return key;
        }

        if (!resources.containsKey(cls)) {
            clas = cls;
            while (clas != Object.class) {
                String path = clas.getPackage().getName() + ".strings";
                try {
                    resource = java.util.ResourceBundle.getBundle(path);
                } catch (Exception e) {
                    resource = null;
                }
                resources.put(clas, resource);
                clas = clas.getSuperclass();
            }
        }
        clas = cls;
        while (clas != Object.class) {
            try {
                resource = resources.get(clas);
                if (resource != null) {
                    return resource.getString(key);
                }
            } catch (Exception e) {
            }
            clas = clas.getSuperclass();
        }
        return key;
    }
}
