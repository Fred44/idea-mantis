package flefebvre.intellij.plugin.mantisbt;

import flefebvre.intellij.plugin.mantisbt.model.RepositoryConfiguration;

/**
 * Created by IntelliJ IDEA.
 * User: fred
 * Date: 4 juin 2010
 * Time: 19:36:39
 * To change this template use File | Settings | File Templates.
 */
public interface ConfigurationListener {

    void configurationChange(RepositoryConfiguration oldCfg, RepositoryConfiguration newCfg);
}
