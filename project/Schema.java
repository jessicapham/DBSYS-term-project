import java.util.*;
import java.io.*;
import java.net.*;

import org.jgrapht.*;
import org.jgrapht.graph.*;
import org.jgrapht.traverse.*;
import org.jgrapht.nio.dimacs.*;

public class Schema {
    final String FILE_PATH_PG = "../dimacs_pg.graph";
    final String FILE_PATH_JG = "../dimacs_jg.graph";
    HashMap<String, Table> tables;
    int counter = 1;

    Schema() {
        tables = new HashMap<String, Table>();
    }

    public void addTable(String t) {
        String tabName = "";
        String orgName = "";

        String[] tsplit = t.split(" as | ");
        if (tsplit.length == 2) {
            tabName = tsplit[1].strip();
            orgName = tsplit[0].strip();
        } else {
            tabName = t;
        }

        if (tables.get(tabName) != null) return;
        
        Table tab = new Table(tabName, tsplit.length == 2, orgName);

        tables.put(tabName, tab);
    }

    Table getTable(String t) {
        Table tab = tables.get(t);
        if (tab != null) return tab;

        addTable(t);
        return getTable(t);
    }

    public void addColumn(String c){
        String[] csplit = c.split("[\\.]");

        Table tab = getTable(csplit[0]);
        tab.addColumn(csplit[1]);
    }

    public Column getColumn(String tabName, String colName) throws Exception {
        Table t = getTable(tabName);
        return t.getColumn(colName);
    }

    public ArrayList<Column> getColumnsWithAlias(int a) {
        ArrayList<Column> cols = new ArrayList<Column>();

        for (Table tab: tables.values()) {
            for (Column c: tab.getAllColumns()) {
                if (c.getAlias() == a) {
                    cols.add(c);
                }
            }
        }

        return cols;
    }

    public Boolean isString(String exp) {
        return (exp.length() - exp.replaceAll("'","").length()) >= 2;
    }

    public void addAlias(String left, String right) {        
        try {
            String[] ls = left.split("\\.");
            String[] rs = right.split("\\.");
            Column lCol = getColumn(ls[0], ls[1]);

            if (isString(right) || rs.length != 2) {
                return;
            }

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

    void addMissingArgs(Schema sch) {
        Collection<Table> tabs = tables.values();

        for (Table t: tabs) {
            String orgTabName = t.getName();
            if (t.isAliased()) orgTabName = t.getOrgTableName();

            for (Column c: sch.getAllTableColumns(orgTabName)){
                t.addColumn(c.getName());
            }
        }
    }

    Collection<Column> getAllTableColumns(String tab) {
        Table t = tables.get(tab);
        Collection<Column> res = Collections.emptyList();
        
        if (t != null) res = t.getAllColumns();
        return res;
    }

    void genPrimalGraph() throws FileNotFoundException {
        //Assign random vals to the columns not in query
        for (Table t: tables.values()) {
            for (Column c: t.getAllColumns()) {
                if (c.getAlias() == 0) c.setAlias(counter++);
            }
        }
        String[] rels = this.toString().split("\\n");
		Graph<String, DefaultEdge> g = new DefaultUndirectedGraph<>(DefaultEdge.class);

        for (String r: rels) {
            addVertices(r, g);
        }

        // System.out.println("\n============ RENAMED ARGS ============\n");
        // System.out.println(this);
        /*

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

        graphExporter(g, FILE_PATH_PG);
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

    void genJoinGraph() throws FileNotFoundException {
        //Assign random vals to the columns not in query
        for (Table t: tables.values()) {
            for (Column c: t.getAllColumns()) {
                if (c.getAlias() == 0) c.setAlias(counter++);
            }
        }

		Graph<String, DefaultEdge> g = new DefaultUndirectedGraph<>(DefaultEdge.class);

        for (Table t: tables.values()) {
            for (Table tbar: tables.values()) {
                if (t.getName() == tbar.getName()) continue;

                if (t.containsTableColumn(tbar)) {
                    g.addVertex(t.getName().strip());
				    g.addVertex(tbar.getName().strip());
				    g.addEdge(t.getName(), tbar.getName());
                }
            }
        }

        // System.out.println("\n============ JOIN GRAPH ============\n");
        
        // for (String v : g.vertexSet()) {
		// 	System.out.println("vertex: " + v);
		// }

		// for (DefaultEdge e : g.edgeSet()) {
		// 	System.out.println("edge: " + e);
        // }  
        
        graphExporter(g, FILE_PATH_JG);
    }

    void graphExporter(Graph<String, DefaultEdge> g, String fp) throws FileNotFoundException {
        DIMACSExporter<String, DefaultEdge> de = new DIMACSExporter<String, DefaultEdge>();
		File file = new File(fp);
        Writer wr = new PrintWriter(file);
		de.exportGraph(g, wr);
    }

    @Override
    public String toString() {
        String res = "";
        for (Table t: tables.values()) {
            res += t.toString() + "\n";
        }
        return res;
    }
}