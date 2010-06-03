package fred.intellij.plugin.mantisbt.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import fred.intellij.plugin.mantisbt.MantisSession;
import fred.intellij.plugin.mantisbt.model.IssueListManager;
import fred.intellij.plugin.mantisbt.model.RepositoryConfiguration;
import fred.intellij.plugin.mantisbt.ui.IssueListPanel;

/**
 * Created by IntelliJ IDEA.
 * User: fred
 * Date: 31 mai 2010
 * Time: 21:15:47
 * To change this template use File | Settings | File Templates.
 */
public class ActionUtil {

    public static IssueListPanel getIssueListPanel(AnActionEvent event) {
        return getComponentInstanceOf(event, IssueListPanel.class);
    }

    public static IssueListManager getIssueListManager(AnActionEvent event) {
        return getComponentInstanceOf(event, IssueListManager.class);
    }

    public static MantisSession getMantisSession(AnActionEvent event) {
        return getComponentInstanceOf(event, MantisSession.class);
    }

    public static RepositoryConfiguration getRepositoryConfig(AnActionEvent event) {
        return getComponentInstanceOf(event, RepositoryConfiguration.class);
    }

    private static <T> T getComponentInstanceOf(AnActionEvent event, Class<T> type) {
        final Project project = PlatformDataKeys.PROJECT.getData(event.getDataContext());

        if (project == null) {
            return null;
        } else {
            return (T) project.getPicoContainer().getComponentInstanceOfType(type);
        }
    }
}