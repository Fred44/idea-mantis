package fred.intellij.plugin.mantisbt.ui;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;

/**
 * Created by IntelliJ IDEA.
 * User: fred
 * Date: 29 mars 2010
 * Time: 22:01:48
 * To change this template use File | Settings | File Templates.
 */
public abstract class MantisAbstractTreeNode extends DefaultMutableTreeNode {

    public abstract TreeCellRenderer getRenderer();
}