package flefebvre.intellij.plugin.mantisbt.ui.tree;

import flefebvre.intellij.plugin.mantisbt.model.IssueListModel;
import flefebvre.intellij.plugin.mantisbt.model.MantisGroupBy;
import org.mantisbt.connect.model.IIssueHeader;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Created by IntelliJ IDEA.
 * User: flefebvre
 * Date: 2 juin 2010
 * Time: 23:46:07
 * To change this template use File | Settings | File Templates.
 */
public class IssueTreeBuilder {

    private MantisGroupBy groupBy;

    private IssueListModel issueListModel;

    private IssueTreeModel treeModel;

    public IssueTreeBuilder(IssueListModel issueListModel) {
        this.issueListModel = issueListModel;
        groupBy = MantisGroupBy.NONE;
    }

    public void setGroupBy(MantisGroupBy groupBy) {
        this.groupBy = groupBy;
    }

    public void buildTree(JTree tree) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        tree.removeAll();
        treeModel = new IssueTreeModel(root);
        tree.setModel(treeModel);
        tree.setRootVisible(false);

        for (IIssueHeader issue : issueListModel.getIssues()) {
            ((DefaultMutableTreeNode) treeModel.getRoot()).add(new IssueTreeNode(issue));
        }
    }
}