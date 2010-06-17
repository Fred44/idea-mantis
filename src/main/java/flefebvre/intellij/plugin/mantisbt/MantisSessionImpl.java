package flefebvre.intellij.plugin.mantisbt;

import org.mantisbt.connect.IMCSession;
import org.mantisbt.connect.MCException;
import org.mantisbt.connect.axis.MCSession;
import org.mantisbt.connect.model.IFilter;
import org.mantisbt.connect.model.IIssue;
import org.mantisbt.connect.model.IProject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: flefebvre
 * Date: 12 avr. 2010
 * Time: 19:57:58
 * To change this template use File | Settings | File Templates.
 */
public class MantisSessionImpl implements MantisSession {

    private IMCSession session;

    private long prj;

    public void setProject(long prjId) {
        this.prj = prjId;
    }

    public void open(String url, String username, String pass) throws MalformedURLException, MCException {
        URL mantisUrl = new URL(url);
        session = new MCSession(mantisUrl, username, pass);
    }

    @Override
    public List<IProject> getAvailableProjects() throws MCException {
        return Arrays.asList(session.getAccessibleProjects());
    }

    public String getVersion() throws MCException {
        return session.getVersion();
    }

    public IFilter getFilter(long id) throws MCException {
        for (IFilter filter : session.getFilters(prj)) {
            if (filter.getId() == id) {
                return filter;
            }
        }
        return null;
    }

    public List<IFilter> getFilters() throws MCException {
        return Arrays.asList(session.getFilters(prj));
    }

    public List<IIssue> getIssues(long filter) throws MCException {
        return Arrays.asList(session.getIssues(prj, filter));
    }

    public boolean isConnected() {
        return session != null;
    }

    @Override
    public void clear() {
        session.flush();
    }
}