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
import java.util.Map.Entry;
import java.util.TreeMap;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;

/**
 * A disposable tabbed pane.
 */
@SuppressWarnings("serial")
public class IdeTabbedPane extends JTabbedPane
        implements IdeDisposable {

    /**
     * Components of the tabbed pane.
     */
    private TreeMap<String, Component> elements;

    /**
     * Construct a new dispoable tabbed pane.
     */
    public IdeTabbedPane() {
        elements = new TreeMap<String, Component>();
    }

    /**
     * Add a tab to the tabbed pane.
     * @param key key to identify the tab
     * @param component component diplayed on the tab pane
     * @param title title of the tab
     * @param icon icon of the tab
     * @param tip tooltip of the tab
     */
    public final void setComponent(final String key, final JComponent component,
            final String title, final Icon icon, final String tip) {
        setComponent(key, component, title, icon, tip, false);
    }

    /**
     * Add a tab to the tabbed pane.
     * @param key key to identify the tab
     * @param component component diplayed on the tab pane
     * @param title title of the tab
     * @param icon icon of the tab
     * @param tip tooltip of the tab
     * @param closeable exhibit close button
     */
    public final void setComponent(final String key, final JComponent component,
            final String title, final Icon icon, final String tip,
            final boolean closeable) {
        int index;
        Component cmp;

        if (elements.containsKey(key)) {
            cmp = elements.get(key);
            // dispose old element
            if (component.equals(cmp)) {
                return;
            }
            index = this.indexOfComponent(cmp);
            remove(index);
        } else {
            index = getTabCount();
        }
        elements.put(key, component);
        this.insertTab(title, icon, component, tip, index);

        new IdeTab(this, index, closeable);
        this.setSelectedIndex(index);
    }

    @Override
    public final void remove(final int index) {
        String key = null;
        Component component = super.getComponentAt(index);
        for (Entry<String, Component> entry : elements.entrySet()) {
            if (entry.getValue() == component) {
                key = entry.getKey();
                break;
            }
        }
        elements.remove(key);
        disposeComponent(component);
        super.remove(index);
    }

    @Override
    public void dispose() throws Throwable {

        for (int i = this.getComponentCount() - 1; i >= 0; i--) {
            disposeComponent(this.getComponent(i));
        }
        this.removeAll();
    }

    /**
     * Dispose of component.
     * @param component component
     */
    private void disposeComponent(final Component component) {
        IdeDisposable disposable;

        if (component instanceof IdeDisposable) {
            disposable = (IdeDisposable) component;
            try {
                disposable.dispose();
            } catch (Throwable ex) {
            }
        }
    }
}
