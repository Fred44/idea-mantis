package flefebvre.intellij.plugin.mantisbt;

import com.intellij.openapi.components.AbstractProjectComponent;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupManager;
import flefebvre.intellij.plugin.mantisbt.settings.MantisProjectSettings;
import flefebvre.intellij.plugin.mantisbt.settings.MantisSettingsComponent;
import flefebvre.intellij.plugin.mantisbt.settings.SettingsListener;
import org.mantisbt.connect.MCException;
import org.mantisbt.connect.model.IFilter;
import org.mantisbt.connect.model.IIssue;
import org.mantisbt.connect.model.IProject;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: fred
 * Date: 8 juin 2010
 * Time: 22:36:24
 * To change this template use File | Settings | File Templates.
 */
@State(name = "MantisManager",
        storages = {@Storage(id = "default", file = "$WORKSPACE_FILE$")})
public class MantisManagerComponent extends AbstractProjectComponent implements PersistentStateComponent<MantisState> {

    private MantisSession session;

    private List<IProject> availableProjects;
    private List<IIssue> issues;
    private List<IFilter> availableFilters;
    private IFilter activeFilter;

    private List<Listener> listeners;

    private MantisState state;

    private boolean isInitialized = false;

    public MantisManagerComponent(Project project) {
        super(project);

        listeners = new ArrayList<Listener>();
        this.session = new MantisSessionImpl();
        this.state = new MantisState();
    }

    public void addListener(Listener listener) {
        this.listeners.add(listener);
    }

    public IFilter getActiveFilter() {
        return activeFilter;
    }

    public void setActiveFilter(IFilter activeFilter) {
        this.activeFilter = activeFilter;
        this.state.activeFilterId = activeFilter.getId();
        update();
    }

    public List<IFilter> getAvailableFilters() {
        if (availableFilters == null) {
            new ArrayList<IFilter>(0);
        }
        return availableFilters;
    }

    public List<IProject> getAvailableProjects() {
        return availableProjects;
    }

    public void update() {
        new Task.Backgroundable(myProject, "Loading issues ...", true) {
            public void run(ProgressIndicator indicator) {
                indicator.setText("Loading issues");
                indicator.setFraction(0.0);
                loadIssues();
                indicator.setFraction(1.0);
            }

            @Override
            public void onSuccess() {
                fireIssuesReloaded();
            }
        }.setCancelText("Stop loading").queue();
    }

    public List<IIssue> getIssues() {
        return issues;
    }

    private void loadIssues() {
        if (activeFilter == null) {
            // Error
//            issues = new ArrayList<IIssue>(0);
        } else {
            try {
                issues = session.getIssues(activeFilter.getId());
            } catch (MCException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    @Override
    public void initComponent() {
        MantisSettingsComponent.getInstance(myProject).addListener(new SettingsListener() {

            @Override
            public void settingsChanged(MantisProjectSettings newSettings) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });

        StartupManager.getInstance(myProject).registerStartupActivity(new Runnable() {
            @Override
            public void run() {
                MantisProjectSettings settings = MantisSettingsComponent.getInstance(myProject).getState();

                try {
                    session.open(settings.getUrl(), settings.getUsername(), settings.getPassword());
                    session.setProject(settings.getProjectId());
                    if (session.isConnected()) {
                        availableProjects = session.getAvailableProjects();
                        availableFilters = session.getFilters();
                        if (state.activeFilterId != null) {
                            activeFilter = session.getFilter(state.activeFilterId);
                        }
                    }
//                    isInitialized = true;
                } catch (MalformedURLException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (MCException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        });
    }

    private void fireIssuesReloaded() {
        for (Listener listener : listeners) {
            listener.issuesReloaded(issues);
        }
    }

    private void fireFilterChanged() {
        for (Listener listener : listeners) {
            listener.filterChanged(activeFilter);
        }
    }

    public static MantisManagerComponent getInstance(Project project) {
        return project.getComponent(MantisManagerComponent.class);
    }

    @Override
    public MantisState getState() {
        return state;
    }

    @Override
    public void loadState(MantisState mantisState) {
        this.state = mantisState;

        if (!isInitialized) {
            isInitialized = true;
            StartupManager.getInstance(myProject).registerPostStartupActivity(new Runnable() {

                @Override
                public void run() {
                    loadIssues();
                }
            });
        }
    }

    public interface Listener extends EventListener {

        void issuesReloaded(List<IIssue> issues);

        void filterChanged(IFilter filter);
    }
}
