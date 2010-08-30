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
 * Password dialog
 * @author Heinrich Schuchardt
 */
public class PasswordDialog extends JDialog
        implements ActionListener, KeyListener {

    private static final long serialVersionUID = -6270063889257216051L;
    private static final String OK = "OK";
    String title;
    String label;
    String password = null;
    private JPasswordField jPasswordField = null;
    private JButton jButtonOk = null;

    private PasswordDialog(JFrame owner, String title, String label) {
        super(owner, true);
        this.title = title;
        this.label = label;

        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        init();
    }

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

    private JPasswordField getJPasswordField() {
        if (jPasswordField == null) {
            jPasswordField = new JPasswordField(40);
            jPasswordField.setActionCommand(OK);
            jPasswordField.addKeyListener(this);
        }
        return jPasswordField;
    }

    /**
     * Get the ok button
     * @return ok button
     */
    private JButton getOkButton() {
        if (jButtonOk == null) {
            jButtonOk = new JButton("OK");
            jButtonOk.setActionCommand(OK);
            jButtonOk.addActionListener(this);
            jButtonOk.setEnabled(true);
        }
        return jButtonOk;
    }

    private void done() {
        password = new String(jPasswordField.getPassword());
        setVisible(false);
        dispose();
    }

    private String getPassword() {
        return password;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getActionCommand().equals(OK)) {
            done();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            done();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    /**
     * Show a password dialog
     * @return password
     */
    public static String askPassword() {
        return askPassword(null, "Password", "Enter password");
    }

    /**
     * Show a password dialog
     * @param title title
     * @param label label
     * @return password
     */
    public static String askPassword(String title, String label) {
        return askPassword(null, title, label);
    }

    /**
     * Show a password dialog
     * @param owner owner frame
     * @param title title
     * @param label label
     * @return
     */
    public static String askPassword(JFrame owner, String title, String label) {
        PasswordDialog dialog;

        dialog = new PasswordDialog(owner, title, label);
        dialog.setVisible(true);

        return dialog.getPassword();
    }
}
