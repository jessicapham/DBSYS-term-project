import java.util.*;
import java.io.File; 
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import org.opencypher.v9_0.ast.factory.neo4j.JavaCCParser;
import org.opencypher.v9_0.util.OpenCypherExceptionFactory;
import org.opencypher.v9_0.util.AnonymousVariableNameGenerator;
import org.opencypher.v9_0.ast.Statement;
import org.opencypher.v9_0.expressions.LogicalVariable;
import org.opencypher.v9_0.expressions.RelationshipChain;
import org.opencypher.v9_0.expressions.NodePattern;
import org.opencypher.v9_0.expressions.RelationshipPattern;
import org.opencypher.v9_0.expressions.EveryPath;
import org.opencypher.v9_0.expressions.Variable;
import org.opencypher.v9_0.expressions.RelTypeName;
import org.opencypher.v9_0.expressions.Variable;
import org.opencypher.v9_0.expressions.LabelName;
import scala.Option;
import scala.reflect.ClassTag;
import scala.collection.Seq;


public class Main {

	public static void main(String[] args) throws Exception {
		try { 
            String query = readFile("../" + args[0]);

            traverseASTNodes(query);
	
		} catch (Exception e) {
			System.out.println(e);
		}
	}

    public static String readFile(String fname) {
        String res = "";
        try {
            File myObj = new File(fname);
            Scanner myReader = new Scanner(myObj);

            while (myReader.hasNextLine()) {
                res += myReader.nextLine() + " ";
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        return res;
    }

    public static void traverseASTNodes(String query) throws FileNotFoundException, IOException {
        Schema sch = new Schema();
        Statement stmt = JavaCCParser.parse(query, new OpenCypherExceptionFactory(Option.apply(null)), new AnonymousVariableNameGenerator());

        ClassTag<NodePattern> np = ClassTag.apply(NodePattern.class);
        ClassTag<RelationshipChain> rc = ClassTag.apply(RelationshipChain.class);
        ClassTag<RelationshipPattern> rp = ClassTag.apply(RelationshipPattern.class);
        ClassTag<EveryPath> ep = ClassTag.apply(EveryPath.class);
        ClassTag<RelTypeName> rtn = ClassTag.apply(RelTypeName.class);
        ClassTag<Variable> vari = ClassTag.apply(Variable.class);
        ClassTag<LabelName> ln = ClassTag.apply(LabelName.class);

        Seq<EveryPath> eplist = stmt.folder().findAllByClass(ep);
        
        for (int i = 0; i < eplist.size(); i++) {
            EveryPath path = eplist.apply(i);

            Seq<NodePattern> nodes = path.folder().findAllByClass(np);
            Seq<RelationshipPattern> relations = path.folder().findAllByClass(rp);
            ArrayList<String> cols = new ArrayList<String>();
            ArrayList<String> col_als = new ArrayList<String>();
            ArrayList<String> tabs = new ArrayList<String>();

            
            for (int j = 0; j < nodes.size(); j++) {
                Seq<Variable> variable = nodes.apply(j).folder().findAllByClass(vari);
                Seq<LabelName> label = nodes.apply(j).folder().findAllByClass(ln);

                String var_f = variable.size() == 1 ? variable.apply(0).name() : "";
                String label_f = label.size() == 1 ? label.apply(0).name() : "";

                cols.add(label_f);
                col_als.add(var_f);
            }
            for (int j = 0; j < relations.size(); j++) {
                Seq<RelTypeName> reltype = relations.apply(j).folder().findAllByClass(rtn);
                tabs.add(reltype.apply(0).name());
            }
            
            for (int j = 1; j < cols.size(); j++) {
                Table createdTable = sch.addTable(tabs.get(j - 1));
                String col1 = col_als.get(j-1).equals("") ? cols.get(j-1) : col_als.get(j-1);
                String col2 = col_als.get(j).equals("") ? cols.get(j) : col_als.get(j);
                createdTable.addColumn(col1);
                createdTable.addColumn(col2);
            }
        }

        sch.genPrimalGraph();
        sch.genJoinGraph();
        sch.genHyperGraph();
    }
}



