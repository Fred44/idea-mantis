package flefebvre.intellij.plugin.mantisbt;

import com.intellij.openapi.components.AbstractProjectComponent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.treeStructure.SimpleTree;
import flefebvre.intellij.plugin.mantisbt.browser.MantisIssuesBrowserPanel;
import flefebvre.intellij.plugin.mantisbt.browser.MantisStructure;
import flefebvre.intellij.plugin.mantisbt.model.IssueListManager;
import flefebvre.intellij.plugin.mantisbt.ui.PluginToolWindow;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.mantisbt.connect.MCException;

import javax.swing.*;
import javax.swing.tree.TreeSelectionModel;
import java.net.MalformedURLException;

/**
 * Created by IntelliJ IDEA.
 * User: flefebvre
 * Date: 8 janv. 2010
 * Time: 15:29:33
 * To change this template use File | Settings | File Templates.
 */

public class MantisProjectComponent extends AbstractProjectComponent implements Configurable {

    private static Log log = LogFactory.getLog(MantisProjectComponent.class);

    public static final String TOOL_WINDOW_ID = "Mantis issues";

    private ToolWindow toolWindow;
    private SimpleTree issueTree;
    private MantisStructure myStructure;

    private ToolWindowManager toolWindowMgr;
    private PluginToolWindow pluginToolWindow;

    private MantisConfigurationManager configMgr;
    private IssueListManager issueListMgr;

    private MantisSession session;

    public MantisProjectComponent(Project project,
                                  ToolWindowManager toolWindowMgr,
                                  @NotNull PluginToolWindow pluginToolWindow,
                                  @NotNull MantisConfigurationManager configMgr,
                                  @NotNull IssueListManager issueListMgr,
                                  @NotNull MantisSession session) {

        super(project);
        this.toolWindowMgr = toolWindowMgr;
        this.pluginToolWindow = pluginToolWindow;

        this.configMgr = configMgr;
        this.issueListMgr = issueListMgr;
        this.session = session;

        initPlugin();

        StartupManager.getInstance(project).registerPostStartupActivity(new Runnable() {
            public void run() {
                log.info("Start: Project initializing");
                initToolWindow();
                log.info("End: Project initialized");
            }
        });
    }

    private void initPlugin() {
//        pluginToolWindow.register(toolWindowMgr);
//        pluginToolWindow.initPanel();

        if (configMgr != null && configMgr.isConfigValid()) {
            try {
                session.setProject(configMgr.getConfig().getProject());
                session.open(configMgr.getConfig().getUrl(), configMgr.getConfig().getUsername(), configMgr.getConfig().getPassword());

                log.info("Connected to Mantis [" + session.getVersion() + "]");

                issueListMgr.init();

            } catch (MalformedURLException urle) {
                Messages.showMessageDialog(
                        "URL is badly formed !",
                        "Connection failure",
                        Messages.getErrorIcon());
                session = null;

            } catch (MCException mce) {
                Messages.showMessageDialog(
                        mce.getMessage(),
                        "Mantis failure",
                        Messages.getErrorIcon());
                session = null;
            }
        }
    }

    @Nls
    public String getDisplayName() {
        return "Mantis";
    }

    public Icon getIcon() {
        return IconLoader.getIcon("/icons/mantis.png");
    }

    public String getHelpTopic() {
        return null;
    }

    public JComponent createComponent() {
        return configMgr.getFormPanel();
    }

    public boolean isModified() {
        return configMgr.isModified();
    }

    public void apply() throws ConfigurationException {
        configMgr.apply();
    }

    public void reset() {
        configMgr.reset();
    }

    public void disposeUIResources() {
        configMgr.disposeUI();
    }

    public PluginToolWindow getPluginToolWindow() {
        return pluginToolWindow;
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
}