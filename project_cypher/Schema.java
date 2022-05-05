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
    final String FILE_PATH_HG = "../hypergraph.txt";
    HashMap<String, Table> tables;
    int counter = 1;

    Schema() {
        tables = new HashMap<String, Table>();
    }

    public Table addTable(String t) {
        String tabName = "";
        String orgName = "";

        String[] tsplit = t.split(" as | ");
        if (tsplit.length == 2) {
            tabName = tsplit[1].strip();
            orgName = tsplit[0].strip();
        } else {
            tabName = t;
        }

        if (tables.get(tabName) != null) tabName += "_";
        
        Table tab = new Table(tabName, tsplit.length == 2, orgName);

        tables.put(tabName, tab);
        return tab;
    }

    Table getTable(String t) {
        Table tab = tables.get(t);
        if (tab != null) return tab;

        addTable(t);
        return getTable(t);
    }

    public void addColumn(String c){
        String[] csplit = c.split("\\.");

        Table tab = getTable(csplit[0]);
        tab.addColumn(csplit[1]);
    }

    void genPrimalGraph() throws FileNotFoundException {
        String[] rels = this.toString().split("\\n");
		Graph<String, DefaultEdge> g = new DefaultUndirectedGraph<>(DefaultEdge.class);

        for (String r: rels) {
            addVertices(r, g);
        }

        System.out.println("\n============ RENAMED ARGS ============\n");
        System.out.println(this);


        System.out.println("\n============ PRIMAL GRAPH ============\n");
        
        for (String v : g.vertexSet()) {
			System.out.println("vertex: " + v);
		}

		for (DefaultEdge e : g.edgeSet()) {
			System.out.println("edge: " + e);
        }
        
        graphExporter(g, FILE_PATH_PG);
    }

    void genJoinGraph() throws FileNotFoundException {
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

        graphExporter(g, FILE_PATH_JG);
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

    void genHyperGraph() throws FileNotFoundException, IOException {
        String hypergraph = this.toString();
        hypergraphExporter(hypergraph, FILE_PATH_HG);
    }

    void hypergraphExporter(String graph, String fp) throws FileNotFoundException, IOException {
		File file = new File(fp);
        Writer wr = new PrintWriter(file);
		wr.write(graph);
        wr.flush();
        wr.close();
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
            res += t.toString() + ",\n";
        }
        res = res.replaceAll(",\n$", ".");
        return res;
    }
}