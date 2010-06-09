package flefebvre.intellij.plugin.mantisbt.ui.tree;

import org.mantisbt.connect.model.IIssueHeader;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Created by IntelliJ IDEA.
 * User: fred
 * Date: 7 juin 2010
 * Time: 21:12:49
 * To change this template use File | Settings | File Templates.
 */
public class IssueTreeNode extends DefaultMutableTreeNode {

    private final IIssueHeader issue;

    public IssueTreeNode(IIssueHeader issue) {
        super(issue.getId() + " - " + issue.getSummary());
        this.issue = issue;
    }
}
