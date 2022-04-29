import java.util.*;
import java.io.*;
import java.net.*;

import org.jgrapht.*;
import org.jgrapht.graph.*;
import org.jgrapht.traverse.*;
import org.jgrapht.nio.dimacs.*;

public class Schema {
    final String FILE_PATH = "../dimacs.graph";
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

    public ArrayList<Column> getColumnsWithAlias(int a) {
        ArrayList<Column> cols = new ArrayList<Column>();

        for (Table tab: tables) {
            for (Column c: tab.getAllColumns()) {
                if (c.getAlias() == a) {
                    cols.add(c);
                }
            }
        }

        return cols;
    }

    public void addAlias(String left, String right) {
        String[] ls = left.split("\\.");
        String[] rs = right.split("\\.");

        try {
            Column lCol = getColumn(ls[0], ls[1]);
            Column rCol = getColumn(rs[0], rs[1]);
            int lal = lCol.getAlias();
            int ral = rCol.getAlias();

            if (lal > 0 && ral == 0) {
                rCol.setAlias((lal));
            } else if (lal > 0 && ral > 0) {
                ArrayList<Column> cols = getColumnsWithAlias(ral);
                for (Column col: cols) {
                    col.setAlias(lal);
                }
            } else if (ral > 0 && lal == 0) {
                lCol.setAlias(ral);
            } else if(ral > 0 && lal > 0) {
                ArrayList<Column> cols = getColumnsWithAlias(lal);
                for (Column col: cols) {
                    col.setAlias(lal);
                }
            } else {
                rCol.setAlias(counter);
                lCol.setAlias(counter);
                counter++;
            }
        } catch (Exception e) {
            System.out.println(e);
            return;
        }
    }

    void genPrimalGraph() throws FileNotFoundException {
        String[] rels = this.toString().split("\\n");
		Graph<String, DefaultEdge> g = new DefaultUndirectedGraph<>(DefaultEdge.class);

        for (String r: rels) {
            addVertices(r, g);
        }

        /*
        System.out.println("\n============ RENAMED ARGS ============\n");
        System.out.println(this);

        System.out.println("\n============ PRIMAL GRAPH ============\n");
        
        for (String v : g.vertexSet()) {
			System.out.println("vertex: " + v);
		}

		for (DefaultEdge e : g.edgeSet()) {
			System.out.println("edge: " + e);
        }
        
        //System.out.println("\n============ DIMACS REPRESENTATION ============\n");
        System.out.println("See " + FILE_PATH + "\n");
        */

        DIMACSExporter<String, DefaultEdge> de = new DIMACSExporter<String, DefaultEdge>();
		File file = new File(FILE_PATH);
        Writer wr = new PrintWriter(file);
		de.exportGraph(g, wr);

        
        
    }

    void addVertices(String r, Graph<String, DefaultEdge> g) {
		String[] vs = r.split("\\(|\\)")[1].split(",");

		for (int i = 0; i < vs.length; i++) {
			for (int j = i+1; j < vs.length; j++) {
				g.addVertex(vs[i].strip());
				g.addVertex(vs[j].strip());
				g.addEdge(vs[i].strip(), vs[j].strip());
			}
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