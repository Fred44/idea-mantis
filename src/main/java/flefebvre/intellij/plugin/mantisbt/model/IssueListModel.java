package flefebvre.intellij.plugin.mantisbt.model;

import flefebvre.intellij.plugin.mantisbt.model.event.IssueListListener;
import org.mantisbt.connect.model.IIssueHeader;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: flefebvre
 * Date: 29 mars 2010
 * Time: 22:49:14
 * To change this template use File | Settings | File Templates.
 */
public interface IssueListModel {

    Collection<IIssueHeader> getIssues();

    void addIssues(Collection<IIssueHeader> issues);

    IIssueHeader findIssue(Long id);

    void clear();

    void addIssueListListener(IssueListListener listener);

    void removeIssueListListener(IssueListListener listener);
}