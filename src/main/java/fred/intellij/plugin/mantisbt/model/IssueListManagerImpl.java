package fred.intellij.plugin.mantisbt.model;

import fred.intellij.plugin.mantisbt.MantisSession;
import org.mantisbt.connect.MCException;
import org.mantisbt.connect.model.IFilter;

/**
 * Created by IntelliJ IDEA.
 * User: fred
 * Date: 30 mars 2010
 * Time: 19:23:05
 * To change this template use File | Settings | File Templates.
 */
public class IssueListManagerImpl implements IssueListManager {

    private IssueListModel issueModel;
    private IFilter activeFilter;

    private MantisSession session;
    private RepositoryConfiguration repoConfig;

    public IssueListManagerImpl(MantisSession session, RepositoryConfiguration repoConfig) {
        this.session = session;
        this.repoConfig = repoConfig;
        this.issueModel = new IssueListModelImpl();
    }

    public void loadIssues() throws MCException {

        if (session == null && issueModel != null) {

            issueModel.clear();
        } else if (issueModel != null && activeFilter != null) {
            issueModel.addIssues(session.getIssues(activeFilter.getId()));
        }
    }

    public IssueListModel getListModel() {
        return issueModel;
    }

    public void setListModel(IssueListModel model) {
        this.issueModel = model;
    }

    public void setActiveFilter(IFilter filter) throws MCException {
        this.activeFilter = filter;
        loadIssues();
    }

    public IFilter getActiveFilter() {
        return activeFilter;
    }
}