package fred.intellij.plugin.mantisbt.model;

/**
 * Created by IntelliJ IDEA.
 * User: fred
 * Date: 19 janv. 2010
 * Time: 22:59:35
 * To change this template use File | Settings | File Templates.
 */
public class RepositoryConfiguration {

    private String name;

    private String url;

    private String username;

    private String password;

    private long project;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getProject() {
        return project;
    }

    public void setProject(long project) {
        this.project = project;
    }
}