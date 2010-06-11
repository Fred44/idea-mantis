package flefebvre.intellij.plugin.mantisbt.browser;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.actionSystem.ex.ComboBoxAction;
import com.intellij.openapi.project.Project;
import flefebvre.intellij.plugin.mantisbt.model.MantisGroupBy;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: fred
 * Date: 9 juin 2010
 * Time: 22:36:36
 * To change this template use File | Settings | File Templates.
 */
public class GroupByMenu extends ComboBoxAction {

    private DefaultActionGroup myActionGroup;
    private JPanel myPanel;

    private MantisGroupBy active = MantisGroupBy.NONE;

    public final void update(final AnActionEvent e) {
        final Presentation presentation = e.getPresentation();
        final Project project = PlatformDataKeys.PROJECT.getData(e.getDataContext());
        if (project == null) return;

        presentation.setText(" " + active.toString());
    }

    @NotNull
    @Override
    protected DefaultActionGroup createPopupActionGroup(JComponent jComponent) {
        myActionGroup = new DefaultActionGroup();

        for (MantisGroupBy groupBy : MantisGroupBy.values()) {
            myActionGroup.add(new ActivateGroupByAction(groupBy));
        }

        return myActionGroup;
    }

    public final JComponent createCustomComponent(Presentation presentation) {
        myPanel = new JPanel(new GridBagLayout());
        myPanel.add(new JLabel("Group by"),
                new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
                        new Insets(0, 5, 0, 0), 0, 0));
        myPanel.add(super.createCustomComponent(presentation),
                new GridBagConstraints(1, 0, 1, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));
        return myPanel;
    }

    class ActivateGroupByAction extends AnAction {

        private MantisGroupBy groupBy;

        public ActivateGroupByAction(MantisGroupBy groupBy) {
            this.groupBy = groupBy;

            if (groupBy != null) {
                getTemplatePresentation().setText(groupBy.toString());
                getTemplatePresentation().setDescription("Group by " + groupBy.toString());
            }
        }

        @Override
        public void actionPerformed(AnActionEvent anActionEvent) {
            active = groupBy;
        }
    }
}
