package flefebvre.intellij.plugin.mantisbt.browser;

import com.intellij.ide.util.treeView.NodeRenderer;
import com.intellij.util.ui.UIUtil;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;

public class IssueTreeRenderer implements TreeCellRenderer {

    private TreeCellRenderer baseRenderer;

    private JPanel panel;
    private JLabel priority;
    private JLabel author;
    private JLabel lastUpdateDate;
    private JLabel status;
    private JLabel notes;

    public IssueTreeRenderer() {
        this(null);
    }

    public IssueTreeRenderer(TreeCellRenderer baseRenderer) {
        if (baseRenderer == null) {
            baseRenderer = new NodeRenderer();
        }
        this.baseRenderer = baseRenderer;

        priority = new JLabel();
        author = new JLabel();
        lastUpdateDate = new JLabel();
        status = new JLabel();
        notes = new JLabel();

        //                                                                      summary                                 author                      status              notes           priority            updDate
        panel = new JPanel(new FormLayout("1dlu, fill:default:grow, 8dlu, right:50dlu, 8dlu, 45dlu, 8dlu, 24dlu, 8dlu, 8dlu, 8dlu, 70dlu, 1dlu", "pref"));
        CellConstraints cc = new CellConstraints();

        panel.add(author, cc.xy(4, 1));
        panel.add(status, cc.xy(6, 1));
        panel.add(notes, cc.xy(8, 1));
        panel.add(priority, cc.xy(10, 1));
        panel.add(lastUpdateDate, cc.xy(12, 1));

    }

    public Component getTreeCellRendererComponent(final JTree tree,
                                                  final Object value,
                                                  final boolean selected,
                                                  final boolean expanded,
                                                  final boolean leaf,
                                                  final int row,
                                                  final boolean hasFocus) {

        final Component baseComponent = baseRenderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

        final Object userObject = ((DefaultMutableTreeNode) value).getUserObject();

        if (userObject instanceof MantisStructure.IssueNode) {
            MantisStructure.IssueNode issueNode = (MantisStructure.IssueNode) userObject;
            IssueNodePresentationData pData = (IssueNodePresentationData) issueNode.getPresentation();

            final Color foreground = selected ? UIUtil.getTreeSelectionForeground() : UIUtil.getTreeTextForeground();

            CellConstraints cc = new CellConstraints();
            panel.add(baseComponent, cc.xy(2, 1));

            panel.setBackground(selected ? UIUtil.getTreeSelectionBackground() : UIUtil.getTreeTextBackground());
            panel.setForeground(foreground);

            author.setForeground(foreground);
            author.setText(pData.getAuthor());

            status.setForeground(foreground);
            status.setText(pData.getStatus());

            notes.setForeground(foreground);
            notes.setIcon(pData.getNotesIcon());
            notes.setText(pData.getNotesTxt());

            priority.setIcon(pData.getPriorityIcon());
//            priority.setToolTipText(issueNode.getPriority());

            lastUpdateDate.setForeground(foreground);
            lastUpdateDate.setText(pData.getLastUpdateDate());

            return panel;

        } else {
            return baseComponent;
        }
    }
}