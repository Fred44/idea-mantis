package flefebvre.intellij.plugin.mantisbt.ui;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

/**
 * Created by IntelliJ IDEA.
 * User: flefebvre
 * Date: 22 févr. 2010
 * Time: 22:44:19
 * To change this template use File | Settings | File Templates.
 */
public class IssueTreeData extends DefaultTreeModel {


    public IssueTreeData(TreeNode treeNode) {
        super(treeNode, false);
    }
}