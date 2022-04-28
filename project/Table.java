import java.util.*;

public class Table {
    String name;
    ArrayList<Column> columns;

    Table(String n) {
        name = n;
        columns = new ArrayList<Column>();
    }

    public void addColumn(String c) {
        for (Column col: columns) {
            if (col.getName().equals(c)) return;
        }

        Column col = new Column(c);
        columns.add(col);
    }

    public Column getColumn(String c) throws Exception {
        for (Column col: columns) {
            if (col.getName().equals(c)) return col;
        }

        throw new Exception("Col: " + c + " does not belong to table " + name);
    }

    String getName() {
        return name;
    }

    ArrayList<Column> getAllColumns() {
        return columns;
    }

    @Override
    public String toString() {
        String res = name + "(";
        for (int i = 0; i < columns.size(); i++) {
            res += columns.get(i).getAlias();
            if (i != columns.size() - 1) res += ", ";
        }
        res += ")";
        return res;
    }
}