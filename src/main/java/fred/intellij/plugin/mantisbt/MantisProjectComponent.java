package fred.intellij.plugin.mantisbt;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.wm.ToolWindowManager;
import fred.intellij.plugin.mantisbt.model.IssueListManager;
import fred.intellij.plugin.mantisbt.model.RepositoryConfiguration;
import fred.intellij.plugin.mantisbt.ui.PluginToolWindow;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.mantisbt.connect.MCException;

import javax.swing.*;
import java.net.MalformedURLException;

/**
 * Created by IntelliJ IDEA.
 * User: fred
 * Date: 8 janv. 2010
 * Time: 15:29:33
 * To change this template use File | Settings | File Templates.
 */

public class MantisProjectComponent implements ProjectComponent, Configurable {

    private static Log log = LogFactory.getLog(MantisProjectComponent.class);

    private Project project;
    private ToolWindowManager toolWindowMgr;
    private PluginToolWindow pluginToolWindow;

    private MantisConfigurationManager configMgr;
    private RepositoryConfiguration repoCfg;
    private IssueListManager issueListMgr;

    private MantisSession session;

    public MantisProjectComponent(Project project,
                                  ToolWindowManager toolWindowMgr,
                                  @NotNull PluginToolWindow pluginToolWindow,
                                  RepositoryConfiguration repoCfg,
                                  @NotNull IssueListManager issueListMgr,
                                  @NotNull MantisSession session) {

        this.project = project;
        this.toolWindowMgr = toolWindowMgr;
        this.pluginToolWindow = pluginToolWindow;

        this.repoCfg = repoCfg;
        this.issueListMgr = issueListMgr;
        this.session = session;

        StartupManager.getInstance(project).registerPostStartupActivity(new Runnable() {
            public void run() {
                log.info("Start: Project initializing");
                initPlugin();
                log.info("End: Project initialized");
            }
        });
    }

    private void initPlugin() {
        pluginToolWindow.register(toolWindowMgr);
        pluginToolWindow.initPanel();

        if (repoCfg != null) {
            try {
                session.setProject(repoCfg.getProject());
                session.open(repoCfg.getUrl(), repoCfg.getUsername(), repoCfg.getPassword());

                log.info("Connected to Mantis [" + session.getVersion() + "]");

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

    public void initComponent() {
        log.debug("init MantisProjectComponent");

    }

    public void disposeComponent() {
    }

    @NotNull
    public String getComponentName() {
        return "MantisProjectComponent";
    }

    public void projectOpened() {
    }

    public void projectClosed() {
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
        if (configMgr == null) {
            configMgr = new MantisConfigurationManager(project, repoCfg);
        }

        return configMgr.getFormPanel();
    }

    public boolean isModified() {
        return configMgr.isModified(repoCfg);
    }

    public void apply() throws ConfigurationException {
        configMgr.apply(repoCfg);
    }

    public void reset() {
        configMgr.reset(repoCfg);
    }

    public void disposeUIResources() {
        configMgr = null;
    }

    public PluginToolWindow getPluginToolWindow() {
        return pluginToolWindow;
    }
}