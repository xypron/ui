/*
 *  Copyright 2011 Heinrich Schuchardt.
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
import de.xypron.ui.model.UserProfile;
import de.xypron.util.IconName;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Swing application.
 * @author Heinrich Schuchardt
 */
@IconName("de/xypron/ui/components/icon.png")
public class IdeApplication implements Runnable {

    /**
     * Height of the frame.
     */
    private static final int FRAME_HEIGHT = 2048;
    /**
     * Width of the frame.
     */
    private static final int FRAME_WIDTH = 2560;
    /**
     * Key used for settings tab.
     */
    public static final String TABKEY_SETTINGS = "TAB_SETTINGS";
    /**
     * String resource helper.
     */
    private IdeText ideText;
    /**
     * Tabbed pane.
     */
    private IdeTabbedPane ideTabbedPane = null;
    /**
     * Frame.
     */
    private static JFrame jFrame = null;
    /**
     * Menu bar.
     */
    protected JMenuBar jMenuBar = null;
    /**
     * File menu.
     */
    private JMenu jMenuFile = null;
    /**
     * Help menu.
     */
    protected JMenu jMenuHelp = null;
    /**
     * Menu item About.
     */
    private JMenuItem jMenuItemAbout = null;
    /**
     * Menu item Exit.
     */
    private JMenuItem jMenuItemExit = null;
    /**
     * Menu item Info.
     */
    protected JMenuItem jMenuItemInfo = null;
    /**
     * Menu item Settings.
     */
    protected JMenuItem jMenuItemSettings = null;
    /**
     * Look and feel.
     */
    protected String lookAndFeel = "Nimbus";
    /**
     * User profile.
     */
    protected UserProfile up;

    /**
     * Constructor.
     * @param args command line parameters
     */
    public IdeApplication(final String[] args) {
        parseCommandLine(args);
    }

    /**
     * Constructor.
     */
    public IdeApplication() {
        this(new String[0]);
    }

    /**
     * Exit application.
     */
    public void exit() {
        jFrame.setVisible(false);
        jFrame.dispose();
        System.exit(0);
    }

    /**
     * This method initializes tabbed pane.
     * @return tabbed pane
     */
    protected final IdeTabbedPane getIdeTabbedPane() {
        if (ideTabbedPane == null) {
            ideTabbedPane = new IdeTabbedPane();
        }
        return ideTabbedPane;
    }

    /**
     * This method initializes frame.
     *
     * @return javax.swing.JFrame
     */
    protected JFrame getJFrame() {
        ImageIcon image;
        if (jFrame == null) {
            jFrame = new JFrame(ideText.getText("IdeApplication.FrameTitle"));
            jFrame.setSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
            jFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            jFrame.setJMenuBar(getJMenuBar());
            jFrame.setContentPane(getIdeTabbedPane());
            try {
                image = IconBuffer.getIcon(this.getClass());
                jFrame.setIconImage(image.getImage());
            } catch (Exception e) {
            }
        }
        return jFrame;
    }

    /**
     * This method initializes the menu bar.
     *
     * @return javax.swing.JMenuBar
     */
    protected JMenuBar getJMenuBar() {
        if (jMenuBar == null) {
            jMenuBar = new JMenuBar();
            jMenuBar.add(getJMenuFile());
            jMenuBar.add(getJMenuHelp());
        }
        return jMenuBar;
    }

    /**
     * This method initializes the File menu.
     *
     * @return javax.swing.JMenu
     */
    protected JMenu getJMenuFile() {
        if (jMenuFile == null) {
            jMenuFile = new JMenu(ideText.getText("IdeApplication.MenuFile"));
            jMenuFile.add(getJMenuItemSettings());
            jMenuFile.addSeparator();
            jMenuFile.add(getJMenuItemExit());
        }
        return jMenuFile;
    }

    /**
     * This method initializes the Help menu.
     *
     * @return javax.swing.JMenu
     */
    protected JMenu getJMenuHelp() {
        if (jMenuHelp == null) {
            jMenuHelp = new JMenu(ideText.getText("IdeApplication.MenuHelp"));
            jMenuHelp.addSeparator();
            jMenuHelp.add(getJMenuItemInfo());
            jMenuHelp.add(getJMenuItemAbout());
        }
        return jMenuHelp;
    }

    /**
     * This method initializes menu item About.
     *
     * @return javax.swing.JMenuItem
     */
    protected JMenuItem getJMenuItemAbout() {
        if (jMenuItemAbout == null) {
            jMenuItemAbout = new JMenuItem(ideText.getText(
                    "IdeApplication.MenuItemAbout"));
            jMenuItemAbout.addActionListener(new AboutAction());
        }
        return jMenuItemAbout;
    }

    /**
     * This method initializes menu item Exit.
     *
     * @return javax.swing.JMenuItem
     */
    protected JMenuItem getJMenuItemExit() {
        if (jMenuItemExit == null) {
            jMenuItemExit = new JMenuItem(ideText.getText(
                    "IdeApplication.MenuItemExit"));
            jMenuItemExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
                    InputEvent.CTRL_MASK));
            jMenuItemExit.addActionListener(new ExitAction());
        }
        return jMenuItemExit;
    }

    /**
     * This method initializes menu Item Info.
     *
     * @return javax.swing.JMenuItem
     */
    protected JMenuItem getJMenuItemInfo() {
        if (jMenuItemInfo == null) {
            jMenuItemInfo = new JMenuItem(ideText.getText(
                    "IdeApplication.MenuItemInfo"));
            jMenuItemInfo.addActionListener(new InfoAction());
        }
        return jMenuItemInfo;
    }

    /**
     * This method initializes menu item Settings.
     *
     * @return javax.swing.JMenuItem
     */
    protected JMenuItem getJMenuItemSettings() {
        if (jMenuItemSettings == null) {
            jMenuItemSettings = new JMenuItem(ideText.getText(
                    "IdeApplication.MenuItemSettings"));
            jMenuItemSettings.addActionListener(new SettingsAction());
        }
        return jMenuItemSettings;
    }

    /**
     * Get main component.
     * @return main component
     */
    public static Component getMainComponent() {
        return jFrame;
    }

    /**
     * Gets value from resource bundle.
     * @param cls class specifying the package
     * @param str property name in the resoruce bundle
     * @return string
     */
    protected final String getText(String str) {
        return ideText.getText(this.getClass(), str);
    }
    
    /**
     * Initialize application.
     */
    protected void init() {
    }

    /**
     * Starts the application.
     *
     * @param args command line parameters
     */
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new IdeApplication(args));
    }

    /**
     * Parse command line.
     * @param args arguments passed to constructor
     */
    protected void parseCommandLine(final String[] args) {
        String parameter = "";
        final String commandLineHelp =
                "The following command line options are available:\n"
                + "-help  show this help\n"
                + "-lf n  set look and feel, default " + lookAndFeel + "\n";

        for (String arg : args) {
            if (arg.startsWith("--")) {
                parameter = arg.substring(2).toLowerCase();
            } else if (arg.startsWith("-")) {
                parameter = arg.substring(1).toLowerCase();
            } else if (parameter.equals("lf")) {
                lookAndFeel = arg;
            } else {
                System.out.println(commandLineHelp);
                System.exit(1);
            }
        }
    }

    @Override
    public final void run() {
        ideText = IdeText.getIdeText(this);
        up = new UserProfile(this.getClass());

        init();
        setLookAndFeel();
        getJFrame().setVisible(true);
        getJFrame().addWindowListener(new ApplicationWindowListener());
        getJFrame().setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    /**
     * Set look and feel.
     */
    protected void setLookAndFeel() {
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if (lookAndFeel.equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (UnsupportedLookAndFeelException e) {
            // handle exception
        } catch (ClassNotFoundException e) {
            // handle exception
        } catch (InstantiationException e) {
            // handle exception
        } catch (IllegalAccessException e) {
            // handle exception
        }
    }

    /**
     * Listener for menu item "About".
     */
    @SuppressWarnings("serial")
    private class AboutAction extends AbstractAction {

        @Override
        public void actionPerformed(final ActionEvent arg0) {
            new About(jFrame, ideText.getText("About.Url"));
        }
    }

    /**
     * WindowListener to react upon closing of the JFrame.
     */
    private class ApplicationWindowListener implements WindowListener {

        @Override
        public void windowActivated(final WindowEvent e) {
        }

        @Override
        public void windowClosed(final WindowEvent e) {
        }

        @Override
        public void windowClosing(final WindowEvent e) {
            exit();
        }

        @Override
        public void windowDeactivated(final WindowEvent e) {
        }

        @Override
        public void windowDeiconified(final WindowEvent e) {
        }

        @Override
        public void windowIconified(final WindowEvent e) {
        }

        @Override
        public void windowOpened(final WindowEvent e) {
        }
    }

    /**
     * Listener for menu item "Exit".
     */
    @SuppressWarnings("serial")
    private class ExitAction extends AbstractAction {

        @Override
        public void actionPerformed(final ActionEvent arg0) {
            exit();
        }
    }

    /**
     * Listener for menu item "Info".
     */
    @SuppressWarnings("serial")
    private class InfoAction extends AbstractAction {

        @Override
        public void actionPerformed(final ActionEvent arg0) {
            JOptionPane.showMessageDialog(jFrame,
                    SystemInfo.info(),
                    ideText.getText("IdeApplication.MenuItemInfo"),
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Listener for menu item "Settings".
     */
    @SuppressWarnings("serial")
    private class SettingsAction extends AbstractAction {

        @Override
        public void actionPerformed(final ActionEvent arg0) {
            ideTabbedPane.setComponent(TABKEY_SETTINGS,
                    new IdePropertiesEditor(up),
                    ideText.getText("IdeApplication.MenuItemSettings"),
                    IconBuffer.getIcon(IdePropertiesEditor.class),
                    ideText.getText("IdeApplication.MenuItemSettings"),
                    true);
            ideTabbedPane.validate();
        }
    }
}
