/**
 * Copyright 2009, Heinrich Schuchardt
 *
 * @author Heinrich Schuchardt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package de.xypron.ui.components;

import de.xypron.ui.model.ObjectSelection;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * Dragable tree.
 */
@SuppressWarnings("serial")
public class IdeTree extends JTree
        implements DragGestureListener {

    /**
     * Drag source listener.
     */
    static final DragSourceListener DRAG_SOURCE_LISTENER =
            new PrivateDragSourceListener();
    /**
     * Drag source.
     */
    private DragSource dragSource;

    /**
     * Constructor.
     * @param model tree model
     */
    public IdeTree(final TreeModel model) {
        super(model);
        init();
    }

    /**
     * Initialize tree.
     */
    private void init() {
        dragSource = DragSource.getDefaultDragSource();
        dragSource.createDefaultDragGestureRecognizer(this,
                DnDConstants.ACTION_COPY_OR_MOVE, this);
    }

    @Override
    public final void dragGestureRecognized(
            final DragGestureEvent dragGestureEvent) {
        TreePath path = getSelectionPath();
        if (path != null) {
            DefaultMutableTreeNode selection =
                    (DefaultMutableTreeNode) path.getLastPathComponent();
            ObjectSelection node =
                    new ObjectSelection(selection.getUserObject());
            dragSource.startDrag(dragGestureEvent, DragSource.DefaultCopyDrop,
                    node, DRAG_SOURCE_LISTENER);
        }
    }

    /**
     * Drag source listener.
     * A DragSourceListener is required, but IdeTree does not currently
     * use this feature. Hence an empty implementation is used.
     */
    private static class PrivateDragSourceListener
            implements DragSourceListener {

        @Override
        public void dragDropEnd(
                final DragSourceDropEvent dragSourceDropEvent) {
        }

        @Override
        public void dragEnter(
                final DragSourceDragEvent dragSourceDragEvent) {
        }

        @Override
        public void dragExit(
                final DragSourceEvent dragSourceEvent) {
        }

        @Override
        public void dragOver(
                final DragSourceDragEvent dragSourceDragEvent) {
        }

        @Override
        public void dropActionChanged(
                final DragSourceDragEvent dragSourceDragEvent) {
        }
    }
}
