package flefebvre.intellij.plugin.mantisbt.settings;

/**
 * Created by IntelliJ IDEA.
 * User: fred
 * Date: 12 juin 2010
 * Time: 23:11:57
 * To change this template use File | Settings | File Templates.
 */
public interface SettingsListener {

    void settingsChanged(MantisProjectSettings newSettings);
}
