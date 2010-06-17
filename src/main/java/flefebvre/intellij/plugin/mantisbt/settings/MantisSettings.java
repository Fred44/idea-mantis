package flefebvre.intellij.plugin.mantisbt.settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
import flefebvre.intellij.plugin.mantisbt.MantisIcons;
import flefebvre.intellij.plugin.mantisbt.MantisManagerComponent;
import org.jetbrains.annotations.Nls;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: fred
 * Date: 8 juin 2010
 * Time: 18:37:51
 * To change this template use File | Settings | File Templates.
 */
public class MantisSettings implements SearchableConfigurable {

    private final Project myProject;
    private final Configurable myConfigurable;

    public MantisSettings(Project project) {
        this.myProject = project;
        myConfigurable = new MantisConfigurable(myProject, MantisManagerComponent.getInstance(myProject).getAvailableProjects()) {

            @Override
            protected MantisProjectSettings getState() {
                return MantisSettingsComponent.getInstance(myProject).getState();
            }
        };
    }

    @Override
    public String getId() {
        return MantisSettings.class.getSimpleName();
    }

    @Override
    public Runnable enableSearch(String s) {
        return null;
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "Mantis";
    }

    @Override
    public Icon getIcon() {
        return MantisIcons.MANTIS_ICON;
    }

    @Override
    public String getHelpTopic() {
        return myConfigurable.getHelpTopic();
    }

    @Override
    public JComponent createComponent() {
        return myConfigurable.createComponent();
    }

    @Override
    public boolean isModified() {
        return myConfigurable.isModified();
    }

    @Override
    public void apply() throws ConfigurationException {
        myConfigurable.apply();
    }

    @Override
    public void reset() {
        myConfigurable.reset();
    }

    @Override
    public void disposeUIResources() {
        myConfigurable.disposeUIResources();
    }
}
