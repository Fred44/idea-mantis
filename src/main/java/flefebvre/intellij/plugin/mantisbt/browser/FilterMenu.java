package flefebvre.intellij.plugin.mantisbt.browser;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.actionSystem.ex.ComboBoxAction;
import com.intellij.openapi.project.Project;
import flefebvre.intellij.plugin.mantisbt.MantisManagerComponent;
import flefebvre.intellij.plugin.mantisbt.MantisSession;
import org.jetbrains.annotations.NotNull;
import org.mantisbt.connect.MCException;
import org.mantisbt.connect.model.IFilter;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: fred
 * Date: 9 juin 2010
 * Time: 23:30:15
 * To change this template use File | Settings | File Templates.
 */
public class FilterMenu extends ComboBoxAction {
    private DefaultActionGroup myActionGroup;
    private JPanel myPanel;

    private IFilter active;

    public final void update(final AnActionEvent e) {
        final Presentation presentation = e.getPresentation();
        final Project project = PlatformDataKeys.PROJECT.getData(e.getDataContext());
        if (project == null) return;

        if (active != null) {
            presentation.setText(" " + active.getName());
        }
    }

    @NotNull
    @Override
    protected DefaultActionGroup createPopupActionGroup(JComponent jComponent) {
        myActionGroup = new DefaultActionGroup();
        final Project project = PlatformDataKeys.PROJECT.getData(DataManager.getInstance().getDataContext(jComponent));
        final MantisSession session = MantisManagerComponent.getInstance(project).getSession();
        Collection<IFilter> filters = null;
        try {
            filters = session.getFilters();

            for (IFilter filter : filters) {
                myActionGroup.add(new ActivateFilterAction(filter));
            }
        } catch (MCException ignore) {
        }

        return myActionGroup;
    }

    public final JComponent createCustomComponent(Presentation presentation) {
        myPanel = new JPanel(new GridBagLayout());
        myPanel.add(new JLabel("Filter by"),
                new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
                        new Insets(0, 5, 0, 0), 0, 0));
        myPanel.add(super.createCustomComponent(presentation),
                new GridBagConstraints(1, 0, 1, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));
        return myPanel;
    }

    class ActivateFilterAction extends AnAction {

        private IFilter filter;

        public ActivateFilterAction(IFilter filter) {
            this.filter = filter;

            if (filter != null) {
                getTemplatePresentation().setText(filter.getName());
                getTemplatePresentation().setDescription("Filter with " + filter.getName() + "[" + filter.getId() + "]");
            }
        }

        @Override
        public void actionPerformed(AnActionEvent anActionEvent) {
            active = filter;
        }
    }
}
