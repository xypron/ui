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

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;

/**
 * Password dialog.
 * @author Heinrich Schuchardt
 */
@SuppressWarnings("serial")
public final class PasswordDialog extends JDialog
        implements ActionListener, KeyListener {

    /**
     * OK action.
     */
    private static final String ACTION_OK = "OK";
    /**
     * Title of dialog.
     */
    private String title;
    /**
     * Label of dialog.
     */
    private String label;
    /**
     * Password.
     */
    private String password = null;
    /**
     * Password field.
     */
    private JPasswordField jPasswordField = null;
    /**
     * OK button.
     */
    private JButton jButtonOk = null;

    /**
     * Constuctor.
     * @param owner frame owning the dialog
     * @param title title of the dialog
     * @param label label explaining the password domain
     */
    private PasswordDialog(final JFrame owner, final String title, 
            final String label) {
        super(owner, true);
        this.title = title;
        this.label = label;

        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        init();
    }

    /**
     * Initialize password dialog.
     */
    private void init() {
        GridBagConstraints constraint;
        Container contentPane;
        setTitle(title);
        this.setLayout(null);

        contentPane = getContentPane();
        contentPane.setLayout(new GridBagLayout());

        constraint = new GridBagConstraints();
        constraint.ipadx = 5;
        constraint.ipady = 5;
        constraint.anchor = GridBagConstraints.CENTER;
        constraint.gridx = 0;
        constraint.gridy = 0;

        add(new JLabel(label), constraint);
        constraint.gridy++;
        add(getJPasswordField(), constraint);
        constraint.gridy++;
        add(getOkButton(), constraint);
        this.pack();
    }

    /**
     * Initialize password field.
     * @return password field
     */
    private JPasswordField getJPasswordField() {
        if (jPasswordField == null) {
            jPasswordField = new JPasswordField(40);
            jPasswordField.setActionCommand(ACTION_OK);
            jPasswordField.addKeyListener(this);
        }
        return jPasswordField;
    }

    /**
     * Get the ok button.
     * @return ok button
     */
    private JButton getOkButton() {
        if (jButtonOk == null) {
            jButtonOk = new JButton("OK");
            jButtonOk.setActionCommand(ACTION_OK);
            jButtonOk.addActionListener(this);
            jButtonOk.setEnabled(true);
        }
        return jButtonOk;
    }

    /**
     * Retrieve password from password field.
     */
    private void done() {
        password = new String(jPasswordField.getPassword());
        setVisible(false);
        dispose();
    }

    /**
     * Get password.
     * @return password.
     */
    private String getPassword() {
        return password;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {

        if (e.getActionCommand().equals(ACTION_OK)) {
            done();
        }
    }

    @Override
    public void keyTyped(final KeyEvent e) {
    }

    @Override
    public void keyPressed(final KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            done();
        }
    }

    @Override
    public void keyReleased(final KeyEvent e) {
    }

    /**
     * Show a password dialog.
     * @return password
     */
    public static String askPassword() {
        return askPassword(null, "Password", "Enter password");
    }

    /**
     * Show a password dialog.
     * @param title title
     * @param label label
     * @return password
     */
    public static String askPassword(final String title, final String label) {
        return askPassword(null, title, label);
    }

    /**
     * Show a password dialog.
     * @param owner owner frame
     * @param title title
     * @param label label
     * @return password
     */
    public static String askPassword(final JFrame owner, final String title, 
            final String label) {
        PasswordDialog dialog;

        dialog = new PasswordDialog(owner, title, label);
        dialog.setVisible(true);

        return dialog.getPassword();
    }
}
