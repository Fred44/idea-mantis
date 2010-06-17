package flefebvre.intellij.plugin.mantisbt.browser;

import com.intellij.ide.projectView.PresentationData;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: fred
 * Date: 16 juin 2010
 * Time: 18:19:19
 * To change this template use File | Settings | File Templates.
 */
public class IssueNodePresentationData extends PresentationData {

    private String author;

    private String status;

    private Icon notesIcon;

    private String notesTxt;

    private Icon priorityIcon;

    private String lastUpdateDate;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Icon getNotesIcon() {
        return notesIcon;
    }

    public void setNotesIcon(Icon notesIcon) {
        this.notesIcon = notesIcon;
    }

    public String getNotesTxt() {
        return notesTxt;
    }

    public void setNotesTxt(String notesTxt) {
        this.notesTxt = notesTxt;
    }

    public Icon getPriorityIcon() {
        return priorityIcon;
    }

    public void setPriorityIcon(Icon priorityIcon) {
        this.priorityIcon = priorityIcon;
    }

    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }
}
