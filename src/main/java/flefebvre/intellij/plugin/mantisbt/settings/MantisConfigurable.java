package flefebvre.intellij.plugin.mantisbt.settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import flefebvre.intellij.plugin.mantisbt.MantisSession;
import flefebvre.intellij.plugin.mantisbt.MantisSessionImpl;
import flefebvre.intellij.plugin.mantisbt.ui.UiUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.mantisbt.connect.MCException;
import org.mantisbt.connect.model.IProject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.MalformedURLException;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: fred
 * Date: 8 juin 2010
 * Time: 18:49:10
 * To change this template use File | Settings | File Templates.
 */
public abstract class MantisConfigurable implements Configurable {
    private JTextField urlField;
    private JLabel urlLabel;
    private JLabel usernameLabel;
    private JTextField usernameField;
    private JLabel passwordLabel;
    private JPasswordField passwordField;
    private JLabel projectLabel;
    private JComboBox projectCB;
    private JPanel projectDescPanel;
    private JTextArea projectDescField;
    private JPanel rootPanel;
    private JButton openBt;

    private DefaultComboBoxModel projectListModel;

    private Project myProject;
    private MantisSession localSession;

    public MantisConfigurable(Project project, Collection<IProject> availableProjects) {
        myProject = project;
        localSession = new MantisSessionImpl();

        projectListModel = new DefaultComboBoxModel();
        projectCB.setModel(projectListModel);
        loadProjectList(availableProjects);
        projectCB.setRenderer(new ProjectListCellRenderer());
        projectCB.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                IProject selected = (IProject) itemEvent.getItem();
                projectDescField.setText(selected == null ? "" : selected.getDescription());
            }
        });

        openBt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ProgressManager.getInstance().run(new Task.Modal(myProject, "Loading Mantis data ...", false) {
                    private Collection<IProject> projects;

                    @Override
                    public void run(@NotNull ProgressIndicator progressIndicator) {
                        try {
                            localSession.open(urlField.getText(), usernameField.getText(), passwordField.getText());
                            projects = localSession.getAvailableProjects();
                        } catch (MalformedURLException urle) {
                            UiUtil.showError("Incorrect configuration", "URL is badly formed !");

                        } catch (MCException mce) {
                            UiUtil.showError("Mantis failure", mce.getMessage());
                        }
                    }

                    @Override
                    public void onSuccess() {
                        loadProjectList(projects);
                    }
                });
            }
        });
    }

    private void loadProjectList(Collection<IProject> projects) {
        IProject selected = (IProject) projectListModel.getSelectedItem();

        projectListModel.removeAllElements();

        if (projects != null) {
            for (IProject prj : projects) {
                projectListModel.addElement(prj);

                if (selected != null && prj.getId() == selected.getId()) {
                    projectListModel.setSelectedItem(prj);
                    projectDescField.setText(prj.getDescription());
                }
            }
        }
    }

    private void selectProject(long prjId) {
        for (int i = 0; i < projectListModel.getSize(); i++) {
            IProject prj = (IProject) projectListModel.getElementAt(i);
            if (prj.getId() == prjId) {
                projectListModel.setSelectedItem(prj);
                projectDescField.setText(prj.getDescription());
            }
        }
    }

    private Long getSelectedProject() {
        IProject selected = (IProject) projectListModel.getSelectedItem();

        return selected == null ? null : selected.getId();
    }

    protected abstract MantisProjectSettings getState();

    @Nls
    @Override
    public String getDisplayName() {
        return "Mantis settings";
    }

    @Override
    public Icon getIcon() {
        return null;
    }

    @Override
    public String getHelpTopic() {
        return null;
    }

    @Override
    public JComponent createComponent() {
        return rootPanel;
    }

    @Override
    public boolean isModified() {
        MantisProjectSettings formData = new MantisProjectSettings();
        setData(formData);
        return !formData.equals(getState());
    }

    @Override
    public void apply() throws ConfigurationException {
        setData(getState());
    }

    @Override
    public void reset() {
        getData(getState());
    }

    @Override
    public void disposeUIResources() {
    }

    private void setData(MantisProjectSettings data) {
        data.setUrl(urlField.getText());
        data.setUsername(usernameField.getText());
        data.setPassword(passwordField.getText());
        data.setProjectId(getSelectedProject());
    }

    private void getData(MantisProjectSettings data) {
        urlField.setText(data.getUrl());
        usernameField.setText(data.getUsername());
        passwordField.setText(data.getPassword());
        if (data.getProjectId() != null) {
            selectProject(data.getProjectId());
        }
    }

    class ProjectListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList jList, Object o, int i, boolean b, boolean b1) {
            IProject prj = (IProject) o;
            JLabel label = (JLabel) super.getListCellRendererComponent(jList, o, i, b, b1);
            if (prj != null) {
                label.setText(prj.getName());
            }
            return label;
        }
    }
}
