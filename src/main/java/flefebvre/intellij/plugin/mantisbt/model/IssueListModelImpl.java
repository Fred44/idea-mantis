package flefebvre.intellij.plugin.mantisbt.model;

import org.mantisbt.connect.model.IIssue;

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

    private Set<IIssue> issues;

    public IssueListModelImpl() {
        this.issues = new HashSet<IIssue>();
    }

    public Collection<IIssue> getIssues() {
        return issues;
    }

    public void addIssues(Collection<IIssue> issues) {
        this.issues.addAll(issues);
    }

    public IIssue findIssue(Long id) {
        IIssue foundIssue = null;

        for (IIssue is : issues) {
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
}