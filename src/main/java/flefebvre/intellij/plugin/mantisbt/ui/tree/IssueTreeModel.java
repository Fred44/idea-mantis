package flefebvre.intellij.plugin.mantisbt.ui.tree;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

/**
 * Created by IntelliJ IDEA.
 * User: fred
 * Date: 7 juin 2010
 * Time: 20:54:35
 * To change this template use File | Settings | File Templates.
 */
public class IssueTreeModel extends DefaultTreeModel {

    public IssueTreeModel(TreeNode treeNode) {
        super(treeNode);
    }
}
