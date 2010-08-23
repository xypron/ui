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

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;

import java.awt.Desktop;
import java.awt.Frame;
import java.awt.Dimension;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/**
 * This class displays the about dialog. The text is read from a file.
 */
public class About extends JDialog
        implements HyperlinkListener {

    private static final long serialVersionUID = -4045545681349619878L;
    private JScrollPane jScrollPane = null;
    private JTextPane jTextPane = null;
    private IdeText ideText;

    /**
     * @param owner the <code>Frame</code> from which the dialog is displayed
     */
    public About(Frame owner) {
        super(owner, true);
        initialize();
    }

    /**
     * This method initializes this
     *
     */
    private void initialize() {
        ideText = IdeText.getIdeText();
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setSize(new Dimension(800, 600));
        this.setTitle(ideText.getText(this.getClass(), "About.Title"));
        this.setContentPane(getJScrollPane());
        this.setVisible(true);
    }

    /**
     * This method initializes jScrollPane
     *
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new JScrollPane();
            jScrollPane.setViewportView(getJTextPane());
            jScrollPane.getViewport().add(getJTextPane());
        }
        return jScrollPane;
    }

    /**
     * This method initializes jScrollPane
     *
     * @return javax.swing.JScrollPane
     */
    private JTextPane getJTextPane() {
        URL url;
        ClassLoader loader;


        if (jTextPane == null) {
            jTextPane = new JTextPane();

            loader = About.class.getClassLoader();
            url = loader.getResource(ideText.getText(this.getClass(),
                    "About.Url"));
            try {
                jTextPane.setPage(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            jTextPane.setEditable(false);
            jTextPane.addHyperlinkListener(this);
        }
        return jTextPane;
    }

    /* (non-Javadoc)
     * @see javax.swing.event.HyperlinkListener#hyperlinkUpdate(javax.swing.event.HyperlinkEvent)
     */
    @Override
    public void hyperlinkUpdate(HyperlinkEvent e) {
        Desktop desktop;
        if (e.getEventType() != HyperlinkEvent.EventType.ACTIVATED) {
            return;
        }
        desktop = Desktop.getDesktop();
        if (desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(e.getURL().toURI());
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(this,
                        ideText.getText(About.class,
                        "About.UserDefaultBrowserNotFound"),
                        ideText.getText(this.getClass(),
                        "About.Error"),
                        JOptionPane.ERROR_MESSAGE);
            } catch (URISyntaxException e1) {
                JOptionPane.showMessageDialog(this,
                        ideText.getText(About.class,
                        "About.InvalidUrl") + ": " + e.getURL(),
                        ideText.getText(this.getClass(), "About.Error"),
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}  //  @jve:decl-index=0:visual-constraint="10,10"

