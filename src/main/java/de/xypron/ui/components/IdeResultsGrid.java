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

import de.xypron.util.IconName;
import de.xypron.util.AbstractResultTableModel;
import de.xypron.util.IdeText;
import de.xypron.util.Mhtml;
import de.xypron.util.MhtmlFileFilter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.print.PrinterException;
import java.io.File;
import java.util.LinkedList;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;

/**
 * The result grids displays a <code>JTable</code> object and allows to filter,
 * export, and print it.
 *
 * @author Heinrich Schuchardt
 */
@SuppressWarnings("serial")
@IconName("de/xypron/ui/components/result.png")
public class IdeResultsGrid extends IdePanel {

    /**
     * Foreground color.
     */
    private static final Color COLOR_FOREGROUND = new Color(0x000000);
    /**
     * Background color for even rows.
     */
    private static final Color COLOR_BACKGROUND_EVEN = new Color(0xCCCCFF);
    /**
     * Background color for odd rows.
     */
    private static final Color COLOR_BACKGROUND_ODD = new Color(0xFFFFFF);
    /**
     * Border color for cells with focus.
     */
    private static final Color COLOR_FOCUS_BORDER = new Color(0xFFFFFF);
    /**
     * Print action.
     */
    private static final String ACTIONPRINT = "PRINT";
    /**
     * Export to mime html action.
     */
    private static final String ACTIONMHTML = "MHTML";
    /**
     * Scroll pane.
     */
    private IdeScrollPane scrollPane = null;
    /**
     * Table.
     */
    private JTable jTable;
    /**
     * Tool bar.
     */
    private JToolBar jToolBar;
    /**
     * Print button.
     */
    private JButton printButton;
    /**
     * Export to mime html button.
     */
    private JButton mhtmlButton;
    /**
     * Table model.
     */
    private AbstractResultTableModel tableModel;

    /**
     * Constructor for result grid.
     *
     * @param frame main frame
     */
    public IdeResultsGrid(final Frame frame) {
        this(frame, null);
    }

    /**
     * Constructor for result grid.
     *
     * @param frame main frame
     * @param tableModel table model
     */
    public IdeResultsGrid(final Frame frame,
            AbstractResultTableModel tableModel) {
        super(frame);
        this.tableModel = tableModel;
        this.add(getJToolBar(), BorderLayout.PAGE_START);
        this.add(getJScrollPane(), BorderLayout.CENTER);
    }

    /**
     * Gets tool bar.
     *
     * @return tool bar
     */
    protected JToolBar getJToolBar() {
        if (jToolBar == null) {
            // The name is used as the title of the undocked tool bar.
            jToolBar = new JToolBar(
                    getText("IdeResultsGrid.ToolbarTitle"),
                    JToolBar.HORIZONTAL);
            addButtons();
        }
        return jToolBar;
    }

    /**
     * Defines buttons for toolbar.
     */
    private void addButtons() {
        jToolBar.add(getPrintButton());
        jToolBar.add(getMhtmlButton());
    }

    /**
     * Gets print button.
     *
     * @return print button
     */
    private JButton getPrintButton() {
        if (printButton == null) {
            printButton = makeButton("de/xypron/ui/components/print.png",
                    ACTIONPRINT,
                    getText("IdeResultsGrid.Print"),
                    getText("IdeResultsGrid.Print"));
        }
        return printButton;
    }

    /**
     * Gets mhtml button.
     *
     * @return mhtml button
     */
    private JButton getMhtmlButton() {
        if (mhtmlButton == null) {
            mhtmlButton = makeButton("de/xypron/ui/components/export.png",
                    ACTIONMHTML,
                    getText("IdeResultsGrid.Mhtml"),
                    getText("IdeResultsGrid.Mhtml"));
        }
        return mhtmlButton;
    }

    /**
     * Get scroll pane.
     *
     * @return scroll pane
     */
    private IdeScrollPane getJScrollPane() {
        if (scrollPane == null) {
            scrollPane = new IdeScrollPane();
            scrollPane.setViewportView(getJTable());
        }
        return scrollPane;
    }

    /**
     * Gets JTable.
     *
     * @TODO implement custom column header renderer (multi line, filter icon)
     *
     * @return JTable
     */
    protected JTable getJTable() {
        if (jTable == null) {
            MouseListener popupListener = new PopupListener();
            jTable = new JTable(tableModel);
            jTable.setRowSorter(new TableRowSorter<AbstractResultTableModel>(
                    tableModel));
            jTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            jTable.setDefaultRenderer(Object.class,
                    new CustomTableCellRenderer());
            jTable.setRowHeight(Math.max(jTable.getRowHeight(), 50));
            jTable.getTableHeader().addMouseListener(popupListener);
        }
        return jTable;
    }

    /**
     * Gets table model.
     *
     * @return table model
     */
    public AbstractResultTableModel getTableModel() {
        return tableModel;
    }

    /**
     * Sets table model.
     *
     * @param tableModel table model
     */
    public void setTableModel(final AbstractResultTableModel tableModel) {
        this.tableModel = tableModel;
        getJTable().setModel(tableModel);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        String actionCommand;
        actionCommand = e.getActionCommand();
        if (actionCommand.equals(ACTIONPRINT)) {
            try {
                jTable.print();
            }
            catch (PrinterException ex) {
            }
        } else if (actionCommand.equals(ACTIONMHTML)) {
            saveAsMhtml();
        } else {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public class CustomTableCellRenderer
            extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(final JTable table,
                final Object value, final boolean isSelected,
                final boolean hasFocus, final int row, final int column) {

            Color background;
            Color foreground;
            if (isSelected) {
                foreground = jTable.getSelectionForeground();
                background = jTable.getSelectionBackground();
            } else if (row % 2 == 0) {
                foreground = COLOR_FOREGROUND;
                background = COLOR_BACKGROUND_EVEN;
            } else {
                foreground = COLOR_FOREGROUND;
                background = COLOR_BACKGROUND_ODD;
            }
            if (value instanceof JComponent) {
                JComponent component = (JComponent) value;
                component.setForeground(foreground);
                component.setBackground(background);
                if (hasFocus) {
                    component.setBorder(new LineBorder(COLOR_FOCUS_BORDER, 1));
                } else {
                    component.setBorder(noFocusBorder);
                }
                return component;
            } else {
                super.setBackground(background);
                return super.getTableCellRendererComponent(table, value,
                        isSelected, hasFocus, row, column);
            }
        }
    }

    class PopupListener implements MouseListener {

        @Override
        public void mousePressed(final MouseEvent e) {
            showPopup(e);
        }

        @Override
        public void mouseReleased(final MouseEvent e) {
            showPopup(e);
        }

        private void showPopup(final MouseEvent e) {
            if (e.isPopupTrigger()) {
                filter();
            }
        }

        @Override
        public void mouseClicked(final MouseEvent e) {
        }

        @Override
        public void mouseEntered(final MouseEvent e) {
        }

        @Override
        public void mouseExited(final MouseEvent e) {
        }
    }

    private void filter() {
        IdeFilter frame;

        frame = new IdeFilter(getParentFrame(), jTable);
        frame.setVisible(true);
        frame.validate();

    }

    private void saveAsMhtml() {
        File file;
        final JFileChooser jFileChooser;
        int ret;
        jFileChooser = new JFileChooser();
        jFileChooser.addChoosableFileFilter(MhtmlFileFilter.getFileFilter());

        do {
            ret = jFileChooser.showSaveDialog(this);
            if (ret == JFileChooser.APPROVE_OPTION) {
                file = jFileChooser.getSelectedFile();
                if (!file.exists()
                        && jFileChooser.getFileFilter() instanceof MhtmlFileFilter
                        && !file.getName().contains(".")) {
                    file = new File(file.getAbsolutePath() + ".mhtml");
                }
                if (file.exists()) {
                    // File exists already
                    switch (JOptionPane.showConfirmDialog(
                            this, IdeText.getText(this.getClass(),
                                    "IdeResultsGrid.ReplaceExistingFile"))) {
                        case JOptionPane.NO_OPTION:
                            // User does not want to overwrite
                            continue;
                        case JOptionPane.CANCEL_OPTION:
                            // User cancelled
                            return;
                    }
                }
                // Save file
                saveMhtml(file);
                return;
            } else {
                // User cancelled
                return;
            }

        } while (true);
    }

    private void saveMhtml(File file) {
        Mhtml mhtml;
        int n;
        n = jTable.getColumnCount();
        LinkedList<LinkedList<Object>> lines;
        LinkedList<Object> line;

        IdeApplication.getMainComponent().setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.WAIT_CURSOR));
        lines = new LinkedList<LinkedList<Object>>();
        line = new LinkedList<Object>();
        for (int j = 0; j < n; j++) {
            line.add(jTable.getColumnName(j));
        }
        lines.add(line);
        for (int i = 0; i < jTable.getRowCount(); i++) {
            line = new LinkedList<Object>();
            for (int j = 0; j < n; j++) {
                line.add(jTable.getValueAt(i, j));
            }
            lines.add(line);
        }

        mhtml = new Mhtml();
        mhtml.addSheet("Table 1", lines);
        try {
            mhtml.write(file.getCanonicalPath());
        }
        catch (Throwable ex) {
            IdeText.errorMessage(this.getClass(), ex.getMessage());
        }
        IdeApplication.getMainComponent().setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.DEFAULT_CURSOR));
    }
}
