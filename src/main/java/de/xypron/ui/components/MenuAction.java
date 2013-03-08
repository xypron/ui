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

import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.swing.AbstractAction;

/**
 * Menu action.
 *
 * @author Heinrich Schuchardt
 */
@SuppressWarnings("serial")
public class MenuAction extends AbstractAction {

    Object obj;
    Method method;

    public MenuAction(Object obj, Method method) {
        this.obj = obj;
        this.method = method;
    }

    @Override
    public void actionPerformed(final ActionEvent arg0) {
        try {
            method.invoke(obj, arg0);
        }
        catch (IllegalAccessException ex) {
        }
        catch (IllegalArgumentException ex) {
        }
        catch (InvocationTargetException ex) {
        }
    }
}