package fred.intellij.plugin.mantisbt.model;

/**
 * Created by IntelliJ IDEA.
 * User: fred
 * Date: 2 juin 2010
 * Time: 23:30:35
 * To change this template use File | Settings | File Templates.
 */
public enum MantisGroupBy {
    NONE("None"),
    UPDATE_DATE("Update date"),
    CATEGORY("Category");

    private String label;

    MantisGroupBy(String label) {
        this.label = label;
    }

    public String toString() {
        return label;
    }
}