package flefebvre.intellij.plugin.mantisbt.browser;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.ui.treeStructure.CachingSimpleNode;
import com.intellij.ui.treeStructure.SimpleTree;
import com.intellij.ui.treeStructure.SimpleTreeBuilder;
import com.intellij.ui.treeStructure.SimpleTreeStructure;
import flefebvre.intellij.plugin.mantisbt.MantisManagerComponent;
import flefebvre.intellij.plugin.mantisbt.MantisSession;
import org.mantisbt.connect.MCException;
import org.mantisbt.connect.model.IIssueHeader;

import javax.swing.tree.DefaultTreeModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
    }

    public abstract class GroupNode extends SimpleNode {
        public GroupNode(SimpleNode parent) {
            super(parent);
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
            MantisSession session = mantisMgr.getSession();
            try {
                Collection<IIssueHeader> issues = session.getIssues(4L);
                List<IssueNode> newNodes = new ArrayList<IssueNode>(issues.size());

                for (IIssueHeader issue : issues) {
                    IssueNode issueNode = new IssueNode(this, issue);
                    newNodes.add(issueNode);
                }
                issueNodes = newNodes;
                childrenChanged();
            } catch (MCException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    public class IssueNode extends SimpleNode {

        private final IIssueHeader issue;

        public IssueNode(SimpleNode parent, IIssueHeader issue) {
            super(parent);
            this.issue = issue;
        }

        @Override
        public String getName() {
            return issue.getId() + " - " + issue.getSummary();
        }
    }
}
