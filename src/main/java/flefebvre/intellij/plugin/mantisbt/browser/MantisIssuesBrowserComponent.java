package flefebvre.intellij.plugin.mantisbt.browser;

import com.intellij.openapi.components.AbstractProjectComponent;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupManager;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.treeStructure.SimpleTree;
import flefebvre.intellij.plugin.mantisbt.MantisIcons;

import javax.swing.*;
import javax.swing.tree.TreeSelectionModel;

/**
 * Created by IntelliJ IDEA.
 * User: fred
 * Date: 8 juin 2010
 * Time: 08:35:49
 * To change this template use File | Settings | File Templates.
 */
@State(name = "MantisIssuesBrowser",
        storages = {@Storage(id = "default", file = "$WORKSPACE_FILE$")})
public class MantisIssuesBrowserComponent extends AbstractProjectComponent implements PersistentStateComponent<MantisIssuesBrowserState> {

    public static final String TOOL_WINDOW_ID = "Mantis issues";

    private MantisIssuesBrowserState state;

    private ToolWindow toolWindow;
    private SimpleTree issueTree;
    private MantisStructure myStructure;

    public MantisIssuesBrowserComponent(Project project) {
        super(project);

        state = new MantisIssuesBrowserState();

        StartupManager.getInstance(project).registerPostStartupActivity(new Runnable() {
            public void run() {
                initToolWindow();
            }
        });
    }

    private void initToolWindow() {
        initTree();
        JPanel panel = new MantisIssuesBrowserPanel(myProject, issueTree);

        final ToolWindowManager manager = ToolWindowManager.getInstance(myProject);
//        toolWindow = manager.registerToolWindow(TOOL_WINDOW_ID, true, ToolWindowAnchor.BOTTOM);
        toolWindow = manager.registerToolWindow(TOOL_WINDOW_ID, panel, ToolWindowAnchor.BOTTOM, myProject, true);
//        toolWindow.getContentManager().getFactory().createContent(panel, "issues", false);
        toolWindow.setIcon(MantisIcons.MANTIS_ICON);

//        final ToolWindowManagerAdapter listener = new ToolWindowManagerAdapter() {
//          boolean wasVisible = false;
//
//          @Override
//          public void stateChanged() {
//            if (toolWindow.isDisposed()) return;
//            boolean visible = toolWindow.isVisible();
//            if (!visible || visible == wasVisible) return;
//            scheduleStructureUpdate();
//            wasVisible = visible;
//          }
//        };
//        manager.addToolWindowManagerListener(listener);
//        Disposer.register(project, new Disposable() {
//          public void dispose() {
//            manager.removeToolWindowManagerListener(listener);
//          }
//        });
    }

    private void initTree() {
        issueTree = new SimpleTree();
        issueTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    }

    private void initStructure() {
        myStructure = new MantisStructure(myProject, issueTree);
    }

    @Override
    public MantisIssuesBrowserState getState() {
        return state;
    }

    @Override
    public void loadState(MantisIssuesBrowserState mantisIssuesBrowserState) {
        this.state = mantisIssuesBrowserState;
    }
}
