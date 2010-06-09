package flefebvre.intellij.plugin.mantisbt.model;

import flefebvre.intellij.plugin.mantisbt.ConfigurationListener;
import flefebvre.intellij.plugin.mantisbt.MantisConfigurationManager;
import flefebvre.intellij.plugin.mantisbt.MantisSession;
import flefebvre.intellij.plugin.mantisbt.ui.UiUtil;
import org.mantisbt.connect.MCException;
import org.mantisbt.connect.model.IFilter;

import java.net.MalformedURLException;

/**
 * Created by IntelliJ IDEA.
 * User: flefebvre
 * Date: 30 mars 2010
 * Time: 19:23:05
 * To change this template use File | Settings | File Templates.
 */
public class IssueListManagerImpl implements IssueListManager, ConfigurationListener {

    private IssueListModel issueModel;
    private FilterListModel filterModel;

    private MantisSession session;

    public IssueListManagerImpl(MantisSession session, MantisConfigurationManager configMgr) {
        this.session = session;
        this.issueModel = new IssueListModelImpl();
        this.filterModel = new FilterListModel();

        configMgr.addConfigurationListener(this);
    }

    @Override
    public void init() throws MCException {
        filterModel.setActiveFilter(null);
        issueModel.clear();
        filterModel.clear();

        if (session != null && session.isConnected()) {
            loadFilters();
            loadIssues();
        }
    }

    public void loadIssues() throws MCException {
        if (issueModel != null && filterModel.getActiveFilter() != null) {
            issueModel.clear();
            issueModel.addIssues(session.getIssues(filterModel.getActiveFilter().getId()));
        }
    }

    public void loadFilters() throws MCException {
        if (session == null && filterModel != null) {

            filterModel.clear();
            issueModel.clear();
        } else if (filterModel != null) {
            filterModel.setFilters(session.getFilters());
        }
    }

    @Override
    public void clearCache() {
        session.clear();
    }

    public IssueListModel getListModel() {
        return issueModel;
    }

    public void setListModel(IssueListModel model) {
        this.issueModel = model;
    }

    public void setActiveFilter(IFilter filter) throws MCException {
        this.getFilterListModel().setActiveFilter(filter);
        loadIssues();
    }

    public IFilter getActiveFilter() {
        return filterModel.getActiveFilter();
    }

    public FilterListModel getFilterListModel() {
        return filterModel;
    }

    public void setFilterListModel(FilterListModel filterModel) {
        this.filterModel = filterModel;
    }

    @Override
    public void configurationChange(RepositoryConfiguration oldCfg, RepositoryConfiguration newCfg) {
        try {
            session.open(newCfg.getUrl(), newCfg.getUsername(), newCfg.getPassword());
            session.setProject(newCfg.getProject());
            init();
        } catch (MalformedURLException e) {
            e.printStackTrace(); // not possible
        } catch (MCException e) {
            UiUtil.showError("Mantis failure", e.getMessage());
        }
    }
}