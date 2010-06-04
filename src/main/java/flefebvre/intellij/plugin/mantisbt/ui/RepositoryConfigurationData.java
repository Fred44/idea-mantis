package flefebvre.intellij.plugin.mantisbt.ui;

import org.mantisbt.connect.model.IProject;

/**
 * Created by IntelliJ IDEA.
 * User: flefebvre
 * Date: 8 janv. 2010
 * Time: 21:18:55
 * To change this template use File | Settings | File Templates.
 */
public class RepositoryConfigurationData {

    private String name;

    private String url;

    private String username;

    private String password;

    private Long project;

    private IProject[] availableProjects = new IProject[0];

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

    public Long getProject() {
        return project;
    }

    public void setProject(Long project) {
        this.project = project;
    }

    public IProject[] getAvailableProjects() {
        return availableProjects;
    }

    public void setAvailableProjects(IProject[] availableProjects) {
        this.availableProjects = availableProjects;
    }
}