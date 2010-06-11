package flefebvre.intellij.plugin.mantisbt.action;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import flefebvre.intellij.plugin.mantisbt.MantisSession;

/**
 * Created by IntelliJ IDEA.
 * User: flefebvre
 * Date: 31 mai 2010
 * Time: 21:15:47
 * To change this template use File | Settings | File Templates.
 */
public class ActionUtil {

//    public static IssueListPanel getIssueListPanel(AnActionEvent event) {
//        return getComponentInstanceOf(event, IssueListPanel.class);
//    }

//    public static IssueListManager getIssueListManager(AnActionEvent event) {
//        return getComponentInstanceOf(event, IssueListManager.class);
//    }

    public static MantisSession getMantisSession(AnActionEvent event) {
        return getComponentInstanceOf(event, MantisSession.class);
    }

//    public static RepositoryConfiguration getRepositoryConfig(AnActionEvent event) {
//        return getComponentInstanceOf(event, RepositoryConfiguration.class);
//    }

    private static <T> T getComponentInstanceOf(AnActionEvent event, Class<T> type) {
        final Project project = PlatformDataKeys.PROJECT.getData(event.getDataContext());

        if (project == null) {
            return null;
        } else {
            return (T) project.getPicoContainer().getComponentInstanceOfType(type);
        }
    }

    public static <T> T getComponentInstanceOfOnCurProject(Class<T> type) {
        final Project project = PlatformDataKeys.PROJECT.getData(DataManager.getInstance().getDataContext());

        if (project == null) {
            return null;
        } else {
            return (T) project.getPicoContainer().getComponentInstanceOfType(type);
        }
    }
}