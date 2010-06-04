package flefebvre.intellij.plugin.mantisbt.ui;

import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import flefebvre.intellij.plugin.mantisbt.MantisConfigurationManager;
import org.jetbrains.annotations.NotNull;
import org.mantisbt.connect.MCException;
import org.mantisbt.connect.model.IProject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created by IntelliJ IDEA.
 * User: flefebvre
 * Date: 8 janv. 2010
 * Time: 15:33:55
 * To change this template use File | Settings | File Templates.
 */
public class MantisConfigurationForm {
    private JTextField urlField;
    private JPanel rootPanel;
    private JLabel urlLabel;
    private JTextField userField;
    private JTextField passwordField;
    private JLabel userLabel;
    private JLabel passwordLabel;
    private JButton testConnectionButton;
    private JTextField repoNameField;
    private JLabel repoNameLabel;
    private JComboBox projectList;
    private JLabel projectLabel;
    private JTextArea projectDescField;
    private JList filterList;

    private MantisConfigurationManager configMgr;
    private Project project;
    private RepositoryConfigurationData cfgData;

    public MantisConfigurationForm(Project project, MantisConfigurationManager cfgMgr, RepositoryConfigurationData cfgData) {
        this.configMgr = cfgMgr;
        this.project = project;
        this.cfgData = cfgData;

        projectList.setRenderer(new ProjectRenderer());

        testConnectionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                Task.Modal openTask = new Task.Modal(MantisConfigurationForm.this.project, "Mantis", false) {
                    @Override
                    public void run(@NotNull ProgressIndicator progressIndicator) {
                        try {
                            configMgr.openConnection(MantisConfigurationForm.this.cfgData.getUrl(), MantisConfigurationForm.this.cfgData.getUsername(), MantisConfigurationForm.this.cfgData.getPassword());
                            configMgr.refreshProjectsList();
                        } catch (MCException mce) {
                            UiUtil.showError("Mantis failure", mce.getMessage());
                        }
                    }

                    @Override
                    public void onSuccess() {
                        setData();
                    }
                };
                ProgressManager.getInstance().run(openTask);
            }
        });

        projectList.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                IProject prj = (IProject) itemEvent.getItem();
                projectDescField.setText(prj.getDescription());
            }
        });
    }

    public JComponent getRootComponent() {
        return rootPanel;
    }

    public void setData() {
        urlField.setText(cfgData.getUrl());
        userField.setText(cfgData.getUsername());
        passwordField.setText(cfgData.getPassword());
        repoNameField.setText(cfgData.getName());
        projectList.setModel(new DefaultComboBoxModel(cfgData.getAvailableProjects()));
        if (cfgData.getProject() != null) {
            for (IProject prj : cfgData.getAvailableProjects()) {
                if (prj.getId() == cfgData.getProject()) {
                    projectList.setSelectedItem(prj);
                    projectDescField.setText(prj.getDescription());
                }
            }
        }
    }

    public void getData() {
        cfgData.setUrl(urlField.getText());
        cfgData.setUsername(userField.getText());
        cfgData.setPassword(passwordField.getText());
        cfgData.setName(repoNameField.getText());
        IProject prj = (IProject) projectList.getSelectedItem();
        if (prj != null) {
            cfgData.setProject(prj.getId());
        } else {
            cfgData.setProject(null);
        }
    }

    class ProjectRenderer extends DefaultListCellRenderer {
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