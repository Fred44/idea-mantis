package flefebvre.intellij.plugin.mantisbt;

import org.mantisbt.connect.MCException;
import org.mantisbt.connect.model.IFilter;
import org.mantisbt.connect.model.IIssue;
import org.mantisbt.connect.model.IProject;

import java.net.MalformedURLException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: flefebvre
 * Date: 12 avr. 2010
 * Time: 19:55:18
 * To change this template use File | Settings | File Templates.
 */
public interface MantisSession {

    void setProject(long prjId);

    void open(String url, String username, String pass) throws MalformedURLException, MCException;

    List<IProject> getAvailableProjects() throws MCException;

    String getVersion() throws MCException;

    IFilter getFilter(long id) throws MCException;

    List<IFilter> getFilters() throws MCException;

    List<IIssue> getIssues(long filter) throws MCException;

    boolean isConnected();

    void clear();
}