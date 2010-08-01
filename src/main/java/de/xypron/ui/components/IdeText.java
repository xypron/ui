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

import javax.swing.JComponent;
import javax.swing.JOptionPane;

/**
 * Helper class to retrieve string resources.
 */
public class IdeText {
    private static String propertiesPath = null;
    private static IdeText ideText = null;
    private java.util.ResourceBundle resource = null;
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
        IdeText.propertiesPath = obj.getClass().getPackage().getName()
                + ".strings";
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
        if (ideText == null) {
            try {
                resource = java.util.ResourceBundle.getBundle(
                        propertiesPath);
            } catch (Exception e) {
                resource = null;
            }
        }
    }

    /**
     *
     * @param key
     */
    public void errorMessage(String key) {
        JOptionPane.showMessageDialog(mainComponent, getText(key), getText("IdeComponent.Error"),
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * This method returns a language dependent string.
     *
     * @param key String used as key in properties file.
     * @return String
     */
    public String getText(String key) {
        try {
            return resource.getString(key);
        } catch (Exception e) {
            return key;
        }
    }
}
