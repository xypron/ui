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

import java.util.TreeMap;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Buffered access to icon resources
 */
public class IconBuffer {

    private static IdeText ideText = null;
    private static ClassLoader loader;
    private static TreeMap<String, Icon> classMap = null;
    private static TreeMap<String, Icon> iconMap = null;

    /*
     * Initialize the class
     */
    private static void init() {
        if (classMap == null) {
            classMap = new TreeMap<String, Icon>();
            iconMap = new TreeMap<String, Icon>();
            ideText = IdeText.getIdeText();
            loader = IconBuffer.class.getClassLoader();
        }
    }

    /**
     * Get icon by name
     * @param iconName
     * @return icon
     */
    public static Icon getIcon(String iconName) {
        Icon icon = null;

        init();

        if (iconMap.containsKey(iconName)) {
            return iconMap.get(iconName);
        }

        try {
            icon = new ImageIcon(loader.getResource(iconName));
        } catch (Exception e1) {
        }
        iconMap.put(iconName, icon);
        return icon;
    }

    /**
     * Get an icon for the class of an element
     * @param cls class of an element
     * @return icon
      * //TODO use annotation for icon file
     */
    public static Icon getIcon(Class<?> cls) {
        Class<?> clas1 = cls;
        String className;
        Icon icon = null;
        String iconName;

        init();

        className = clas1.getName();
        if (classMap.containsKey(className)) {
            return classMap.get(className);
        }

        do {
            iconName = ideText.getText("Icon." + className);
            try {
                icon = new ImageIcon(loader.getResource(iconName));
                break;
            } catch (Exception e1) {
            }
            clas1 = clas1.getSuperclass();
            className = clas1.getName();
            if (classMap.containsKey(className)) {
                icon = classMap.get(className);
                break;
            }
        } while (clas1 != Object.class);
        classMap.put(cls.getName(), icon);
        return icon;
    }
}
