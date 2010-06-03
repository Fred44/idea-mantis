package fred.intellij.plugin.mantisbt.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.mantisbt.connect.MCException;
import org.mantisbt.connect.model.IFilter;

/**
 * Created by IntelliJ IDEA.
 * User: fred
 * Date: 1 juin 2010
 * Time: 23:50:58
 * To change this template use File | Settings | File Templates.
 */
public class ActivateFilterAction extends AnAction {

    private IFilter filter;

    public ActivateFilterAction(IFilter filter) {
        this.filter = filter;

        if (filter != null) {
            getTemplatePresentation().setText(filter.getName());
            getTemplatePresentation().setDescription(filter.getName());
        }
    }

    @Override
    public void actionPerformed(AnActionEvent event) {
        try {
            ActionUtil.getIssueListManager(event).setActiveFilter(filter);
        } catch (MCException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}