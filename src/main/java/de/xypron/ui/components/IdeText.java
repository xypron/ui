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
package de.xypron.ui.components;

import java.util.HashMap;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

/**
 * Helper class to retrieve string resources.
 */
public class IdeText {

    private static Class ideClass = null;
    private static IdeText ideText = null;
    private HashMap<Class, java.util.ResourceBundle> resources = null;
    private JComponent mainComponent = null;

    /**
     * This method intializes the class IdeText. The properties file
     * location is set to "stings.properties" in the same package
     * where the passed object <code>obj</code> is located.
     * The singleton is created.
     * @param obj Object defining the package where the properties file
     * "strings.properties" is located
     * @return instance
     */
    public static IdeText getIdeText(Object obj) {
        IdeText.ideClass = obj.getClass();
        IdeText.ideText = null;
        return getIdeText();
    }

    /**
     * Get the text reader
     * @return instance
     */
    public static IdeText getIdeText() {
        if (ideText == null) {
            ideText = new IdeText();
        }
        return ideText;
    }

    /**
     * Create text reader
     * @param mainComponent main component for error dialogs
     */
    protected IdeText(JComponent mainComponent) {
        this();
        this.mainComponent = mainComponent;
    }

    private IdeText() {
        java.util.ResourceBundle resource;
        if (resources == null) {
            resources = new HashMap<Class, java.util.ResourceBundle>();
        }
    }

    /**
     *
     * @param key
     */
    public void errorMessage(String key) {
        JOptionPane.showMessageDialog(mainComponent,
                getText(key),
                getText(IdeText.class,
                "IdeComponent.Error"),
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * This method returns a language dependent string.
     *
     * @param key String used as key in properties file.
     * @return String
     */
    public String getText(Class cls, String key) {
        java.util.ResourceBundle resource;
        Class clas;

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
                } else {
                    break;
                }
            } catch (Exception e) {
            }
            clas = clas.getSuperclass();
        }
        return key;
    }

    /**
     * This method returns a language dependent string.
     *
     * @param key String used as key in properties file.
     * @return String
     */
    public String getText(String key) {
        return getText(ideClass, key);
    }
}
