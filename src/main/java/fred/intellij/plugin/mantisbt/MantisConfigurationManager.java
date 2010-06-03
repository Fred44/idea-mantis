package fred.intellij.plugin.mantisbt;

import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import fred.intellij.plugin.mantisbt.model.RepositoryConfiguration;
import fred.intellij.plugin.mantisbt.ui.MantisConfigurationForm;
import fred.intellij.plugin.mantisbt.ui.RepositoryConfigurationData;
import fred.intellij.plugin.mantisbt.ui.UiUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jetbrains.annotations.NotNull;
import org.mantisbt.connect.IMCSession;
import org.mantisbt.connect.MCException;
import org.mantisbt.connect.axis.MCSession;
import org.mantisbt.connect.model.IProject;

import javax.swing.*;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: fred
 * Date: 8 janv. 2010
 * Time: 21:18:23
 * To change this template use File | Settings | File Templates.
 */
public class MantisConfigurationManager {

    private static Log log = LogFactory.getLog(MantisConfigurationManager.class);

    private com.intellij.openapi.project.Project project;

    private IMCSession session;
    private RepositoryConfigurationData formData;
    private MantisConfigurationForm configForm;

    public MantisConfigurationManager(com.intellij.openapi.project.Project project) {
        this(project, null);
    }

    public MantisConfigurationManager(com.intellij.openapi.project.Project project, RepositoryConfiguration config) {
        this.project = project;
        formData = new RepositoryConfigurationData();

        if (config != null) {
            formData.setName(config.getName());
            formData.setUrl(config.getUrl() == null ? null : config.getUrl().toString());
            formData.setUsername(config.getUsername());
            formData.setPassword(config.getPassword());
            if (config.getProject() != 0) {
                formData.setProject(config.getProject());
            }

            if (isValid()) {
                openConnection();
                try {
                    refreshProjectsList();
                } catch (MCException mce) {
                    UiUtil.showError("Mantis failure", mce.getMessage());
                }
            }
        }

        configForm = new MantisConfigurationForm(this);
        configForm.setData(formData);
    }

    public void onOpenConnectionClick() {
        configForm.getData(formData);
        if (isValid()) {
            Task.Modal openTask = new Task.Modal(project, "Mantis", false) {
                @Override
                public void run(@NotNull ProgressIndicator progressIndicator) {
                    try {
                        openConnection();
                        refreshProjectsList();
                    } catch (MCException mce) {
                        UiUtil.showError("Mantis failure", mce.getMessage());
                    }
                }

                @Override
                public void onSuccess() {
                    configForm.setData(formData);
                }
            };
            ProgressManager.getInstance().run(openTask);
        }
    }

    private void openConnection() {
        try {

            URL mantisUrl = new URL(formData.getUrl());
            this.session = new MCSession(mantisUrl,
                    formData.getUsername(),
                    formData.getPassword());

            log.info("Connected to Mantis [" + session.getVersion() + "]");
        } catch (MalformedURLException urle) {
            UiUtil.showError("Connection failure", "URL is badly formed !");
            this.session = null;

        } catch (MCException mce) {
            log.error("Mantis failure", mce);
            UiUtil.showError("Mantis failure", mce.getMessage());
            this.session = null;
        }
    }

    private void refreshProjectsList() throws MCException {
        formData.setAvailableProjects(new IProject[0]);

        if (session != null) {
            formData.setAvailableProjects(session.getAccessibleProjects());
        }
    }

    public boolean isModified(RepositoryConfiguration repoCfg) {
        configForm.getData(formData);

        if (formData.getName() != null ? !formData.getName().equals(repoCfg.getName()) : repoCfg.getName() != null)
            return true;
        if (formData.getUrl() != null ? !formData.getUrl().equals(repoCfg.getUrl()) : repoCfg.getUrl() != null)
            return true;
        if (formData.getUsername() != null ? !formData.getUsername().equals(repoCfg.getUsername()) : repoCfg.getUsername() != null)
            return true;
        if (formData.getPassword() != null ? !formData.getPassword().equals(repoCfg.getPassword()) : repoCfg.getPassword() != null)
            return true;
        if (formData.getProject() != null ? formData.getProject() != repoCfg.getProject() : repoCfg.getProject() != 0)
            return true;
        return false;
    }

    public void apply(RepositoryConfiguration repoCfg) {
        configForm.getData(formData);

        if (isValid()) {
            repoCfg.setName(formData.getName());
            repoCfg.setUrl(formData.getUrl());
            repoCfg.setUsername(formData.getUsername());
            repoCfg.setPassword(formData.getPassword());
            repoCfg.setProject(formData.getProject() == null ? null : formData.getProject());

            this.session = null;
        }
    }

    public void reset(RepositoryConfiguration repoCfg) {
        if (repoCfg != null) {
            formData.setName(repoCfg.getName());
            formData.setUrl(repoCfg.getUrl());
            formData.setUsername(repoCfg.getUsername());
            formData.setPassword(repoCfg.getPassword());
            if (repoCfg.getProject() == 0) {
                formData.setProject(null);
            } else {
                formData.setProject(repoCfg.getProject());
            }

            configForm.setData(formData);
        }
        this.session = null;
    }

    protected boolean isValid() {
        boolean isValid = true;

        if (formData.getName() == null || "".equals(formData.getName())) {
            isValid = false;
        }
        if (formData.getUrl() == null || "".equals(formData.getUrl())) {
            isValid = false;
        } else {
            try {
                URL mantisUrl = new URL(formData.getUrl());
            } catch (MalformedURLException e) {
                isValid = false;
            }
        }

        return isValid;
    }

    public JComponent getFormPanel() {
        return configForm.getRootComponent();
    }
}