package flefebvre.intellij.plugin.mantisbt.browser;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.treeStructure.SimpleTree;
import com.intellij.ui.treeStructure.actions.CollapseAllAction;
import com.intellij.ui.treeStructure.actions.ExpandAllAction;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: fred
 * Date: 7 juin 2010
 * Time: 22:14:01
 * To change this template use File | Settings | File Templates.
 */
public class MantisIssuesBrowserPanel extends SimpleToolWindowPanel {

    private final Project project;
    private final SimpleTree issueTree;

    public MantisIssuesBrowserPanel(Project project, SimpleTree tree) {
        super(true, true);

        this.project = project;
        this.issueTree = tree;

        setToolbar(createToolBar());
        setContent(new JScrollPane(issueTree));
    }

    private JComponent createToolBar() {
        final ActionManager actionManager = ActionManager.getInstance();
        DefaultActionGroup toolbarGroups = new DefaultActionGroup();

        ActionGroup filterActionsGroup = (ActionGroup) actionManager.getAction("FilterSortActionGroup");

        DefaultActionGroup treeActionsGroup = new DefaultActionGroup();
        treeActionsGroup.add(new ExpandAllAction(issueTree));
        treeActionsGroup.add(new CollapseAllAction(issueTree));

        ActionGroup issueActionsGroup = (ActionGroup) actionManager.getAction("IssueActionGroup");

        toolbarGroups.add(filterActionsGroup);
        toolbarGroups.add(treeActionsGroup);
        toolbarGroups.add(issueActionsGroup);

        return actionManager.createActionToolbar("Mantis Toolbar", toolbarGroups, true).getComponent();
    }
}
