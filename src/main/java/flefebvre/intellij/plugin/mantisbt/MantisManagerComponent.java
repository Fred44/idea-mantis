package flefebvre.intellij.plugin.mantisbt;

import com.intellij.openapi.components.AbstractProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupManager;
import flefebvre.intellij.plugin.mantisbt.settings.MantisProjectSettings;
import flefebvre.intellij.plugin.mantisbt.settings.MantisSettingsComponent;

/**
 * Created by IntelliJ IDEA.
 * User: fred
 * Date: 8 juin 2010
 * Time: 22:36:24
 * To change this template use File | Settings | File Templates.
 */
public class MantisManagerComponent extends AbstractProjectComponent {

    public MantisManagerComponent(Project project) {
        super(project);
    }

    public MantisProjectSettings getSettings() {
        return MantisSettingsComponent.getInstance(myProject).getState();
    }

    @Override
    public void initComponent() {

        StartupManager.getInstance(myProject).registerStartupActivity(new Runnable() {
            @Override
            public void run() {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
    }

    public static MantisManagerComponent getInstance(Project project) {
        return project.getComponent(MantisManagerComponent.class);
    }
}
