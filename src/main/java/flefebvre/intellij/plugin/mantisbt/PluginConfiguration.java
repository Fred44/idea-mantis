package flefebvre.intellij.plugin.mantisbt;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import flefebvre.intellij.plugin.mantisbt.model.RepositoryConfiguration;
import org.jetbrains.annotations.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: flefebvre
 * Date: 1 avr. 2010
 * Time: 20:21:12
 * To change this template use File | Settings | File Templates.
 */
@State(name = "idea-mantis-plugin",
        storages = {@Storage(id = "idea-mantis-plugin-id", file = "$PROJECT_CONFIG_DIR$/idea-mantis-plugin.xml")})
public class PluginConfiguration extends RepositoryConfiguration implements ProjectComponent, PersistentStateComponent<PluginConfiguration> {

    public PluginConfiguration getState() {
        return this;
    }

    public void loadState(PluginConfiguration pluginConfig) {
        setName(pluginConfig.getName());
        setUrl(pluginConfig.getUrl());
        setUsername(pluginConfig.getUsername());
        setPassword(pluginConfig.getPassword());
        setProject(pluginConfig.getProject());
    }

    public void projectOpened() {
    }

    public void projectClosed() {
    }

    @NotNull
    public String getComponentName() {
        return getClass().getCanonicalName();
    }

    public void initComponent() {
    }

    public void disposeComponent() {
    }
}