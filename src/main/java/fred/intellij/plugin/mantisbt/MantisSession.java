package fred.intellij.plugin.mantisbt;

import org.mantisbt.connect.MCException;
import org.mantisbt.connect.model.IFilter;
import org.mantisbt.connect.model.IIssue;

import java.net.MalformedURLException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: fred
 * Date: 12 avr. 2010
 * Time: 19:55:18
 * To change this template use File | Settings | File Templates.
 */
public interface MantisSession {

    void setProject(long prjId);

    void open(String url, String username, String pass) throws MalformedURLException, MCException;

    String getVersion() throws MCException;

    List<IFilter> getFilters() throws MCException;

    List<IIssue> getIssues(long filter) throws MCException;

    boolean isConnected();
}