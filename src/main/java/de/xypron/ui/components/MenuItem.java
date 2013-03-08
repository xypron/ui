/*
 * Copyright 2013 Heinrich Schuchardt.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.xypron.ui.components;

import de.xypron.util.IdeText;
import de.xypron.util.MenuItemText;
import java.awt.event.ActionEvent;
import java.lang.reflect.Method;
import javax.swing.JMenuItem;

/**
 * Menu item.
 * @author Heinrich Schuchardt
 */
public class MenuItem extends JMenuItem {

    /**
     * Creates a menu item.
     * @param obj object to be invoked by menu item
     * @param methodName method to be invoked by menu item
     */
    public MenuItem(Object obj, String methodName) {
        MenuItemText annotation;
        Class clas;
        Method method = null;
        String text;

        clas = obj.getClass();

        while (clas != Object.class) {
            try {
                method = clas.getDeclaredMethod(methodName, ActionEvent.class);
            }
            catch (NoSuchMethodException ex) {
                clas = clas.getSuperclass();
                continue;
            }
            catch (SecurityException ex) {
                break;
            }
            break;
        }
        text = methodName;
        if (method
                != null) {
            annotation = method.getAnnotation(MenuItemText.class);
            if (annotation != null) {
                text = annotation.value();
                if (text.length() == 0) {
                    text = methodName;
                }
            }
        }
        this.setText(IdeText.getText(clas, text));
        if (method == null) {
            this.setEnabled(false);
        }

        this.addActionListener(new MenuAction(obj, method));
    }
    
}
