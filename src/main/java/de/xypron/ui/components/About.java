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

import de.xypron.util.IdeText;
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
@SuppressWarnings("serial")
public class About extends JDialog
        implements HyperlinkListener {

    /**
     * Height of the dialog box.
     */
    private static final int DIALOG_HEIGHT = 600;
    /**
     * Width of the dialog box.
     */
    private static final int DIALOG_WIDTH = 800;
    /**
     * Scroll pane.
     */
    private JScrollPane jScrollPane = null;
    /**
     * Text pane.
     */
    private JTextPane jTextPane = null;
    /**
     * URL.
     */
    private String urlString = null;

    /**
     * Construcotr.
     * @param owner the <code>Frame</code> from which the dialog is displayed
     */
    public About(final Frame owner) {
        this(owner, null);
    }

    /**
     * Constructor.
     * @param owner the <code>Frame</code> from which the dialog is displayed
     * @param urlString URL of the help text
     */
    public About(final Frame owner, final String urlString) {
        super(owner, true);
        this.urlString = urlString;
        initialize();
    }

    /**
     * This method initializes this.
     */
    private void initialize() {
        if (urlString == null) {
            urlString = IdeText.getText(this.getClass(), "About.Url");
        }
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setSize(new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT));
        this.setTitle(IdeText.getText(this.getClass(), "About.Title"));
        this.setContentPane(getJScrollPane());
        this.setVisible(true);
    }

    /**
     * This method initializes jScrollPane.
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
     * This method initializes jScrollPane.
     *
     * @return javax.swing.JScrollPane
     */
    private JTextPane getJTextPane() {
        URL url;
        ClassLoader loader;


        if (jTextPane == null) {
            jTextPane = new JTextPane();

            loader = About.class.getClassLoader();
            url = loader.getResource(urlString);
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

    @Override
    public void hyperlinkUpdate(final HyperlinkEvent e) {
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
                        IdeText.getText(About.class,
                        "About.UserDefaultBrowserNotFound"),
                        IdeText.getText(this.getClass(),
                        "About.Error"),
                        JOptionPane.ERROR_MESSAGE);
            } catch (URISyntaxException e1) {
                JOptionPane.showMessageDialog(this,
                        IdeText.getText(About.class,
                        "About.InvalidUrl") + ": " + e.getURL(),
                        IdeText.getText(this.getClass(), "About.Error"),
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
