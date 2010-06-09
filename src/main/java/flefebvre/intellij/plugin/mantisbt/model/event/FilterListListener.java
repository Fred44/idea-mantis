package flefebvre.intellij.plugin.mantisbt.model.event;

import org.mantisbt.connect.model.IFilter;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: fred
 * Date: 5 juin 2010
 * Time: 00:43:24
 * To change this template use File | Settings | File Templates.
 */
public interface FilterListListener {

    void filterListChange(Collection<IFilter> filterLst, IFilter active);
}
