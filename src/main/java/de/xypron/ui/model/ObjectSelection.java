/**
 * Copyright 2009, Heinrich Schuchardt
 *
 * @author Heinrich Schuchardt
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package de.xypron.ui.model;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * Object selection used for drag and drop.
 */
public class ObjectSelection
        implements Transferable {

    /**
     * Dragged object.
     */
    private Object object;
    /**
     * Meta information about dragable objects.
     */
    private static final DataFlavor[] FLAVORS = {createConstant(Object.class,
        "Object"),};

    /**
     * Creates transferable capable of transferring the object.
     *
     * @param object object
     */
    public ObjectSelection(Object object) {
        this.object = object;
    }

    @Override
    public Object getTransferData(final DataFlavor flavor)
            throws UnsupportedFlavorException, IOException {
        int i;
        for (i = 0; i < FLAVORS.length; i++) {
            if (flavor.equals(FLAVORS[i])) {
                return object;
            }
        }
        throw new UnsupportedFlavorException(flavor);
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return FLAVORS;
    }

    @Override
    public boolean isDataFlavorSupported(final DataFlavor flavor) {
        int i;
        for (i = 0; i < FLAVORS.length; i++) {
            if (flavor.equals(FLAVORS[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Private initializer for flavor.
     *
     * @param rc representationclass
     * @param prn human-readable string
     * @return flavor
     */
    static private DataFlavor createConstant(final Class<?> rc,
            final String prn) {
        try {
            return new DataFlavor(rc, prn);
        } catch (Exception e) {
            return null;
        }
    }
}
