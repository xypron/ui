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
package de.xypron.util;

import javax.swing.RowFilter;
import javax.swing.RowFilter.Entry;

/**
 * Row filter for string values.
 * @author Heinrich Schuchardt
 * @param <Model> the type of the model; for example <code>PersonModel</code>
 * @param <Identifier> the type of the identifier; when using
 *            <code>TableRowSorter</code> this will be <code>Integer</code>
 */
public class StringFilter<Model, Identifier>
        extends RowFilter<Model, Identifier> {

    /**
     * Indices of columns for which the filter condition is checked.
     */
    private int[] columns;
    /**
     * Comparison operator.
     */
    private Filter.ComparisonType type;
    /**
     * Value to compare against.
     */
    private String value;

    /**
     * Constructor.
     * @param type comparison operator
     * @param value value to compare against
     * @param columns indices of the columns to cmpare against
     */
    public StringFilter(final Filter.ComparisonType type,
            final String value, final int... columns) {
        this.type = type;
        this.value = value;
        this.columns = columns;
    }

    @Override
    public boolean include(
            final Entry<? extends Model, ? extends Identifier> entry) {

        for (int i : columns) {
            String o = entry.getStringValue(i);
            int cp = o.compareTo(value);
            switch (type) {
                case LESS_THAN:
                    if (!(cp < 0)) {
                        return false;
                    }
                    break;
                case LESS_OR_EQUAL:
                    if (!(cp <= 0)) {
                        return false;
                    }
                    break;
                case EQUALS:
                    if (!(cp == 0)) {
                        return false;
                    }
                    break;
                case GREATER_OR_EQUAL:
                    if (!(cp >= 0)) {
                        return false;
                    }
                    break;
                case GREATER_THAN:
                    if (!(cp > 0)) {
                        return false;
                    }
                    break;
                case NOT_EQUALS:
                    if (!(cp != 0)) {
                        return false;
                    }
                    break;
                default:
                    return true;
            }
        }
        return true;
    }
}
