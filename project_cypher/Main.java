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
            String query1 = "MATCH (:Country)<-[:IS_PART_OF]-(:City)<-[:IS_LOCATED_IN]-(:Person)<-[:HAS_MEMBER]-(:Forum)-[:CONTAINER_OF]->(:Post)<-[:REPLY_OF]-(:Comment)-[:HAS_TAG]->(:Tag)-[:HAS_TYPE]->(:TagClass) RETURN count(*) AS count";
            String query2 = "MATCH (person1:Person)-[:KNOWS]-(person2:Person), (person1)<-[:HAS_CREATOR]-(comment:Comment)-[:REPLY_OF]->(post:Post)-[:HAS_CREATOR]->(person2) RETURN count(*) AS count";
            String query3 = "MATCH (person1:Person)-[:IS_LOCATED_IN]->(city1:City)-[:IS_PART_OF]->(country) MATCH (person2:Person)-[:IS_LOCATED_IN]->(city2:City)-[:IS_PART_OF]->(country) MATCH (person3:Person)-[:IS_LOCATED_IN]->(city3:City)-[:IS_PART_OF]->(country) MATCH (person1)-[:KNOWS]-(person2)-[:KNOWS]-(person3)-[:KNOWS]-(person1) RETURN count(*) AS count";
            String query4 = "MATCH (:Tag)<-[:HAS_TAG]-(message:Message)-[:HAS_CREATOR]-( creator:Person), (message)<-[:LIKES]-(liker:Person), (message)<-[:REPLY_OF]-(comment:Comment) RETURN count(*) AS count";
            String query5Broken = "MATCH (tag1:Tag)<-[:HAS_TAG]-(message:Message)<-[:REPLY_OF ]-(comment:Comment)-[:HAS_TAG]->(tag2:Tag) WHERE tag1 <> tag2 RETURN count(*) AS count";
            String query6 = "MATCH (:Tag)<-[:HAS_TAG]-(message:Message)-[:HAS_CREATOR]-( creator:Person) OPTIONAL MATCH (message)<-[:LIKES]-(liker:Person) OPTIONAL MATCH (message)<-[:REPLY_OF]-(comment:Comment) RETURN count(*) AS count";
            String query7Broken = "MATCH (tag1:Tag)<-[:HAS_TAG]-(message:Message)<-[:REPLY_OF ]-(comment:Comment)-[:HAS_TAG]->(tag2:Tag) WHERE NOT (comment)-[:HAS_TAG]->(tag1) AND tag1 <> tag2 RETURN count(*) AS count";
            
            traverseASTNodes(query1);

	
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



