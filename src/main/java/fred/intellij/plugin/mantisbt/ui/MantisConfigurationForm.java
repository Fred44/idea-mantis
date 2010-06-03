package fred.intellij.plugin.mantisbt.ui;

import fred.intellij.plugin.mantisbt.MantisConfigurationManager;
import org.mantisbt.connect.model.IProject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by IntelliJ IDEA.
 * User: fred
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
    private JList filterList;

    private MantisConfigurationManager configMgr;

    public MantisConfigurationForm(MantisConfigurationManager cfgMgr) {
        this.configMgr = cfgMgr;

        testConnectionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                configMgr.onOpenConnectionClick();
            }
        });
    }

    public JComponent getRootComponent() {
        return rootPanel;
    }

    public void setData(RepositoryConfigurationData data) {
        urlField.setText(data.getUrl());
        userField.setText(data.getUsername());
        passwordField.setText(data.getPassword());
        repoNameField.setText(data.getName());
        projectList.setModel(new DefaultComboBoxModel(data.getAvailableProjects()));
        projectList.setSelectedItem(data.getProject());
    }

    public void getData(RepositoryConfigurationData data) {
        data.setUrl(urlField.getText());
        data.setUsername(userField.getText());
        data.setPassword(passwordField.getText());
        data.setName(repoNameField.getText());
        data.setProject(((IProject) projectList.getSelectedItem()).getId());
    }
}