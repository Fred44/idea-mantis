package fred.intellij.plugin.mantisbt.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import fred.intellij.plugin.mantisbt.MantisSession;
import fred.intellij.plugin.mantisbt.ui.IssueListPanel;

/**
 * Created by IntelliJ IDEA.
 * User: fred
 * Date: 30 mars 2010
 * Time: 21:00:54
 * To change this template use File | Settings | File Templates.
 */
public class RefreshAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent event) {

        IssueListPanel issuesPanel = ActionUtil.getIssueListPanel(event);
        if (issuesPanel != null) {
            issuesPanel.refresh();
        }
    }

    @Override
    public void update(AnActionEvent e) {
        super.update(e);
        MantisSession sess = ActionUtil.getMantisSession(e);
        e.getPresentation().setEnabled(sess != null && sess.isConnected());
    }
}