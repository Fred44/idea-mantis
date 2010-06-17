package flefebvre.intellij.plugin.mantisbt.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import flefebvre.intellij.plugin.mantisbt.MantisManagerComponent;

/**
 * Created by IntelliJ IDEA.
 * User: flefebvre
 * Date: 30 mars 2010
 * Time: 21:00:54
 * To change this template use File | Settings | File Templates.
 */
public class RefreshAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent event) {
        final Project project = PlatformDataKeys.PROJECT.getData(event.getDataContext());
        MantisManagerComponent.getInstance(project).update();
    }

    @Override
    public void update(AnActionEvent e) {
        super.update(e);
//        MantisSession sess = ActionUtil.getMantisSession(e);
//        e.getPresentation().setEnabled(sess != null && sess.isConnected());
    }
}