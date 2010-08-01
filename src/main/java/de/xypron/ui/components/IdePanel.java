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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Dispoable JPanel
 */
public class IdePanel extends JPanel
    implements IdeDisposable, ActionListener {
    private static final long serialVersionUID = 5115366079508079617L;
    /**
     * Object to read texts from resource
     */
    protected IdeText ideText;

    /**
     * Create a new panel
     */
    public IdePanel() {
        super(new BorderLayout());
        ideText = IdeText.getIdeText();
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
     * Create a button
     * @param imageName name of the image
     * @param actionCommand action
     * @param toolTipText tooltip text
     * @param altText alternative text
     * @return button
     */
    protected JButton makeButton(
            String imageName, String actionCommand,
            String toolTipText, String altText) {

        JButton button = new JButton(IconBuffer.getIcon(
                ideText.getText(imageName)));
        button.setMargin(new Insets(0,0,0,0));
        button.setActionCommand(actionCommand);
        button.setToolTipText(toolTipText);
        button.addActionListener(this);

        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
}
