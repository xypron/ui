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
import javax.swing.JSplitPane;

/**
 * Disposable JSplitPane.
 */
@SuppressWarnings("serial")
public class IdeSplitPane extends JSplitPane
        implements IdeDisposable {

    /**
     * Create horizontal split pane.
     */
    public IdeSplitPane() {
        this(HORIZONTAL_SPLIT, false, null, null);
    }

    /**
     * Create split pane.
     * @param newOrientation
     */
    public IdeSplitPane(final int newOrientation) {
        this(newOrientation, false, null, null);
    }

    /**
     * Create split pane.
     * @param newOrientation horizontal or vertial
     * @param newContinuousLayout true if components shall be repainted
     * during change of the split position
     * @see javax.swing.JSplitPane#HORIZONTAL_SPLIT
     * @see javax.swing.JSplitPane#VERTICAL_SPLIT
     */
    public IdeSplitPane(final int newOrientation,
            final boolean newContinuousLayout) {
        this(newOrientation, newContinuousLayout, null, null);
    }

    /**
     * Create split pane.
     * @param newOrientation horizontal or vertical
     * @param newLeftComponent left/upper component
     * @param newRightComponent right/lower component
     * @see javax.swing.JSplitPane#HORIZONTAL_SPLIT
     * @see javax.swing.JSplitPane#VERTICAL_SPLIT
     */
    public IdeSplitPane(final int newOrientation,
            final Component newLeftComponent,
            final Component newRightComponent) {
        this(newOrientation, false, newLeftComponent, newRightComponent);
    }

    /**
     * Create split pane.
     * @param newOrientation horizontal or vertical
     * @param newContinuousLayout true if components shall be repainted
     * @param newLeftComponent left/upper component
     * @param newRightComponent right/lower component
     * @see javax.swing.JSplitPane#HORIZONTAL_SPLIT
     * @see javax.swing.JSplitPane#VERTICAL_SPLIT
     */
    public IdeSplitPane(int newOrientation, boolean newContinuousLayout,
            Component newLeftComponent, Component newRightComponent) {
        super(newOrientation, newContinuousLayout, newLeftComponent,
                newRightComponent);
    }

    @Override
    public void setLeftComponent(Component comp) {
        try {
            leftDispose();
        } catch (Throwable e) {
        }
        super.setLeftComponent(comp);
    }

    @Override
    public void setRightComponent(Component comp) {
        try {
            rightDispose();
        } catch (Throwable e) {
        }
        super.setRightComponent(comp);
    }

    /* (non-Javadoc)
     * @see javax.swing.JSplitPane#setTopComponent(java.awt.Component)
     */
    @Override
    public void setTopComponent(Component comp) {
        setLeftComponent(comp);
    }

    @Override
    public void setBottomComponent(Component comp) {
        setRightComponent(comp);
    }

    @Override
    public void dispose() throws Throwable {
        leftDispose();
        rightDispose();
    }

    /**
     * This method removes all references to the left component.
     */
    private synchronized void leftDispose() throws Throwable {
        Component component;
        IdeDisposable disposable;
        component = this.getLeftComponent();
        if (component instanceof IdeDisposable) {
            disposable = (IdeDisposable) component;
            disposable.dispose();
        }
    }

    /**
     * This method removes all references to the right component.
     */
    private synchronized void rightDispose() throws Throwable {
        Component component;
        IdeDisposable disposable;
        component = this.getRightComponent();
        if (component instanceof IdeDisposable) {
            disposable = (IdeDisposable) component;
            disposable.dispose();
        }
    }
}
