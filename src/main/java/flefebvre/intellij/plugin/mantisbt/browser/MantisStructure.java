package flefebvre.intellij.plugin.mantisbt.browser;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.ui.treeStructure.CachingSimpleNode;
import com.intellij.ui.treeStructure.SimpleTree;
import com.intellij.ui.treeStructure.SimpleTreeBuilder;
import com.intellij.ui.treeStructure.SimpleTreeStructure;
import org.mantisbt.connect.model.IIssueHeader;

import javax.swing.tree.DefaultTreeModel;
import java.util.ArrayList;
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
    private final SimpleTreeBuilder myTreeBuilder;
    private final RootNode myRoot = new RootNode();

    public MantisStructure(Project project, SimpleTree tree) {
        this.myProject = project;

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

    public abstract class SimpleNode extends CachingSimpleNode {
        private SimpleNode myParent;

        public SimpleNode(SimpleNode parent) {
            super(MantisStructure.this.myProject, null);
            this.myParent = parent;
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
    }

    public abstract class GroupNode extends SimpleNode {
        public GroupNode(SimpleNode parent) {
            super(parent);
        }
    }

    public class RootNode extends IssuesNode {

        public RootNode() {
            super(null);
        }
    }

    public class IssuesNode extends GroupNode {
        private List<IssueNode> issues = new ArrayList<IssueNode>();

        public IssuesNode(SimpleNode parent) {
            super(parent);
        }
    }

    public class IssueNode extends SimpleNode {

        private final IIssueHeader issue;

        public IssueNode(SimpleNode parent, IIssueHeader issue) {
            super(parent);
            this.issue = issue;
        }
    }

}
