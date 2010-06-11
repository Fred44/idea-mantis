package flefebvre.intellij.plugin.mantisbt;

import com.intellij.openapi.components.AbstractProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupManager;
import flefebvre.intellij.plugin.mantisbt.settings.MantisProjectSettings;
import flefebvre.intellij.plugin.mantisbt.settings.MantisSettingsComponent;
import org.mantisbt.connect.MCException;
import org.mantisbt.connect.model.IProject;

import java.net.MalformedURLException;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: fred
 * Date: 8 juin 2010
 * Time: 22:36:24
 * To change this template use File | Settings | File Templates.
 */
public class MantisManagerComponent extends AbstractProjectComponent {

    private MantisSession session;
    private Collection<IProject> availableProjects;


    public MantisManagerComponent(Project project, MantisSession session) {
        super(project);
        this.session = session;
    }

    public MantisProjectSettings getSettings() {
        return MantisSettingsComponent.getInstance(myProject).getState();
    }

    public MantisSession getSession() {
        return session;
    }

    public Collection<IProject> getAvailableProjects() {
        return availableProjects;
    }

    @Override
    public void initComponent() {

        StartupManager.getInstance(myProject).registerStartupActivity(new Runnable() {
            @Override
            public void run() {
                MantisProjectSettings settings = MantisSettingsComponent.getInstance(myProject).getState();

                try {
                    session.open(settings.getUrl(), settings.getUsername(), settings.getPassword());
                    session.setProject(settings.getProjectId());
                    if (session.isConnected()) {
                        availableProjects = session.getAvailableProjects();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (MCException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        });
    }

    public static MantisManagerComponent getInstance(Project project) {
        return project.getComponent(MantisManagerComponent.class);
    }
}
