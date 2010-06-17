package flefebvre.intellij.plugin.mantisbt.browser;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.PopupHandler;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.treeStructure.SimpleTree;
import com.intellij.ui.treeStructure.actions.CollapseAllAction;
import com.intellij.ui.treeStructure.actions.ExpandAllAction;
import flefebvre.intellij.plugin.mantisbt.MantisManagerComponent;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

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
        setContent(ScrollPaneFactory.createScrollPane(issueTree));

        issueTree.addMouseListener(new PopupHandler() {
            public void invokePopup(final Component comp, final int x, final int y) {
                final ActionManager actionManager = ActionManager.getInstance();
                final String id = getMenuId(getSelectedNode());
                if (id != null) {
                    final ActionGroup actionGroup = (ActionGroup) actionManager.getAction(id);
//                    if (actionGroup != null) {
//                        actionManager.createActionPopupMenu("", actionGroup).getComponent().show(comp, x, y);
//                    }
                }
            }

            @Nullable
            private String getMenuId(MantisStructure.SimpleNode node) {
                return node.getMenuId();
            }
        });
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
        final JLabel priorityLabel = new JLabel();
        final JPanel panel = new JPanel(new BorderLayout());
        panel.add(priorityLabel, BorderLayout.EAST);
        final TreeCellRenderer baseRenderer = issueTree.getCellRenderer();
        issueTree.setCellRenderer(new IssueTreeRenderer(baseRenderer));
        issueTree.addComponentListener(new MyTreeComponentListener(issueTree));


//        TreeUIHelper uiHelper = TreeUIHelper.getInstance();
//        uiHelper.installToolTipHandler(issueTree);
//        uiHelper.installTreeSpeedSearch(issueTree);
//        TreeUtil.installActions(issueTree);

        scheduleStructureUpdate();
        issueTree.requestFocus();
    }

    public void scheduleStructureUpdate() {
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

    private MantisStructure.SimpleNode getSelectedNode() {
        return MantisStructure.getSelectedNode(issueTree);
    }

    private void initStructure() {
        this.structure = new MantisStructure(project, mantisMgr, issueTree);
    }

    private class MyTreeComponentListener extends ComponentAdapter {
        private JTree tree;


        public MyTreeComponentListener(JTree tree) {
            this.tree = tree;
        }

        public void componentResized(ComponentEvent e) {
            if (tree.isVisible()) {
                registerUI();
            }
        }

        private void registerUI() {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    tree.setUI(new BasicWideNodeTreeUI());
                }
            });
        }
    }
}
