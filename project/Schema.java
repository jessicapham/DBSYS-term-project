import java.util.*;

public class Schema {
    ArrayList<Table> tables;
    int counter = 1;

    Schema() {
        tables = new ArrayList<Table>();
    }

    public void addTable(String t) {
        String[] tsplit = t.split(" as | ");
        if (tsplit.length == 2) t = tsplit[1].strip();
        for (Table tab: tables) {
            if (tab.getName().equals(t)) {return;}
        }
        
        Table tab = new Table(t);
        tables.add(tab);
    }

    Table getTable(String t) {
        for (Table tab: tables) {
            if (tab.getName().equals(t)) return tab;
        }

        addTable(t);
        return getTable(t);
    }

    public void addColumn(String c){
        String[] csplit = c.split("\\.");

        Table tab = getTable(csplit[0]);
        tab.addColumn(csplit[1]);
    }

    public Column getColumn(String tabName, String colName) throws Exception {
        Table t = getTable(tabName);
        return t.getColumn(colName);
    }

    public void addAlias(String left, String right) {
        String[] ls = left.split("\\.");
        String[] rs = right.split("\\.");

        try {
            Column lCol = getColumn(ls[0], ls[1]);
            Column rCol = getColumn(rs[0], rs[1]);
            int lal = lCol.getAlias();
            int ral = rCol.getAlias();

            if (lal > 0) {
                rCol.setAlias((lal));
                return;
            }
            else if (ral > 0) {
                lCol.setAlias(ral);
                return;
            } else {
                rCol.setAlias(counter);
                lCol.setAlias(counter);
                counter++;
                return;
            }
        } catch (Exception e) {
            System.out.println(e);
            return;
        }
    }

    @Override
    public String toString() {
        String res = "";
        for (Table t: tables) {
            res += t.toString() + "\n";
        }
        return res;
    }
}