package flefebvre.intellij.plugin.mantisbt.model;

import org.mantisbt.connect.MCException;
import org.mantisbt.connect.model.IFilter;

/**
 * Created by IntelliJ IDEA.
 * User: flefebvre
 * Date: 30 mars 2010
 * Time: 19:16:12
 * To change this template use File | Settings | File Templates.
 */
public interface IssueListManager {

    void loadIssues() throws MCException;

    IssueListModel getListModel();

    void setListModel(IssueListModel model);

    void setActiveFilter(IFilter filter) throws MCException;

    IFilter getActiveFilter();
}