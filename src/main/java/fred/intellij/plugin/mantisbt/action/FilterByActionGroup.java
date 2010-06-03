package fred.intellij.plugin.mantisbt.action;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.actionSystem.ex.ComboBoxAction;
import com.intellij.openapi.project.Project;
import fred.intellij.plugin.mantisbt.MantisSession;
import fred.intellij.plugin.mantisbt.model.IssueListManager;
import org.jetbrains.annotations.NotNull;
import org.mantisbt.connect.MCException;
import org.mantisbt.connect.model.IFilter;

import javax.swing.*;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: fred
 * Date: 13 avr. 2010
 * Time: 22:47:31
 * To change this template use File | Settings | File Templates.
 */
public class FilterByActionGroup extends ComboBoxAction {
//public class FilterByActionGroup extends AnAction implements CustomComponentAction {
//    private MantisSession session;
//
//    @Override
//    public void actionPerformed(AnActionEvent anActionEvent) {
//        //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    public JComponent createCustomComponent(Presentation presentation) {
//        ComboBoxModel model = new DefaultComboBoxModel();
//        if( session != null ) {
//            try {
//                model = new DefaultComboBoxModel(session.getFilters().toArray());
//            } catch (MCException e) {
//                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//            }
//        }
//        JPanel filterByPane = new JPanel();
//        FlowLayout layout = new FlowLayout();
//		layout.setVgap(0);
//		filterByPane.setLayout(layout);
//        JLabel filterByLabel = new JLabel("Filter by");
//        JComboBox filterByCombo = new JComboBox(model);
//        filterByPane.add(filterByLabel);
//        filterByPane.add(filterByCombo);
//
//        presentation.putClientProperty(FilterByActionGroup.class.getName() + ".combo", filterByCombo);
//
//        return filterByPane;
//    }


    @Override
    public void update(AnActionEvent event) {
        super.update(event);
        IssueListManager issLstMgr = ActionUtil.getIssueListManager(event);
        if (issLstMgr != null) {
            IFilter activeFilter = ActionUtil.getIssueListManager(event).getActiveFilter();
            if (activeFilter != null) {
                event.getPresentation().setText(activeFilter.getName());
                event.getPresentation().setDescription(activeFilter.getName());
            }
        }
    }

    @NotNull
    @Override
    protected DefaultActionGroup createPopupActionGroup(JComponent jComponent) {
        Project project = PlatformDataKeys.PROJECT.getData(DataManager.getInstance().getDataContext(jComponent));
        MantisSession session = (MantisSession) project.getPicoContainer().getComponentInstanceOfType(MantisSession.class);
        DefaultActionGroup filterGroup = new DefaultActionGroup("Use filter", true);

        filterGroup.add(new ConfigureFilterAction());
        filterGroup.addSeparator();

        if (session != null && session.isConnected()) {
            try {
                Collection<IFilter> filters = session.getFilters();
                if (filters != null) {
                    for (IFilter filter : filters) {
                        filterGroup.add(new ActivateFilterAction(filter));
                    }
                }
            } catch (MCException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        return filterGroup;
    }
}