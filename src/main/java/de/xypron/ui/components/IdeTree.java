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
import java.awt.dnd.*;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
 
/**
 * Dragable tree
 */
public class IdeTree extends JTree
        implements DragGestureListener {
	private static final long serialVersionUID = 8973770068707791644L;
	DragSource dragSource = DragSource.getDefaultDragSource();
    final static DragSourceListener dragSourceListener = new MyDragSourceListener();
 
    /**
     * @param model
     */
    public IdeTree (TreeModel model) {
        super(model);
        dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY_OR_MOVE,
                this);
    }
 
    /* (non-Javadoc)
     * @see java.awt.dnd.DragGestureListener#dragGestureRecognized(java.awt.dnd.DragGestureEvent)
     */
    @Override
    public void dragGestureRecognized (DragGestureEvent dragGestureEvent) {
        TreePath path = getSelectionPath();
        if (path != null) {
            DefaultMutableTreeNode selection = 
            	(DefaultMutableTreeNode)path.getLastPathComponent();
            ObjectSelection node = new ObjectSelection(selection.getUserObject());
            dragSource.startDrag(dragGestureEvent, DragSource.DefaultCopyDrop,
                    node, dragSourceListener);
        }
    }

    /**
     * A DragSourceListener is required, but IdeTree does not currently
     * use this feature so an empty implementation is used.
     */
    static class MyDragSourceListener
            implements DragSourceListener {
 
        @Override
        public void dragDropEnd (DragSourceDropEvent DragSourceDropEvent) {}
 
        @Override
        public void dragEnter (DragSourceDragEvent DragSourceDragEvent) {}
 
        @Override
        public void dragExit (DragSourceEvent DragSourceEvent) {}
 
        @Override
        public void dragOver (DragSourceDragEvent DragSourceDragEvent) {}
 
        @Override
        public void dropActionChanged (DragSourceDragEvent DragSourceDragEvent) {}
    }
}
