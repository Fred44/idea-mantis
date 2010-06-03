package fred.intellij.plugin.mantisbt.ui;

import com.intellij.openapi.ui.Messages;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: fred
 * Date: 31 mai 2010
 * Time: 21:38:30
 * To change this template use File | Settings | File Templates.
 */
public class UiUtil {

    public static void showError(final String title, final String message) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                Messages.showMessageDialog(
                        message,
                        title,
                        Messages.getErrorIcon());
            }
        });
    }
}