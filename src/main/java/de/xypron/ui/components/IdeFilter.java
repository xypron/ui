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
import de.xypron.util.Filter;
import de.xypron.util.FilterInfo;
import de.xypron.util.AbstractResultTableModel;
import de.xypron.util.StringFilter;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.util.LinkedList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.RowFilter.ComparisonType;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 * Dialog for line filter.
 *
 * @author Heinrich Schuchardt
 */
@SuppressWarnings("serial")
public class IdeFilter extends JDialog {

    /**
     * Table.
     */
    private JTable jTable;
    /**
     * Scrool pane.
     */
    private IdeScrollPane scrollPane = null;
    /**
     * Editor panel.
     */
    private IdePanel editorPanel;
    /**
     * Parent frame.
     */
    private Frame frame;

    /**
     * Constuctor.
     *
     * @param owner frame owning the dialog
     * @param jTable table to filter
     */
    public IdeFilter(final Frame owner, final JTable jTable) {
        super(owner, true);
        frame = owner;
        this.jTable = jTable;
        initialize();
    }

    /**
     * Initialize filter.
     */
    private void initialize() {
        this.setTitle(IdeText.getText(IdeFilter.class, "IdeFilter.Filter"));
        this.add(getJScrollPane(), BorderLayout.CENTER);
        this.addWindowListener(new MyWindowAdapter());
        pack();
        setResizable(false);
    }

    /**
     * Initialize scroll pane.
     *
     * @return scroll pane.
     */
    private IdeScrollPane getJScrollPane() {
        if (scrollPane == null) {
            scrollPane = new IdeScrollPane();
            scrollPane.setViewportView(getEditorPanel());
        }
        return scrollPane;
    }

    /**
     * Initialize editor panel.
     *
     * @return editor panel
     */
    private IdePanel getEditorPanel() {
        GridBagConstraints constraint;
        JTextField textField;
        AbstractResultTableModel model;

        if (editorPanel == null) {
            editorPanel = new IdePanel(frame);
        }
        editorPanel.removeAll();

        editorPanel.setLayout(new GridBagLayout());
        // create constraint
        constraint = new GridBagConstraints();
        constraint.ipadx = 5;
        constraint.ipady = 5;
        constraint.gridy = 0;

        model = (AbstractResultTableModel) jTable.getModel();
        for (FilterInfo filterInfo : model.getFilterInfos()) {
            constraint.gridx = 0;
            constraint.anchor = GridBagConstraints.WEST;
            editorPanel.add(
                    new JLabel(model.getColumnName(filterInfo.getColumn())),
                    constraint);
            constraint.gridx++;
            constraint.anchor = GridBagConstraints.WEST;
            editorPanel.add(new ComparisonComboBox(filterInfo), constraint);
            constraint.gridx++;
            constraint.anchor = GridBagConstraints.WEST;
            textField = new TextField(filterInfo.getValue(), 40, filterInfo);
            editorPanel.add(textField, constraint);
            constraint.gridy++;
        }
        return editorPanel;
    }

    /**
     * WindowsAdapter to react upon closing of the JFrame.
     */
    private class MyWindowAdapter extends WindowAdapter {

        @Override
        public void windowClosing(final WindowEvent e) {
            exit();
        }
    }

    /**
     * Update filter when leaving dialog.
     */
    private void exit() {
        AbstractResultTableModel model
                = (AbstractResultTableModel) jTable.getModel();
        LinkedList<RowFilter<? super TableModel, ? super Integer>> filters;
        TableRowSorter<? extends TableModel> rowSorter;

        try {
            rowSorter = (TableRowSorter<? extends TableModel>) jTable
                    .getRowSorter();
        }
        catch (ClassCastException e) {
            return;
        }
        filters = new LinkedList<RowFilter<? super TableModel, ? super Integer>>();

        for (FilterInfo filterInfo : model.getFilterInfos()) {
            LinkedList<RowFilter<? super TableModel, ? super Integer>> orFilters;
            orFilters = new LinkedList<RowFilter<? super TableModel, ? super Integer>>();
            int column = filterInfo.getColumn();

            if (filterInfo.isNumeric()) {
                Number value;
                try {
                    value = new Double(filterInfo.getValue());
                }
                catch (NumberFormatException e) {
                    continue;
                }
                switch (filterInfo.getComparisonType()) {
                    case LESS_OR_EQUAL:
                    case EQUALS:
                    case GREATER_OR_EQUAL:
                        orFilters.add(RowFilter.numberFilter(
                                ComparisonType.EQUAL,
                                value,
                                column));
                        break;
                    default:
                        break;
                }
                switch (filterInfo.getComparisonType()) {
                    case LESS_THAN:
                    case LESS_OR_EQUAL:
                        orFilters.add(RowFilter.numberFilter(
                                ComparisonType.BEFORE,
                                value,
                                filterInfo.getColumn()));
                        break;
                    case GREATER_OR_EQUAL:
                    case GREATER_THAN:
                        orFilters.add(RowFilter.numberFilter(
                                ComparisonType.AFTER,
                                value,
                                filterInfo.getColumn()));
                        break;
                    case NOT_EQUALS:
                        orFilters.add(RowFilter.numberFilter(
                                ComparisonType.NOT_EQUAL,
                                value,
                                filterInfo.getColumn()));
                        break;
                    default:
                        break;
                }
                if (orFilters.size() > 0) {
                    filters.add(RowFilter.orFilter(orFilters));
                }
            } else {
                String value;
                value = filterInfo.getValue();
                filters.add(new StringFilter<Object, Object>(
                        filterInfo.getComparisonType(),
                        value,
                        column));
            }
        }
        rowSorter.setRowFilter(RowFilter.andFilter(filters));
    }

    /**
     * Input field for filter.
     */
    private class TextField extends JTextField {

        /**
         * Serial version unique ID.
         */
        private static final long serialVersionUID = 5237275173528788213L;
        /**
         * Filter information.
         */
        private FilterInfo filterInfo;

        /**
         * Constructor.
         *
         * @param text text to be displayed
         * @param columns width of text field
         * @param filterInfo filter information
         */
        public TextField(final String text,
                final int columns, final FilterInfo filterInfo) {
            super(text, columns);
            this.filterInfo = filterInfo;
            initialize();
        }

        /**
         * Initialize filter.
         */
        private void initialize() {
            getDocument().addDocumentListener(new DocumentListener() {

                @Override
                public void insertUpdate(final DocumentEvent e) {
                    filterInfo.setValue(getText());
                }

                @Override
                public void removeUpdate(final DocumentEvent e) {
                    filterInfo.setValue(getText());
                }

                @Override
                public void changedUpdate(final DocumentEvent e) {
                    filterInfo.setValue(getText());
                }
            });
        }
    }

    /**
     * Combo box to choose comparison operator.
     */
    @SuppressWarnings("serial")
    private class ComparisonComboBox extends JComboBox<String> {

        /**
         * No selection, ignore.
         */
        private static final int SELECTED_NONE = 0;
        /**
         * Less than.
         */
        private static final int SELECTED_LT = 1;
        /**
         * Less or equal.
         */
        private static final int SELECTED_LE = 2;
        /**
         * Equal.
         */
        private static final int SELECTED_EQ = 3;
        /**
         * Greater or equal.
         */
        private static final int SELECTED_GE = 4;
        /**
         * Greater then.
         */
        private static final int SELECTED_GT = 5;
        /**
         * Not equal.
         */
        private static final int SELECTED_NE = 6;
        /**
         * Filter information.
         */
        FilterInfo filterInfo;
        /**
         * Available operators.
         */
        private String[] operators = {
            "",
            "<",
            "<=",
            "=",
            ">=",
            ">",
            "!="
        };

        /**
         * Constuctor.
         *
         * @param filterInfo filter information
         */
        public ComparisonComboBox(FilterInfo filterInfo) {
            super();
            this.filterInfo = filterInfo;
            initialize();
        }

        /**
         * Initialize combo box.
         */
        private void initialize() {
            setModel(new DefaultComboBoxModel<String>(operators));
            this.setSelectedItem(filterInfo.getComparisonType());
            this.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    filterInfo.setComparisonType(getComparisonType());
                }
            });
        }

        /**
         * Set selected item.
         *
         * @param type type of comparison operator.
         */
        private void setSelectedItem(Filter.ComparisonType type) {
            switch (type) {
                case LESS_THAN:
                    setSelectedIndex(SELECTED_LT);
                    break;
                case LESS_OR_EQUAL:
                    setSelectedIndex(SELECTED_LE);
                    break;
                case EQUALS:
                    setSelectedIndex(SELECTED_EQ);
                    break;
                case GREATER_OR_EQUAL:
                    setSelectedIndex(SELECTED_GE);
                    break;
                case GREATER_THAN:
                    setSelectedIndex(SELECTED_GT);
                    break;
                case NOT_EQUALS:
                    setSelectedIndex(SELECTED_NE);
                    break;
                default:
                    setSelectedIndex(SELECTED_NONE);
                    break;
            }
        }

        /**
         * Get type of comparison operator.
         *
         * @return type of comparison operator
         */
        public Filter.ComparisonType getComparisonType() {
            switch (getSelectedIndex()) {
                case SELECTED_LT:
                    return Filter.ComparisonType.LESS_THAN;
                case SELECTED_LE:
                    return Filter.ComparisonType.LESS_OR_EQUAL;
                case SELECTED_EQ:
                    return Filter.ComparisonType.EQUALS;
                case SELECTED_GE:
                    return Filter.ComparisonType.GREATER_OR_EQUAL;
                case SELECTED_GT:
                    return Filter.ComparisonType.GREATER_THAN;
                case SELECTED_NE:
                    return Filter.ComparisonType.NOT_EQUALS;
                default:
                    return Filter.ComparisonType.IGNORE;
            }
        }
    }
}
