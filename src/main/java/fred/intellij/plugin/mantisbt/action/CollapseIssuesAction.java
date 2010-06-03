package fred.intellij.plugin.mantisbt.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import fred.intellij.plugin.mantisbt.ui.IssueListPanel;

/**
 * Created by IntelliJ IDEA.
 * User: fred
 * Date: 13 avr. 2010
 * Time: 20:41:45
 * To change this template use File | Settings | File Templates.
 */
public class CollapseIssuesAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent event) {
        IssueListPanel issuesListPanel = ActionUtil.getIssueListPanel(event);
        issuesListPanel.collapseTree();
    }
}