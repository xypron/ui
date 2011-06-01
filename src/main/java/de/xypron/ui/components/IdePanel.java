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

import de.xypron.util.IdeText;
import de.xypron.util.IconBuffer;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Dispoable JPanel.
 */
@SuppressWarnings("serial")
public class IdePanel extends JPanel
        implements IdeDisposable, ActionListener {

    /**
     * Parent frame.
     */
    private Frame parentFrame = null;

    /**
     * Create a new panel.
     */
    @Deprecated
    public IdePanel() {
        this((Frame) null);
    }

    /**
     * Create a new panel.
     * @param parentFrame parent frame
     */
    public IdePanel(final Frame parentFrame) {
        super(new BorderLayout());
        this.parentFrame = parentFrame;
    }

    /**
     * Get parent frame.
     * @return parent frame
     */
    public final Frame getParentFrame() {
        return parentFrame;
    }
    
    /**
     * Get string from resource bundle.
     * @param key property name pointing to string
     * @return string
     */
    protected final String getText(final String key) {
        return IdeText.getText(this.getClass(), key);
    }

    @Override
    public void dispose() throws Throwable {
        Component[] components;
        IdeDisposable disposable;

        components = this.getComponents();
        for (Component component : components) {
            if (component instanceof IdeDisposable) {
                disposable = (IdeDisposable) component;
                disposable.dispose();
            }
        }
    }

    /**
     * Create a button.
     * @param imageName name of the image
     * @param actionCommand action
     * @param toolTipText tooltip text
     * @param altText alternative text
     * @return button
     */
    protected final JButton makeButton(
            final String imageName, final String actionCommand,
            final String toolTipText, final String altText) {

        JButton button = new JButton(IconBuffer.getIcon(
                IdeText.getText(this.getClass(), imageName)));
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setActionCommand(actionCommand);
        button.setToolTipText(toolTipText);
        button.addActionListener(this);

        return button;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
    }
}
