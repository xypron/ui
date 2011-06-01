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
package de.xypron.util;

import java.util.TreeSet;
import javax.swing.table.AbstractTableModel;

/**
 * The abstract result table model is used for holding the data of the
 * <code>IdeResultGrid</code>.
 * @author Heinrich Schuchardt
 */
    public abstract class AbstractResultTableModel extends AbstractTableModel {

    /**
     * Row filters.
     */
    private TreeSet<FilterInfo> filterInfos = new TreeSet<FilterInfo>();

    /**
     * Get information on column filters
     * @return filter information
     */
    public final TreeSet<FilterInfo> getFilterInfos() {
        return filterInfos;
    }

    /**
     *  Returns class of objects in column <code>columnIndex</code>.
     *
     *  @param columnIndex  the column being queried
     *  @return the class
     */
    @Override
    public final Class<?> getColumnClass(int columnIndex) {
        Object obj;
        try {
            obj = this.getValueAt(0, columnIndex);
        } catch (Exception ex) {
            return Object.class;
        }
        if (obj != null) {
            return obj.getClass();
        } else {
            return Object.class;
        }
    }
}
