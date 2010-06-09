package flefebvre.intellij.plugin.mantisbt.ui;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.ui.SearchTextField;
import flefebvre.intellij.plugin.mantisbt.model.IssueListManager;
import flefebvre.intellij.plugin.mantisbt.model.IssueListModel;
import flefebvre.intellij.plugin.mantisbt.model.MantisGroupBy;
import flefebvre.intellij.plugin.mantisbt.ui.tree.IssueTreeBuilder;
import org.jetbrains.annotations.NotNull;
import org.mantisbt.connect.MCException;
import org.mantisbt.connect.model.IIssue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by IntelliJ IDEA.
 * User: flefebvre
 * Date: 1 avr. 2010
 * Time: 23:11:39
 * To change this template use File | Settings | File Templates.
 */
public class IssueListPanel extends JPanel {

    private static final String TOOLBAR_NAME = "MantisToolBar";

    private Project project;

    private IssueListManager issueListMgr;
    private IssueListModel issueListModel;

    private DefaultComboBoxModel filterListModel;

    private JTree issueTree;
    private IssueTreeBuilder issueTreeBuilder;
    private IIssue selectedIssue;

    private SearchTextField searchField;

    public IssueListPanel(@NotNull final Project project, IssueListManager issueListMgr, IssueListModel listModel) {

        this.project = project;
        filterListModel = new DefaultComboBoxModel();

        this.issueListMgr = issueListMgr;
        this.issueListModel = listModel;

        this.issueListMgr.setListModel(this.issueListModel);

        this.issueTreeBuilder = new IssueTreeBuilder(this.issueListModel);

        initUI();

        issueTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvt) {
                if (mouseEvt.getButton() == MouseEvent.BUTTON1
                        && mouseEvt.getClickCount() == 2
                        && selectedIssue != null) {
                    // TODO: open issue
                }
            }

            @Override
            public void mousePressed(MouseEvent mouseEvt) {
                if (mouseEvt.isPopupTrigger()) {
                    showCtxMenu();
                }
            }
        });
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JPanel tbPane = new JPanel(new BorderLayout());
        searchField = new SearchTextField();
        tbPane.add(searchField, BorderLayout.EAST);
        tbPane.add(createToolBar(TOOLBAR_NAME, this.getClass().getSimpleName()), BorderLayout.WEST);
        add(tbPane, BorderLayout.NORTH);

        issueTree = new JTree();
        add(new JScrollPane(issueTree), BorderLayout.CENTER);
    }

    private void showCtxMenu() {
        //TODO
    }

    public void refresh() {
        Task.Backgroundable task = new Task.Backgroundable(project, "Refresh data", false) {
            @Override
            public void run(@NotNull final ProgressIndicator indicator) {
                try {
//                    getStatusBarPane().setInfoMessage("Loading issues...", false);
                    issueListMgr.clearCache();
                    issueListMgr.loadFilters();
                    issueListMgr.loadIssues();
                } catch (MCException e) {
                    UiUtil.showError("Connection failure", "Server not responding! Try again.");
                }
            }

            @Override
            public void onSuccess() {
                issueTreeBuilder.buildTree(issueTree);
            }
        };
        ProgressManager.getInstance().run(task);
    }

    public void expandTree() {
        for (int i = 0; i < issueTree.getRowCount(); i++) {
            issueTree.expandRow(i);
        }
    }

    public void collapseTree() {
        for (int i = 0; i < issueTree.getRowCount(); i++) {
            issueTree.collapseRow(i);
        }
    }

    private JComponent createToolBar(String toolbarName, String toolbarPalce) {
        JComponent component = null;
        ActionManager actionManager = ActionManager.getInstance();
        ActionGroup toolbar = (ActionGroup) actionManager.getAction(toolbarName);
        if (toolbar != null) {
            final ActionToolbar actionToolbar = actionManager.createActionToolbar(toolbarPalce, toolbar, true);
            actionToolbar.setTargetComponent(this);
            component = actionToolbar.getComponent();
        }
        return component;
    }

    public ComboBoxModel getFilterListModel() {
        return filterListModel;
    }

    public void setGroupBy(MantisGroupBy groupBy) {
        issueTreeBuilder.setGroupBy(groupBy);
        issueTreeBuilder.buildTree(this.issueTree);
    }
}