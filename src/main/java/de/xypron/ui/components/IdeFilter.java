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
 *
 * @author Heinrich Schuchardt
 */
public class IdeFilter extends JDialog {

    IdeText ideText;
    JTable jTable;
    private IdeScrollPane scrollPane = null;
    private IdePanel editorPanel;

    public IdeFilter(Frame owner, JTable jTable) {
        super(owner, true);
        this.jTable = jTable;
        this.initialize();
    }

    private void initialize() {
        ideText = IdeText.getIdeText();
        this.setTitle(ideText.getText(IdeFilter.class, "IdeFilter.Filter"));
        this.add(getJScrollPane(), BorderLayout.CENTER);
        this.addWindowListener(new MyWindowAdapter());
        pack();
        setResizable(false);
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
        AbstractResultTableModel model;

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
     * WindowsAdapter to react upon closing of the JFrame
     */
    private class MyWindowAdapter extends WindowAdapter {

        @Override
        public void windowClosing(WindowEvent e) {
            exit();
        }
    }

    /**
     * Update filter when leaving dialog
     */
    private void exit() {
        AbstractResultTableModel model = (AbstractResultTableModel) jTable.
                getModel();
        LinkedList<RowFilter<? super TableModel, ? super Integer>> filters;
        TableRowSorter rowSorter;

        if ((jTable.getRowSorter() instanceof TableRowSorter)) {
            filters =
                    new LinkedList<RowFilter<? super TableModel, ? super Integer>>();

            for (FilterInfo filterInfo : model.getFilterInfos()) {
                LinkedList<RowFilter<? super TableModel, ? super Integer>> orFilters;
                orFilters =
                        new LinkedList<RowFilter<? super TableModel, ? super Integer>>();
                int column = filterInfo.getColumn();

                if (filterInfo.isNumeric()) {
                    Number value;
                    try {
                        value = new Double(filterInfo.getValue());
                    } catch (NumberFormatException e) {
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
                    }
                    if (orFilters.size() > 0) {
                        filters.add(RowFilter.orFilter(orFilters));
                    }
                } else {
                    String value;
                    value = filterInfo.getValue();
                    filters.add(new StringFilter(
                            filterInfo.getComparisonType(),
                            value,
                            column));
                }
            }

            rowSorter = (TableRowSorter) jTable.getRowSorter();
            rowSorter.setRowFilter(RowFilter.andFilter(filters));
            System.out.println(jTable.getRowSorter().getClass().getName());
        }

    }

    private class TextField extends JTextField {

        FilterInfo filterInfo;

        public TextField(String text, int columns, FilterInfo filterInfo) {
            super(text, columns);
            this.filterInfo = filterInfo;
            initialize();
        }

        private void initialize() {
            getDocument().addDocumentListener(new DocumentListener() {

                @Override
                public void insertUpdate(DocumentEvent e) {
                    filterInfo.setValue(getText());
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    filterInfo.setValue(getText());
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    filterInfo.setValue(getText());
                }
            });
        }
    }

    private class ComparisonComboBox extends JComboBox {

        FilterInfo filterInfo;
        private String[] operators = {
            "",
            "<",
            "<=",
            "=",
            ">=",
            ">",
            "!="
        };

        public ComparisonComboBox(FilterInfo filterInfo) {
            super();
            this.filterInfo = filterInfo;
            initialize();
        }

        private void initialize() {
            setModel(new DefaultComboBoxModel(operators));
            this.setSelectedItem(filterInfo.getComparisonType());
            this.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    filterInfo.setComparisonType(getComparisonType());
                }
            });
        }

        private void setSelectedItem(Filter.ComparisonType type) {
            switch (type) {
                case LESS_THAN:
                    setSelectedIndex(1);
                    break;
                case LESS_OR_EQUAL:
                    setSelectedIndex(2);
                    break;
                case EQUALS:
                    setSelectedIndex(3);
                    break;
                case GREATER_OR_EQUAL:
                    setSelectedIndex(4);
                    break;
                case GREATER_THAN:
                    setSelectedIndex(5);
                    break;
                case NOT_EQUALS:
                    setSelectedIndex(6);
                    break;
                default:
                    setSelectedIndex(0);
                    break;
            }
        }

        public Filter.ComparisonType getComparisonType() {
            switch (getSelectedIndex()) {
                case 1:
                    return Filter.ComparisonType.LESS_THAN;
                case 2:
                    return Filter.ComparisonType.LESS_OR_EQUAL;
                case 3:
                    return Filter.ComparisonType.EQUALS;
                case 4:
                    return Filter.ComparisonType.GREATER_OR_EQUAL;
                case 5:
                    return Filter.ComparisonType.GREATER_THAN;
                case 6:
                    return Filter.ComparisonType.NOT_EQUALS;
                default:
                    return Filter.ComparisonType.IGNORE;
            }
        }
    }
}
