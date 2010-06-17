package flefebvre.intellij.plugin.mantisbt.browser;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.AbstractLayoutCache;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: fred
 * Date: 15 juin 2010
 * Time: 18:19:37
 * To change this template use File | Settings | File Templates.
 */
public class BasicWideNodeTreeUI extends BasicTreeUI {

    private int lastWidth;
    private boolean leftToRight;
    protected JTree tree;

    public BasicWideNodeTreeUI() {
        super();
    }

    public void installUI(JComponent c) {
        if (c == null) {
            throw new NullPointerException("null component passed to " +
                    "BasicTreeUI.installUI()");
        }
        tree = (JTree) c;
        super.installUI(c);
    }

    protected void prepareForUIInstall() {
        super.prepareForUIInstall();
        leftToRight = tree.getComponentOrientation().isLeftToRight();
        lastWidth = tree.getParent().getWidth();
    }

    protected TreeCellRenderer createDefaultCellRenderer() {
        return new DefaultTreeCellRenderer();
    }

    protected AbstractLayoutCache.NodeDimensions createNodeDimensions() {
        return new NodeDimensionsHandler();
    }

    public class NodeDimensionsHandler extends AbstractLayoutCache.NodeDimensions {
        public Rectangle getNodeDimensions(Object value, int row, int depth,
                                           boolean expanded, Rectangle size) {

            // Return size of editing component, if editing and asking
            // for editing row.
            if (editingComponent != null && editingRow == row) {
                Dimension prefSize = editingComponent.getPreferredSize();
                int rh = getRowHeight();

                if (rh > 0 && rh != prefSize.height)
                    prefSize.height = rh;
                if (size != null) {
                    size.x = getRowX(row, depth);
                    size.width = prefSize.width;
                    size.height = prefSize.height;
                } else {
                    size = new Rectangle(getRowX(row, depth), 0,
                            prefSize.width, prefSize.height);
                }

                if (!leftToRight) {
                    size.x = lastWidth - size.width - size.x - 2;
                }
                return size;
            }
            // Not editing, use renderer.
            if (currentCellRenderer != null) {
                Component aComponent;

                aComponent = currentCellRenderer.getTreeCellRendererComponent
                        (tree, value, tree.isRowSelected(row),
                                expanded, treeModel.isLeaf(value), row,
                                false);
                if (tree != null) {
                    // Only ever removed when UI changes, this is OK!
                    rendererPane.add(aComponent);
                    aComponent.validate();
                }
                Dimension prefSize = aComponent.getPreferredSize();

                if (size != null) {
                    size.x = getRowX(row, depth);
                    size.width = //prefSize.width;
                            lastWidth - size.x; // <*** the only change
                    size.height = prefSize.height;
                } else {
                    size = new Rectangle(getRowX(row, depth), 0,
                            prefSize.width, prefSize.height);
                }

                if (!leftToRight) {
                    size.x = lastWidth - size.width - size.x - 2;
                }
                return size;
            }
            return null;
        }
    }
}
