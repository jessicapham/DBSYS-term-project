import java.util.*;

public class Table {
    String name;
    String orgName;
    Boolean aliased;
    HashMap<String, Column> columns;

    Table(String n, Boolean a, String o) {
        name = n;
        aliased = a;
        orgName = o;
        columns = new HashMap<String, Column>();
    }

    public void setAliased(Boolean a) {
        this.aliased = a;
    }

    public void setOrgName(String o) {
        orgName = o;
    }

    public Column addColumn(String c) {
        if (columns.get(c) != null) return columns.get(c);

        Column col = new Column(c);
        columns.put(c, col);

        return col;
    }

    public Column getColumn(String c) throws Exception {
        Column col = columns.get(c);
        if(col != null) return col;

        return addColumn(c);
    }

    String getName() {
        return name;
    }

    Collection<Column> getAllColumns() {
        return columns.values();
    }

    Boolean isAliased() {
        return aliased;
    }

    String getOrgTableName() {
        return orgName;
    }

    boolean containsTableColumn(Table tbar) {
        for (Column c: this.getAllColumns()) {
            for (Column tcol: tbar.getAllColumns()) {
                if (c.getAlias() == tcol.getAlias()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        String res = name + " (";
        Collection<Column> cols = columns.values();
        int i = 0;
        for (Column c: cols) {
            res += c.getAlias();
            if(i++ != cols.size() - 1) res += ", ";
        }
        res += ")";
        return res;
    }
}