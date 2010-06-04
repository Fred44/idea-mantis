package flefebvre.intellij.plugin.mantisbt.ui;

import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;
import org.jetbrains.annotations.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: flefebvre
 * Date: 13 avr. 2010
 * Time: 13:24:03
 * To change this template use File | Settings | File Templates.
 */
public class PluginToolWindow {

    public static final String TOOL_WINDOW_NAME = "Mantis";

    private ToolWindow ideaToolWindow;

    private IssueListPanel issuesPanel;

    public PluginToolWindow(IssueListPanel issuesPanel) {
        this.issuesPanel = issuesPanel;
    }

    public void register(@NotNull ToolWindowManager toolWindowManager) {
        ideaToolWindow = toolWindowManager.registerToolWindow(
                TOOL_WINDOW_NAME, false, ToolWindowAnchor.BOTTOM);
        ideaToolWindow.setIcon(IconLoader.getIcon("/icons/mantis.png"));
    }

    public void initPanel() {
        final ContentManager contentManager = ideaToolWindow.getContentManager();
        contentManager.removeAllContents(false);
        contentManager.addContent(createIssuesContent());
    }

    protected Content createIssuesContent() {
        final ContentManager contentManager = ideaToolWindow.getContentManager();
        final Content content = contentManager.getFactory().createContent(issuesPanel, "issues", false);
        content.putUserData(ToolWindow.SHOW_CONTENT_ICON, Boolean.TRUE);

        return content;
    }

    public IssueListPanel getIssuesListPanel() {
        return issuesPanel;
    }
}