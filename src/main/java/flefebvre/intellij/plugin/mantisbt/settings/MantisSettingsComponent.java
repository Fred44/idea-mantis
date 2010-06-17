package flefebvre.intellij.plugin.mantisbt.settings;

import com.intellij.openapi.components.AbstractProjectComponent;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: fred
 * Date: 8 juin 2010
 * Time: 22:31:09
 * To change this template use File | Settings | File Templates.
 */
@State(name = "MantisProjectSettings", storages = {@Storage(id = "default", file = "$WORKSPACE_FILE$")})
public class MantisSettingsComponent extends AbstractProjectComponent implements PersistentStateComponent<MantisProjectSettings> {
    private MantisProjectSettings mySettings = new MantisProjectSettings();

    private List<SettingsListener> listeners = new ArrayList<SettingsListener>();

    public static MantisSettingsComponent getInstance(Project project) {
        return project.getComponent(MantisSettingsComponent.class);
    }

    public MantisSettingsComponent(Project project) {
        super(project);
    }

    public MantisProjectSettings getState() {
        return mySettings;
    }

    public void loadState(MantisProjectSettings state) {
        mySettings = state;
    }

    public void addListener(SettingsListener listener) {
        listeners.add(listener);
    }

    public void removeListener(SettingsListener listener) {
        listeners.remove(listener);
    }
}