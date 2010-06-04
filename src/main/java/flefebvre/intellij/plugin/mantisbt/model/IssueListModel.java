package flefebvre.intellij.plugin.mantisbt.model;

import org.mantisbt.connect.model.IIssue;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: flefebvre
 * Date: 29 mars 2010
 * Time: 22:49:14
 * To change this template use File | Settings | File Templates.
 */
public interface IssueListModel {

    Collection<IIssue> getIssues();

    void addIssues(Collection<IIssue> issues);

    IIssue findIssue(Long id);

    void clear();
}