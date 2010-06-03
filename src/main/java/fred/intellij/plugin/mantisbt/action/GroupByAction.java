package fred.intellij.plugin.mantisbt.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.actionSystem.ex.CustomComponentAction;
import fred.intellij.plugin.mantisbt.model.MantisGroupBy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by IntelliJ IDEA.
 * User: fred
 * Date: 13 avr. 2010
 * Time: 22:47:31
 * To change this template use File | Settings | File Templates.
 */
public class GroupByAction extends AnAction implements CustomComponentAction {
    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public JComponent createCustomComponent(Presentation presentation) {
        JPanel groupByPane = new JPanel();
        FlowLayout layout = new FlowLayout();
        layout.setVgap(0);
        groupByPane.setLayout(layout);
        JLabel groupByLabel = new JLabel("Group by");
        final JComboBox groupByCombo = new JComboBox(MantisGroupBy.values());
        groupByPane.add(groupByLabel);
        groupByPane.add(groupByCombo);

        presentation.putClientProperty(GroupByAction.class.getName() + ".combo", groupByCombo);

        groupByCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                groupByCombo.getSelectedItem();
            }
        });

        return groupByPane;
    }
}