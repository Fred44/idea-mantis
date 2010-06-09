package flefebvre.intellij.plugin.mantisbt.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.actionSystem.ex.CustomComponentAction;
import flefebvre.intellij.plugin.mantisbt.model.MantisGroupBy;
import flefebvre.intellij.plugin.mantisbt.ui.IssueListPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created by IntelliJ IDEA.
 * User: flefebvre
 * Date: 13 avr. 2010
 * Time: 22:47:31
 * To change this template use File | Settings | File Templates.
 */
public class GroupByAction extends AnAction implements CustomComponentAction, ItemListener {
    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
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

        groupByCombo.addItemListener(this);

        return groupByPane;
    }

    @Override
    public void itemStateChanged(ItemEvent itemEvent) {
        MantisGroupBy groupBy = (MantisGroupBy) itemEvent.getItem();
        IssueListPanel issueLstPanel = ActionUtil.getComponentInstanceOfOnCurProject(IssueListPanel.class);
        if (issueLstPanel != null) {
            issueLstPanel.setGroupBy(groupBy);
        }
    }
}