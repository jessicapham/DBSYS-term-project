import java.util.*;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserTreeConstants;
import net.sf.jsqlparser.parser.CCJSqlParserDefaultVisitor;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.parser.SimpleNode;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.StatementVisitorAdapter;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectVisitorAdapter;


public class Main {

	public static void main(String[] args) throws Exception {
		try {

			String selectSQL = "SELECT count(*)" +
                " FROM Person_knows_Person" +
                " JOIN Comment ON Person_knows_Person.Person1Id = Comment.hasCreator_Person" + 
                " JOIN Post ON Person_knows_Person.Person2Id = Post.hasCreator_Person" +
                " AND Comment.replyOf_Post = Post.id";

            traverseASTNodes(selectSQL);
	
		} catch (Exception e) {
			System.out.println(e);
		}
	}

    public static void traverseASTNodes(String sql) throws JSQLParserException {
        SimpleNode node = (SimpleNode) CCJSqlParserUtil.parseAST(sql);
        
        System.out.println("---------- NODE DUMP ----------");
        node.dump("*");

        System.out.println("\n");
        System.out.println("---------- VISITING NODES ----------");
        CCJSqlParserDefaultVisitor visitor = new CCJSqlParserDefaultVisitor() {
            int count = 0;

            @Override
            public Object visit(SimpleNode node, Object data) {
                if (node.toString() == "JoinerExpression") {
                    System.out.println("- Join Expression");
                }

                if (node.toString() == "RegularCondition") {
                    System.out.println("--- Condition in Join Expression");
                    count += 1;
                }

                if (node.toString() == "Column") {
                    System.out.println("join attr " + count + ": " + node.jjtGetValue());
                }

                if (node.toString() == "Table") {
                    System.out.println("table: " + node.jjtGetValue());
                }
                
                if (node.toString() == "SelectItem") {
                    System.out.println("select item: " + node.jjtGetValue());
                }

                return super.visit(node, data);
            }
        };

        node.jjtAccept(visitor, null);
    }
}



