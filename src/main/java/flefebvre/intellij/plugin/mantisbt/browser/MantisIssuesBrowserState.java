package flefebvre.intellij.plugin.mantisbt.browser;

import flefebvre.intellij.plugin.mantisbt.model.MantisGroupBy;
import org.mantisbt.connect.model.IFilter;

/**
 * Created by IntelliJ IDEA.
 * User: fred
 * Date: 8 juin 2010
 * Time: 18:18:57
 * To change this template use File | Settings | File Templates.
 */
public class MantisIssuesBrowserState {

    public IFilter activeFilter;

    public MantisGroupBy groupBy;
}
