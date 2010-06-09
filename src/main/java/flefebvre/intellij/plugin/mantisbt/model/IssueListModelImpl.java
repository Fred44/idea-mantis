package flefebvre.intellij.plugin.mantisbt.model;

import flefebvre.intellij.plugin.mantisbt.model.event.IssueListListener;
import org.mantisbt.connect.model.IIssueHeader;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: flefebvre
 * Date: 29 mars 2010
 * Time: 22:53:26
 * To change this template use File | Settings | File Templates.
 */
public class IssueListModelImpl implements IssueListModel {

    private Set<IIssueHeader> issues;

    private Set<IssueListListener> listeners;

    public IssueListModelImpl() {
        this.issues = new HashSet<IIssueHeader>();
        this.listeners = new HashSet<IssueListListener>();
    }

    public Collection<IIssueHeader> getIssues() {
        return issues;
    }

    public void addIssues(Collection<IIssueHeader> issues) {
        this.issues.addAll(issues);
    }

    public IIssueHeader findIssue(Long id) {
        IIssueHeader foundIssue = null;

        for (IIssueHeader is : issues) {
            if (is.getId() == id) {
                foundIssue = is;
                break;
            }
        }

        return foundIssue;
    }

    public void clear() {
        issues.clear();
    }

    @Override
    public void addIssueListListener(IssueListListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeIssueListListener(IssueListListener listener) {
        listeners.remove(listener);
    }
}