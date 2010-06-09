package flefebvre.intellij.plugin.mantisbt;

import com.intellij.openapi.project.Project;
import flefebvre.intellij.plugin.mantisbt.model.RepositoryConfiguration;
import flefebvre.intellij.plugin.mantisbt.ui.MantisConfigurationForm;
import flefebvre.intellij.plugin.mantisbt.ui.RepositoryConfigurationData;
import flefebvre.intellij.plugin.mantisbt.ui.UiUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mantisbt.connect.IMCSession;
import org.mantisbt.connect.MCException;
import org.mantisbt.connect.axis.MCSession;
import org.mantisbt.connect.model.IProject;

import javax.swing.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: flefebvre
 * Date: 8 janv. 2010
 * Time: 21:18:23
 * To change this template use File | Settings | File Templates.
 */
public class MantisConfigurationManager {

    private static Log log = LogFactory.getLog(MantisConfigurationManager.class);

    private Project project;

    private RepositoryConfiguration config;
    private IMCSession session;

    private RepositoryConfigurationData formData;
    private MantisConfigurationForm configForm;

    private List<ConfigurationListener> cfgListeners;

    public MantisConfigurationManager(Project project, PluginConfiguration config) {
        this.project = project;
        this.config = config;

        this.cfgListeners = new ArrayList<ConfigurationListener>();
    }

    public void openConnection(String url, String username, String pass) {
        try {

            URL mantisUrl = new URL(url);
            this.session = new MCSession(mantisUrl, username, pass);

            log.info("Connected to Mantis [" + session.getVersion() + "]");
        } catch (MalformedURLException urle) {
            UiUtil.showError("Incorrect configuration", "URL is badly formed !");
            this.session = null;

        } catch (MCException mce) {
            log.error("Mantis failure", mce);
            UiUtil.showError("Mantis failure", mce.getMessage());
            this.session = null;
        }
    }

    public void refreshProjectsList() throws MCException {
        formData.setAvailableProjects(new IProject[0]);

        if (session != null) {
            formData.setAvailableProjects(session.getAccessibleProjects());
        }
    }

    public boolean isModified() {
        configForm.getData();

        if (formData.getName() != null ? !formData.getName().equals(config.getName()) : config.getName() != null)
            return true;
        if (formData.getUrl() != null ? !formData.getUrl().equals(config.getUrl()) : config.getUrl() != null)
            return true;
        if (formData.getUsername() != null ? !formData.getUsername().equals(config.getUsername()) : config.getUsername() != null)
            return true;
        if (formData.getPassword() != null ? !formData.getPassword().equals(config.getPassword()) : config.getPassword() != null)
            return true;
        if (formData.getProject() != null ? formData.getProject() != config.getProject() : config.getProject() != 0)
            return true;
        return false;
    }

    public void apply() {
        configForm.getData();

        if (isFormValid()) {
            RepositoryConfiguration oldCfg = new RepositoryConfiguration();
            oldCfg.setName(config.getName());
            oldCfg.setUrl(config.getUrl());
            oldCfg.setUsername(config.getUsername());
            oldCfg.setPassword(config.getPassword());
            oldCfg.setProject(config.getProject());

            config.setName(formData.getName());
            config.setUrl(formData.getUrl());
            config.setUsername(formData.getUsername());
            config.setPassword(formData.getPassword());
            config.setProject(formData.getProject() == null ? null : formData.getProject());

            if (!oldCfg.equals(config)) {
                notifyConfigChange(oldCfg, config);
            }
        }
    }

    public void reset() {
        if (config != null) {
            formData.setName(config.getName());
            formData.setUrl(config.getUrl());
            formData.setUsername(config.getUsername());
            formData.setPassword(config.getPassword());
            if (config.getProject() == 0) {
                formData.setProject(null);
            } else {
                formData.setProject(config.getProject());
            }

            configForm.setData();
        }
    }

    public boolean isConfigValid() {
        boolean isValid = true;

        if (config.getName() == null || "".equals(config.getName())) {
            isValid = false;
        }
        if (config.getUrl() == null || "".equals(config.getUrl())) {
            isValid = false;
        } else {
            try {
                URL mantisUrl = new URL(config.getUrl());
            } catch (MalformedURLException e) {
                isValid = false;
            }
        }
        if (config.getProject() <= 0) {
            isValid = false;
        }

        return isValid;
    }

    protected boolean isFormValid() {
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
        if (formData.getProject() == null) {
            isValid = false;
        }

        return isValid;
    }

    public void addConfigurationListener(ConfigurationListener cfgListener) {
        cfgListeners.add(cfgListener);
    }

    public void removeConfigurationListener(ConfigurationListener cfgListener) {
        cfgListeners.remove(cfgListener);
    }

    private void notifyConfigChange(RepositoryConfiguration oldCfg, RepositoryConfiguration newCfg) {
        for (ConfigurationListener cfgListener : cfgListeners) {
            cfgListener.configurationChange(oldCfg, newCfg);
        }
    }

    public JComponent getFormPanel() {
        if (isConfigValid()) {
            openConnection(config.getUrl(), config.getUsername(), config.getPassword());
        }

        formData = new RepositoryConfigurationData();
        if (config != null) {
            formData.setName(config.getName());
            formData.setUrl(config.getUrl() == null ? null : config.getUrl().toString());
            formData.setUsername(config.getUsername());
            formData.setPassword(config.getPassword());
            if (config.getProject() != 0) {
                formData.setProject(config.getProject());
            }

            if (session != null) {
                try {
                    refreshProjectsList();
                } catch (MCException mce) {
                    UiUtil.showError("Mantis failure", mce.getMessage());
                }
            }
        }
        configForm = new MantisConfigurationForm(project, this, formData);

        return configForm.getRootComponent();
    }

    public RepositoryConfiguration getConfig() {
        return config;
    }

    public void disposeUI() {
        configForm = null;
    }
}