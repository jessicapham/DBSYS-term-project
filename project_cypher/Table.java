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

    public void addColumn(String c) {
        if (columns.get(c) != null) return;

        Column col = new Column(c);
        columns.put(c, col);
    }

    public Column getColumn(String c) throws Exception {
        Column col = columns.get(c);
        if(col != null) return col;

        throw new Exception("Col: " + c + " does not belong to table " + name);
    }

    String getName() {
        return name;
    }

    @Override
    public String toString() {
        String res = name + "(";
        Collection<Column> cols = columns.values();
        int i = 0;
        for (Column c: cols) {
            res += c.getName();
            if(i++ != cols.size() - 1) res += ", ";
        }
        res += ")";
        return res;
    }
}