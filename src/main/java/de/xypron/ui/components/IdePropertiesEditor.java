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

import de.xypron.ui.model.Storable;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.TreeMap;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;

/**
 * The properties editor displays a <code>Properties</code> object and
 * allows to edit it.
 */
public class IdePropertiesEditor extends IdePanel {

    private Properties backup;
    private Properties properties;
    private final String ACTIONDELETE = "DELETE";
    private final String ACTIONSAVE = "SAVE";
    private final String ACTIONUNDO = "UNDO";
    private static final long serialVersionUID = -7356954009240207787L;
    private JButton deleteButton = null;
    private JButton saveButton = null;
    private JButton undoButton = null;
    private JToolBar jToolBar = null;
    private IdeScrollPane scrollPane = null;
    private IdePanel editorPanel;

    /**
     * Constructor
     * @param properties properties
     */
    public IdePropertiesEditor(Properties properties) {
        super();
        this.properties = properties;
        backup();
        this.add(getJToolBar(), BorderLayout.PAGE_START);
        this.add(getJScrollPane(), BorderLayout.CENTER);
    }

    /**
     * Backup properties for undo
     */
    private void backup() {
        this.backup = new Properties();
        for (Entry<Object, Object> entry : properties.entrySet()) {
            backup.setProperty((String) entry.getKey(),
                    (String) entry.getValue());
        }
    }

    private IdeScrollPane getJScrollPane() {
        if (scrollPane == null) {
            scrollPane = new IdeScrollPane();
            scrollPane.setViewportView(getEditorPanel());
        }
        return scrollPane;
    }

    private IdePanel getEditorPanel() {
        GridBagConstraints constraint;
        JTextField textField;
        TreeMap<Object, Object> tree;

        if (editorPanel == null) {
            editorPanel = new IdePanel();
        }
        editorPanel.removeAll();

        editorPanel.setLayout(new GridBagLayout());
        // create constraint
        constraint = new GridBagConstraints();
        constraint.ipadx = 5;
        constraint.ipady = 5;
        constraint.gridy = 0;

        // sort properties by key
        tree = new TreeMap<Object, Object>();
        for (Entry<Object, Object> entry : properties.entrySet()) {
            tree.put(entry.getKey(), entry.getValue());
        }
        // create labels and text fields
        for (Entry<Object, Object> entry : tree.entrySet()) {
            Object key;
            Object value;
            String keyText;
            String valText;
            key = entry.getKey();
            value = entry.getValue();
            if (key instanceof String) {
                keyText = (String) key;
            } else {
                continue;
            }
            if (value instanceof String) {
                valText = (String) value;
            } else {
                continue;
            }
            constraint.gridx = 0;
            constraint.anchor = GridBagConstraints.WEST;

            editorPanel.add(new JLabel(keyText), constraint);
            constraint.gridx++;
            constraint.anchor = GridBagConstraints.WEST;
            textField = new JTextField(valText, 40);
            textField.getAccessibleContext().addPropertyChangeListener(
                    new Listener(this, keyText, textField));
            editorPanel.add(textField, constraint);
            constraint.gridy++;
        }
        return editorPanel;
    }

    private JToolBar getJToolBar() {
        if (jToolBar == null) {
            // The name is used as the title of the undocked tool bar.
            jToolBar = new JToolBar(
                    ideText.getText(this.getClass(),
                    "IdePropertiesEditor.ToolbarTitle"),
                    JToolBar.HORIZONTAL);
            addButtons();
        }
        return jToolBar;
    }

    /**
     *  Define buttons for toolbar
     */
    private void addButtons() {
        jToolBar.add(getUndoButton());

        if (properties instanceof Storable) {
            Storable obj = (Storable) properties;
            jToolBar.add(getSaveButton());
            jToolBar.add(getDeleteButton());
            getDeleteButton().setVisible(obj.exists());
        }
    }

    private JButton getDeleteButton() {
        if (deleteButton == null) {
            deleteButton = makeButton("de/xypron/ui/components/delete.png",
                    ACTIONDELETE,
                    ideText.getText(this.getClass(),
                    "IdePropertiesEditor.ToolTip.Delete"), "Delete");
        }
        return deleteButton;
    }

    private JButton getUndoButton() {
        if (undoButton == null) {
            undoButton = makeButton("de/xypron/ui/components/undo.png",
                    ACTIONUNDO,
                    ideText.getText(this.getClass(),
                    "IdePropertiesEditor.ToolTip.Undo"), "Undo");
        }
        return undoButton;
    }

    private JButton getSaveButton() {
        if (saveButton == null) {
            saveButton = makeButton("de/xypron/ui/components/save.png",
                    ACTIONSAVE,
                    ideText.getText(this.getClass(),
                    "IdePropertiesEditor.ToolTip.Save"), "Save");
        }
        return saveButton;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand;
        actionCommand = e.getActionCommand();
        if (actionCommand.equals(ACTIONDELETE)) {
            delete();
        } else if (actionCommand.equals(ACTIONSAVE)) {
            save();
        } else if (actionCommand.equals(ACTIONUNDO)) {
            undo();
        } else {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    /**
     * delete
     */
    private void delete() {
        if (properties instanceof Storable) {
            Storable obj = (Storable) properties;
            if (obj.exists()) {
                if (JOptionPane.showConfirmDialog(
                        getParent(),
                        ideText.getText("IdePropertiesEditor.DeleteFromStore"),
                        ideText.getText("IdePropertiesEditor.Confirmation"),
                        JOptionPane.OK_CANCEL_OPTION)
                        != JOptionPane.OK_OPTION) {
                    // User cancelled
                    return;
                }
            }
            if (obj.remove()) {
                getDeleteButton().setVisible(false);
            }
        }
    }

    /**
     * undo
     */
    private void undo() {
        for (Entry<Object, Object> entry : backup.entrySet()) {
            properties.setProperty((String) entry.getKey(),
                    (String) entry.getValue());
        }
        getEditorPanel();
        editorPanel.validate();
    }

    private void save() {
        if (properties instanceof Storable) {
            Storable obj = (Storable) properties;
            if (obj.exists()) {
                if (JOptionPane.showConfirmDialog(
                        getParent(),
                        ideText.getText("IdePropertiesEditor.OverwriteInStore"),
                        ideText.getText("IdePropertiesEditor.Confirmation"),
                        JOptionPane.OK_CANCEL_OPTION)
                        != JOptionPane.OK_OPTION) {
                    // User cancelled
                    return;
                }
            }
            if (obj.store()) {
                getDeleteButton().setVisible(true);
            }
            backup();
        }
    }

    private static class Listener implements PropertyChangeListener {

        IdePropertiesEditor editor;
        String key;
        JTextField textField;

        public Listener(IdePropertiesEditor editor,
                String key, JTextField textField) {
            this.editor = editor;
            this.key = key;
            this.textField = textField;
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            editor.properties.setProperty(key, textField.getText());
        }
    }
}
