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
 * Buffered access to icon resources.
 */
public class IconBuffer {

    private static ClassLoader loader;
    private static TreeMap<String, Icon> classMap = null;
    private static TreeMap<String, Icon> iconMap = null;

    /*
     * Initializes the class.
     */
    private static void init() {
        if (classMap == null) {
            classMap = new TreeMap<String, Icon>();
            iconMap = new TreeMap<String, Icon>();
            loader = IconBuffer.class.getClassLoader();
        }
    }

    /**
     * Gets icon by class annotation.
     * @see de.xypron.util.IconName
     * @param cls annotated class
     * @return icon
     */
    public static Icon getIcon(Class cls) {
        IconName annotation = null;
        
        if (cls == null) {
            return null;
        }

        while (annotation == null && !cls.equals(Object.class)) {
            annotation = (IconName) cls.getAnnotation(IconName.class);
            cls = cls.getSuperclass();
        }
        
        if (annotation == null) {
            return null;
        } else {
            return getIcon(annotation.value());
        }
    }
    
    /**
     * Gets icon by name.
     * @param iconName path to icon
     * @return icon icon
     */
    public static Icon getIcon(final String iconName) {
        Icon icon = null;

        init();
        
        if (iconName == null) {
            return null;
        }

        if (iconMap.containsKey(iconName)) {
            return iconMap.get(iconName);
        }

        try {
            icon = new ImageIcon(loader.getResource(iconName));
        } catch (Exception e1) {
            icon = null;
        }
        iconMap.put(iconName, icon);
        return icon;
    }
}
