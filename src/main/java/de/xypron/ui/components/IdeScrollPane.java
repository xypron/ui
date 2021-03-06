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

import java.awt.Component;

import javax.swing.JScrollPane;

/**
 * Disposable JScrollPane.
 */
@SuppressWarnings("serial")
public class IdeScrollPane extends JScrollPane
        implements IdeDisposable {

    /**
     * Constructor.
     * @param view component to display in the view port
     * @param vsbPolicy vertical scroll bar policy
     * @param hsbPolicy horizontal scroll bar policy
     */
    public IdeScrollPane(Component view, int vsbPolicy, int hsbPolicy) {
        super(view, vsbPolicy, hsbPolicy);
    }

    /**
     * Constructor.
     * @param view  view component to display in the viewport
     */
    public IdeScrollPane(Component view) {
        super(view);
    }

    /**
     * Constructor.
     * @param vsbPolicy
     * @param hsbPolicy
     */
    public IdeScrollPane(int vsbPolicy, int hsbPolicy) {
        super(vsbPolicy, hsbPolicy);
    }

    /**
     * Constructor.
     */
    public IdeScrollPane() {
        super();
    }

    @Override
    public void setViewportView(Component view) {
        try {
            dispose();
        } catch (Throwable e) {
        }
        super.setViewportView(view);
    }

    @Override
    public void dispose() throws Throwable {
        Component component;
        IdeDisposable disposable;
        component = this.getViewport().getView();
        if (component instanceof IdeDisposable) {
            disposable = (IdeDisposable) component;
            disposable.dispose();
        }
    }
}
