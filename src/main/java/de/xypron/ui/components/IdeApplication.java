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
 *
 * @author Heinrich Schuchardt
 */
public class IdeApplication implements Runnable {

    /**
     * key used for settings tab
     */
    protected static String TABKEY_SETTINGS = "SETTINGS";
    protected IdeText ideText;
    protected IdeTabbedPane ideTabbedPane = null;
    protected static JFrame jFrame = null;
    protected JMenuBar jMenuBar = null;
    protected JMenu jMenuFile = null;
    protected JMenu jMenuHelp = null;
    protected JMenuItem jMenuItemAbout = null;
    protected JMenuItem jMenuItemExit = null;
    protected JMenuItem jMenuItemInfo = null;
    protected JMenuItem jMenuItemSettings = null;
    protected String lookAndFeel = "Nimbus";
    protected UserProfile up;

    /**
     * Constructor.
     * @param args command line parameters
     */
    public IdeApplication(String[] args) {
        parseCommandLine(args);
    }

    /**
     * Constructor.
     */
    public IdeApplication() {
        this(new String[0]);
    }
    
    /**
     * Exit IDE
     */
    public void exit() {
        jFrame.setVisible(false);
        jFrame.dispose();
        System.exit(0);
    }

    /**
     * This method initializes ideTabbedPane
     * @return tabbed pane
     */
    protected IdeTabbedPane getIdeTabbedPane() {
        if (ideTabbedPane == null) {
            ideTabbedPane = new IdeTabbedPane();
        }
        return ideTabbedPane;
    }

    /**
     * This method initializes jFrame
     *
     * @return javax.swing.JFrame
     */
    protected JFrame getJFrame() {
        ImageIcon image;
        ClassLoader loader;
        if (jFrame == null) {
            jFrame = new JFrame(ideText.getText("IdeApplication.FrameTitle"));
            jFrame.setSize(new Dimension(2560, 2048));
            jFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            jFrame.setJMenuBar(getJMenuBar());
            jFrame.setContentPane(getIdeTabbedPane());
            try {
                loader = this.getClass().getClassLoader();
                image = new ImageIcon(loader.getResource(
                        ideText.getText("IdeApplication.FrameIcon")));
                jFrame.setIconImage(image.getImage());
            } catch (Exception e) {
            }
        }
        return jFrame;
    }

    /**
     * This method initializes jJMenuBar
     *
     * @return javax.swing.JMenuBar
     */
    private JMenuBar getJMenuBar() {
        if (jMenuBar == null) {
            jMenuBar = new JMenuBar();
            jMenuBar.add(getJMenuFile());
            jMenuBar.add(getJMenuHelp());
        }
        return jMenuBar;
    }

    /**
     * This method initializes jMenuFile
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
     * This method initializes jMenuHelp
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
     * This method initializes jMenuItemAbout
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
     * This method initializes jMenuItemExit
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
     * This method initializes jMenuItemInfo
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
     * This method initializes jMenuItemSettings
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
     * Get main component
     * @return main component
     */
    public static Component getMainComponent() {
        return jFrame;
    }

    /**
     * Initialize application
     */
    protected void init() {
    }

    /**
     * Starts the IDE
     *
     * @param args command line parameters
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new IdeApplication(args));
    }

    /**
     * Parse command line
     * @param args arguments passed to constructor
     */
    protected void parseCommandLine(String[] args) {
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
    public void run() {
        ideText = IdeText.getIdeText(this);
        up = new UserProfile(this.getClass());

        init();
        setLookAndFeel();
        getJFrame().setVisible(true);
        getJFrame().addWindowListener(new ApplicationWindowListener());
        getJFrame().setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    /**
     * Set look and feel
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
    private class AboutAction extends AbstractAction {

        private static final long serialVersionUID = -4008890923968961894L;

        @Override
        public void actionPerformed(ActionEvent arg0) {
            new About(jFrame, ideText.getText("About.Url"));
        }
    }

    /**
     * WindowListener to react upon closing of the JFrame
     */
    private class ApplicationWindowListener implements WindowListener {

        @Override
        public void windowActivated(WindowEvent e) {
        }

        @Override
        public void windowClosed(WindowEvent e) {
        }

        @Override
        public void windowClosing(WindowEvent e) {
            exit();
        }

        @Override
        public void windowDeactivated(WindowEvent e) {
        }

        @Override
        public void windowDeiconified(WindowEvent e) {
        }

        @Override
        public void windowIconified(WindowEvent e) {
        }

        @Override
        public void windowOpened(WindowEvent e) {
        }
    }

    /**
     * Listener for menu item "Exit".
     */
    private class ExitAction extends AbstractAction {

        private static final long serialVersionUID = -3228220219179387819L;

        @Override
        public void actionPerformed(ActionEvent arg0) {
            exit();
        }
    }

    /**
     * Listener for menu item "Info".
     */
    private class InfoAction extends AbstractAction {

        private static final long serialVersionUID = -1485719900328993760L;

        @Override
        public void actionPerformed(ActionEvent arg0) {
            JOptionPane.showMessageDialog(jFrame,
                    SystemInfo.info(),
                    ideText.getText("IdeApplication.MenuItemInfo"),
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Listener for menu item "Settings".
     */
    private class SettingsAction extends AbstractAction {

        private static final long serialVersionUID = 7326124121439143329L;

        @Override
        public void actionPerformed(ActionEvent arg0) {
            ideTabbedPane.setComponent(TABKEY_SETTINGS,
                    new IdePropertiesEditor(up),
                    ideText.getText("IdeApplication.MenuItemSettings"),
                    IconBuffer.getIcon(UserProfile.ICONNAME),
                    ideText.getText("IdeApplication.MenuItemSettings"),
                    true);
            ideTabbedPane.validate();
        }
    }
}
