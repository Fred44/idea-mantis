package flefebvre.intellij.plugin.mantisbt.browser;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.TreeUIHelper;
import com.intellij.ui.treeStructure.SimpleTree;
import com.intellij.ui.treeStructure.actions.CollapseAllAction;
import com.intellij.ui.treeStructure.actions.ExpandAllAction;
import com.intellij.util.ui.tree.TreeUtil;
import flefebvre.intellij.plugin.mantisbt.MantisManagerComponent;

import javax.swing.*;
import javax.swing.tree.TreeSelectionModel;

/**
 * Created by IntelliJ IDEA.
 * User: fred
 * Date: 7 juin 2010
 * Time: 22:14:01
 * To change this template use File | Settings | File Templates.
 */
public class MantisIssuesBrowserPanel extends SimpleToolWindowPanel {

    public static final String TAB_NAME = "Issues browser";

    private final Project project;
    private final SimpleTree issueTree;
    private MantisStructure structure;

    private MantisManagerComponent mantisMgr;

    public MantisIssuesBrowserPanel(Project project, MantisManagerComponent mantisMgr) {
        super(true, false);

        this.project = project;
        this.issueTree = new SimpleTree();
        this.mantisMgr = mantisMgr;

        initTree();

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

    public void initTree() {
        issueTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        TreeUIHelper uiHelper = TreeUIHelper.getInstance();
        uiHelper.installToolTipHandler(issueTree);
        uiHelper.installTreeSpeedSearch(issueTree);
        TreeUtil.installActions(issueTree);

        scheduleStructureUpdate();
        issueTree.requestFocus();
    }

    private void scheduleStructureUpdate() {
        scheduleStructureRequest(new Runnable() {
            public void run() {
                structure.update();
            }
        });
    }

    private void scheduleStructureRequest(final Runnable r) {
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            public void run() {
                if (project.isDisposed()) return;
                initStructure();
                r.run();
            }
        }, ModalityState.defaultModalityState());
    }

    private void initStructure() {
        this.structure = new MantisStructure(project, mantisMgr, issueTree);
    }
}
