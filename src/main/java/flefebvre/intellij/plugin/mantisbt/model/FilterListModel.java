package flefebvre.intellij.plugin.mantisbt.model;

import flefebvre.intellij.plugin.mantisbt.model.event.FilterListListener;
import org.mantisbt.connect.model.IFilter;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: fred
 * Date: 5 juin 2010
 * Time: 00:21:11
 * To change this template use File | Settings | File Templates.
 */
public class FilterListModel {

    private Set<IFilter> filters;
    private IFilter activeFilter;

    private List<FilterListListener> fListeners;

    public FilterListModel() {
        this.filters = new HashSet<IFilter>();
        activeFilter = null;
        this.fListeners = new ArrayList<FilterListListener>();
    }

    public void setFilters(Collection<IFilter> filters) {
        this.filters.clear();
        this.filters.addAll(filters);

        if (filters.isEmpty()) {
            activeFilter = null;
        } else if (activeFilter == null) {
            activeFilter = filters.iterator().next();
        } else {
            long filId = activeFilter.getId();
            activeFilter = filters.iterator().next();
            for (IFilter filter : filters) {
                if (filter.getId() == filId) {
                    activeFilter = filter;
                    break;
                }
            }
        }

        notifyListeners();
    }

    public void setActiveFilter(IFilter filter) {
        this.activeFilter = filter;
    }

    public IFilter getActiveFilter() {
        return activeFilter;
    }

    public void clear() {
        filters.clear();
        notifyListeners();
    }

    public Collection<IFilter> getFilters() {
        return filters;
    }

    public void addFilterListListener(FilterListListener fListener) {
        fListeners.add(fListener);
    }

    public void removeFilterListListener(FilterListListener fListener) {
        fListeners.remove(fListener);
    }

    private void notifyListeners() {
        for (FilterListListener fl : fListeners) {
            fl.filterListChange(filters, activeFilter);
        }
    }
}
