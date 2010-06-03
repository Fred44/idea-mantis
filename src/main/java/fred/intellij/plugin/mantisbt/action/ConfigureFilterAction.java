package fred.intellij.plugin.mantisbt.action;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: fred
 * Date: 2 juin 2010
 * Time: 08:52:21
 * To change this template use File | Settings | File Templates.
 */
public class ConfigureFilterAction extends AnAction {

    static final Icon CONFIG_ICON = IconLoader.getIcon("/general/applicationSettings.png");

    public ConfigureFilterAction() {
        getTemplatePresentation().setText("Configure filters ...");
        getTemplatePresentation().setIcon(CONFIG_ICON);
    }

    @Override
    public void actionPerformed(AnActionEvent event) {
        BrowserUtil.launchBrowser("http://localhost/mantis/query_view_page.php");
    }
}