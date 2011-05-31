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

import de.xypron.util.IconBuffer;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Tab component with close button.
 */
@SuppressWarnings("serial")
public class IdeTab extends JPanel implements ActionListener {

    /**
     * Tabbed pane this tab belongs to.
     */
    private IdeTabbedPane pane;

    /**
     * Constructor for tab component without close button
     * @param pane
     * @param index
     */
    public IdeTab(final IdeTabbedPane pane, final int index) {
        this(pane, index, false);
    }

    /**
     * Constructor for tab component with close button.
     * @param pane tabbed pane
     * @param index tab index
     * @param closeable exhibit close button
     */
    public IdeTab(final IdeTabbedPane pane, final int index,
            final boolean closeable) {
        JButton buttonClose;
        JLabel jLabel;
        JLabel spacer;
        FlowLayout layout;

        this.pane = pane;
        layout = new FlowLayout(FlowLayout.LEFT, 0, 0);
        this.setBorder(BorderFactory.createEmptyBorder());
        setLayout(layout);
        setOpaque(false);
        jLabel = (new JLabel(pane.getTitleAt(index), pane.getIconAt(index),
                JLabel.LEFT));
        add(jLabel);
        if (closeable) {
            spacer = new JLabel();
            spacer.setPreferredSize(new Dimension(4, 4));
            add(spacer);
            buttonClose = new CloseButton();
            buttonClose.addActionListener(this);
            add(buttonClose);
        }
        pane.setTabComponentAt(index, this);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        int i = pane.indexOfTabComponent(this);
        if (i != -1) {
            pane.remove(i);
        }
    }

    /**
     * Close button.
     */
    @SuppressWarnings("serial")
    private class CloseButton extends JButton {

        public CloseButton() {
            Icon closeIcon;
            Icon rollOverIcon;
            closeIcon = IconBuffer.getIcon(
                    "de/xypron/ui/components/closeTab.png");
            rollOverIcon = IconBuffer.getIcon(
                    "de/xypron/ui/components/closeTabRollover.png");
            setRolloverIcon(rollOverIcon);
            setIcon(closeIcon);
            if (closeIcon != null) {
                setPreferredSize(new Dimension(
                        closeIcon.getIconWidth(),
                        closeIcon.getIconHeight()));
            } else {
                this.setText("x");
            }
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder());
            setBorderPainted(false);
            setContentAreaFilled(false);
        }
    }
}

