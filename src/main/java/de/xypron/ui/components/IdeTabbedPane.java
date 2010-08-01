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

import java.awt.Component;
import java.util.TreeMap;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;

/**
 * A disposable tabbed pane
 */
public class IdeTabbedPane extends JTabbedPane
        implements IdeDisposable {

    private static final long serialVersionUID = 6534656272673589161L;
    private TreeMap<String, Integer> elements;

    /**
     * Construct a new dispoable tabbed pane
     */
    public IdeTabbedPane() {
        elements = new TreeMap<String, Integer>();
    }

    /**
     * Add a tab to the tabbed pane
     * @param key key to identify the tab
     * @param component component diplayed on the tab pane
     * @param title title of the tab
     * @param icon icon of the tab
     * @param tip tooltip of the tab
     */
    public void setComponent(String key, JComponent component,
            String title, Icon icon, String tip) {
        int index;

        if (elements.containsKey(key)) {
            index = elements.get(key);
            // dispose old element
            if (component.equals(this.getComponent(index))) {
                return;
            }
            disposeAtIndex(index);
            remove(index);
        } else {
            index = getTabCount();
            elements.put(key, index);
        }
        this.insertTab(title, icon, component, tip, index);

        this.setTabComponentAt(index, new IdeTab(this, index));
    }

    private Component newTabComponent(String title, Icon icon, String tip) {
        JLabel jLabel;
        jLabel = new JLabel(title, icon, LEFT);
        jLabel.setToolTipText(tip);
        return jLabel;
    }

    @Override
    public void dispose() throws Throwable {

        for (int i = this.getComponentCount() - 1; i >= 0; i--) {
            disposeAtIndex(i);
        }
        this.removeAll();
    }

    private void disposeAtIndex(int index) {
        Component component;
        IdeDisposable disposable;

        component = this.getComponent(index);
        if (component instanceof IdeDisposable) {
            disposable = (IdeDisposable) component;
            try {
                disposable.dispose();
            } catch (Throwable ex) {
            }
        }
    }
}
