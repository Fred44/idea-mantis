package flefebvre.intellij.plugin.mantisbt.browser;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.Disposer;
import com.intellij.ui.treeStructure.CachingSimpleNode;
import com.intellij.ui.treeStructure.SimpleTree;
import com.intellij.ui.treeStructure.SimpleTreeBuilder;
import com.intellij.ui.treeStructure.SimpleTreeStructure;
import flefebvre.intellij.plugin.mantisbt.MantisIcons;
import flefebvre.intellij.plugin.mantisbt.MantisManagerComponent;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mantisbt.connect.model.IIssue;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.event.InputEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: fred
 * Date: 7 juin 2010
 * Time: 23:08:42
 * To change this template use File | Settings | File Templates.
 */
public class MantisStructure extends SimpleTreeStructure {

    private final Project myProject;
    private final MantisManagerComponent mantisMgr;

    private final SimpleTreeBuilder myTreeBuilder;
    private final RootNode myRoot = new RootNode();

    public MantisStructure(Project project, MantisManagerComponent mantisMgr, SimpleTree tree) {
        this.myProject = project;
        this.mantisMgr = mantisMgr;

        configureTree(tree);

        myTreeBuilder = new SimpleTreeBuilder(tree, (DefaultTreeModel) tree.getModel(), this, null);
        Disposer.register(myProject, myTreeBuilder);
        myTreeBuilder.initRoot();
        myTreeBuilder.expand(myRoot, null);
    }

    private void configureTree(final SimpleTree tree) {
        tree.setRootVisible(false);
        tree.setShowsRootHandles(true);
    }

    @Override
    public Object getRootElement() {
        return myRoot;
    }

    public void update() {
        myRoot.updateIssues();
    }

    private void updateFrom(com.intellij.ui.treeStructure.SimpleNode node) {
        myTreeBuilder.addSubtreeToUpdateByElement(node);
    }

    private void updateUpTo(com.intellij.ui.treeStructure.SimpleNode node) {
        com.intellij.ui.treeStructure.SimpleNode each = node;
        while (each != null) {
            updateFrom(each);
            each = each.getParent();
        }
    }

    public static SimpleNode getSelectedNode(SimpleTree tree) {
        TreePath treePath = tree.getSelectionPath();
        if (treePath != null) {
            return (SimpleNode) tree.getNodeFor(treePath);
        } else {
            return null;
        }
    }

    public abstract class SimpleNode extends CachingSimpleNode {
        private SimpleNode myParent;

        public SimpleNode(SimpleNode parent) {
            super(MantisStructure.this.myProject, null);
            this.myParent = parent;
        }

        public <T extends SimpleNode> T findParent(Class<T> parentClass) {
            SimpleNode node = this;
            while (true) {
                node = node.myParent;
                if (node == null || parentClass.isInstance(node)) {
                    //noinspection unchecked
                    return (T) node;
                }
            }
        }

        @Override
        protected SimpleNode[] buildChildren() {
            List<? extends SimpleNode> children = doGetChildren();
            if (children.isEmpty()) return new SimpleNode[0];

            List<SimpleNode> result = new ArrayList<SimpleNode>();
            for (SimpleNode each : children) {
                result.add(each);
            }
            return result.toArray(new SimpleNode[result.size()]);
        }

        protected List<? extends SimpleNode> doGetChildren() {
            return Collections.emptyList();
        }

        @Nullable
        @NonNls
        String getActionId() {
            return null;
        }

        @Nullable
        @NonNls
        String getMenuId() {
            return null;
        }

        protected void childrenChanged() {
            SimpleNode each = this;
            while (each != null) {
                each.cleanUpCache();
                each = (SimpleNode) each.getParent();
            }
            updateUpTo(this);
        }

        @Override
        protected void doUpdate() {
            setName(getName());
        }

        protected void setName(String name) {
            getTemplatePresentation().setPresentableText(name);
        }

        public void handleDoubleClickOrEnter(SimpleTree tree, InputEvent inputEvent) {
            String actionId = getActionId();
            if (actionId != null) {
//                MavenUIUtil.executeAction(actionId, inputEvent);
            }
        }
    }

    public abstract class GroupNode extends SimpleNode {
        public GroupNode(SimpleNode parent) {
            super(parent);
        }

        protected void sort(List<IssueNode> list) {
            Collections.sort(list, NODE_COMPARATOR);
        }
    }

    public class RootNode extends GroupNode {
        private List<IssueNode> issueNodes = new ArrayList<IssueNode>();

        public RootNode() {
            super(null);
        }

        protected List<? extends SimpleNode> doGetChildren() {
            return issueNodes;
        }

        public void updateIssues() {
            Collection<IIssue> issues = mantisMgr.getIssues();
            List<IssueNode> newNodes = new ArrayList<IssueNode>();

            if (issues != null) {
                for (IIssue issue : issues) {
                    IssueNode issueNode = findOrCreateNodeFor(issue);
                    newNodes.add(issueNode);
                }
            }
            issueNodes = newNodes;
            sort(issueNodes);
            childrenChanged();
        }

        private IssueNode findOrCreateNodeFor(IIssue issue) {
            for (IssueNode each : issueNodes) {
                if (each.getIssue().getId() == issue.getId()) return each;
            }
            return new IssueNode(this, issue);
        }
    }

    public class IssueNode extends SimpleNode {

        private final DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        private final IIssue issue;

        public IssueNode(SimpleNode parent, IIssue issue) {
            super(parent);
            this.issue = issue;
            getTemplatePresentation().setIcons(MantisIcons.TASK_ICON);
        }

        public IIssue getIssue() {
            return issue;
        }

        @NotNull
        @Override
        protected PresentationData createPresentation() {
            return new IssueNodePresentationData();
        }

        @Override
        protected void update(PresentationData presentation) {
            IssueNodePresentationData pData = (IssueNodePresentationData) presentation;

            if (issue.getStatus().getId() == 80 || issue.getStatus().getId() == 90) {
                pData.setIcons(MantisIcons.TASK_COMPLETED_ICON);
            } else {
                pData.setIcons(MantisIcons.TASK_ICON);
            }
            pData.setPresentableText("[" + issue.getId() + "]  " + issue.getSummary());

            pData.setAuthor(issue.getReporter().getName());

            pData.setStatus(issue.getStatus().getName());

            if (issue.getNotes() != null && issue.getNotes().length > 0) {
                pData.setNotesIcon(MantisIcons.COMMENT_ICON);
                pData.setNotesTxt("(" + issue.getNotes().length + ")");
            } else {
                pData.setNotesIcon(null);
                pData.setNotesTxt("");
            }

            if (issue.getPriority().getId() == 10) {
                pData.setPriorityIcon(MantisIcons.PRIORITY_NONE_ICON);
            } else if (issue.getPriority().getId() == 20) {
                pData.setPriorityIcon(MantisIcons.PRIORITY_LOW_ICON);
            } else if (issue.getPriority().getId() == 30) {
                pData.setPriorityIcon(MantisIcons.PRIORITY_NORMAL_ICON);
            } else if (issue.getPriority().getId() == 40) {
                pData.setPriorityIcon(MantisIcons.PRIORITY_HIGH_ICON);
            } else if (issue.getPriority().getId() == 50) {
                pData.setPriorityIcon(MantisIcons.PRIORITY_URGENT_ICON);
            } else if (issue.getPriority().getId() == 60) {
                pData.setPriorityIcon(MantisIcons.PRIORITY_IMMEDIATE_ICON);
            }

            pData.setLastUpdateDate(df.format(issue.getDateLastUpdated()));
        }

        @Override
        String getActionId() {
            return "Mantis.openIssue";
        }

        @Override
        String getMenuId() {
            return "Mantis.oneIssueGroup";
        }
    }

    private static final Comparator<IssueNode> NODE_COMPARATOR = new Comparator<IssueNode>() {
        public int compare(IssueNode o1, IssueNode o2) {
            return -Comparing.compare(o1.getIssue().getDateLastUpdated(), o2.getIssue().getDateLastUpdated());
        }
    };
}
