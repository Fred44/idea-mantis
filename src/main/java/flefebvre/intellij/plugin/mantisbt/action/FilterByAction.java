package flefebvre.intellij.plugin.mantisbt.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.actionSystem.ex.CustomComponentAction;
import flefebvre.intellij.plugin.mantisbt.model.IssueListManager;
import flefebvre.intellij.plugin.mantisbt.model.event.FilterListListener;
import flefebvre.intellij.plugin.mantisbt.ui.UiUtil;
import org.mantisbt.connect.MCException;
import org.mantisbt.connect.model.IFilter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: flefebvre
 * Date: 13 avr. 2010
 * Time: 22:47:31
 * To change this template use File | Settings | File Templates.
 */
public class FilterByAction extends AnAction implements CustomComponentAction, ItemListener {

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
    }

    public JComponent createCustomComponent(Presentation presentation) {

        JPanel filterByPane = new JPanel();
        FlowLayout layout = new FlowLayout();
        layout.setVgap(0);
        filterByPane.setLayout(layout);
        JLabel filterByLabel = new JLabel("Filter by");
        final JComboBox filterByCombo = new JComboBox(new DefaultComboBoxModel());
        filterByCombo.setRenderer(new FilterRenderer());
        filterByPane.add(filterByLabel);
        filterByPane.add(filterByCombo);

        presentation.putClientProperty(FilterByAction.class.getName() + ".combo", filterByCombo);

        IssueListManager issuesMgr = ActionUtil.getComponentInstanceOfOnCurProject(IssueListManager.class);
        if (issuesMgr != null) {
            filterByCombo.setModel(new DefaultComboBoxModel(issuesMgr.getFilterListModel().getFilters().toArray()));
            issuesMgr.getFilterListModel().addFilterListListener(new FilterListListener() {
                @Override
                public void filterListChange(final Collection<IFilter> filterLst, final IFilter active) {
                    EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            filterByCombo.setModel(new DefaultComboBoxModel(filterLst.toArray()));
                            filterByCombo.setSelectedItem(active);
                        }
                    });
                }
            });
        }
        filterByCombo.addItemListener(this);

        return filterByPane;
    }

    @Override
    public void itemStateChanged(ItemEvent itemEvent) {
        IFilter filter = (IFilter) itemEvent.getItem();
        if (filter != null) {
            IssueListManager issueLstMgr = ActionUtil.getComponentInstanceOfOnCurProject(IssueListManager.class);
            if (issueLstMgr != null) {
                try {
                    issueLstMgr.setActiveFilter(filter);
                } catch (MCException e) {
                    UiUtil.showError("Mantis failure", e.getMessage());
                }
            }
        }
    }

    class FilterRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList jList, Object o, int i, boolean b, boolean b1) {
            IFilter filter = (IFilter) o;
            JLabel label = (JLabel) super.getListCellRendererComponent(jList, o, i, b, b1);
            if (filter != null) {
                label.setText(filter.getName());
            }
            return label;
        }
    }
}