package flefebvre.intellij.plugin.mantisbt;

import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupManager;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.content.ContentManager;
import flefebvre.intellij.plugin.mantisbt.browser.MantisIssuesBrowserPanel;
import flefebvre.intellij.plugin.mantisbt.browser.MantisIssuesBrowserState;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: fred
 * Date: 8 juin 2010
 * Time: 08:35:49
 * To change this template use File | Settings | File Templates.
 */
@State(name = "MantisIssuesBrowser",
        storages = {@Storage(id = "default", file = "$WORKSPACE_FILE$")})
public class MantisWindowComponent extends AbstractProjectComponent implements PersistentStateComponent<MantisIssuesBrowserState> {

    public static final String TOOL_WINDOW_ID = "Mantis";

    private MantisManagerComponent mantisMgr;

    private MantisIssuesBrowserState state;

    private ToolWindow toolWindow;

    public MantisWindowComponent(Project project, @NotNull MantisManagerComponent mantisMgr) {
        super(project);
        this.mantisMgr = mantisMgr;

        state = new MantisIssuesBrowserState();

        StartupManager.getInstance(project).registerPostStartupActivity(new Runnable() {
            public void run() {
                initToolWindow();
            }
        });
    }

    private void initToolWindow() {
        final ToolWindowManager manager = ToolWindowManager.getInstance(myProject);
        toolWindow = manager.registerToolWindow(TOOL_WINDOW_ID, false, ToolWindowAnchor.BOTTOM);
        toolWindow.setIcon(MantisIcons.MANTIS_ICON);

        addTab(MantisIssuesBrowserPanel.TAB_NAME, new MantisIssuesBrowserPanel(myProject, mantisMgr), true, true, true);
    }

    public void addTab(String name,
                       JComponent component,
                       boolean selectTab,
                       boolean replaceContent,
                       boolean lockable) {

        Content content = getContentManager().findContent(name);

        if (content != null) {
            if (replaceContent) {
                if (!content.isPinned()) {
                    getContentManager().removeContent(content, true);
                    content.release();
                }

            } else {
                getContentManager().setSelectedContent(content);
                return;
            }
        }

        content = ContentFactory.SERVICE.getInstance().createContent(component, name, lockable);
//        newComponent.setContent(content);
        getContentManager().addContent(content);

        if (selectTab) {
            getContentManager().setSelectedContent(content);
        }
    }

    public ContentManager getContentManager() {
        return toolWindow.getContentManager();
    }

    @Override
    public MantisIssuesBrowserState getState() {
        return state;
    }

    @Override
    public void loadState(MantisIssuesBrowserState mantisIssuesBrowserState) {
        this.state = mantisIssuesBrowserState;
    }

    public static MantisWindowComponent getInstance(final Project project) {
        return ServiceManager.getService(project, MantisWindowComponent.class);
    }
}
