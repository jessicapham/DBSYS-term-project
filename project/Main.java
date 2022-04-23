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

			String selectSQL = "SELECT  a,  b FROM  mytable WHERE a > 10 AND b > 100";

            traverseASTNodes(selectSQL);
	
		} catch (Exception e) {
			System.out.println(e);
		}
	}

    public static void traverseASTNodes(String sql) throws JSQLParserException {
        SimpleNode node = (SimpleNode) CCJSqlParserUtil.parseAST(sql);
        
        System.out.println("----- NODE DUMP -----");
        node.dump("*");

        System.out.println("\n");
        System.out.println("----- VISITING NODES -----");
        node.jjtAccept(new CCJSqlParserDefaultVisitor() {
            @Override
            public Object visit(SimpleNode node, Object data) {
                System.out.println("node: " + node.toString() + " --- value: " + node.jjtGetValue() + " --- num children: " + node.jjtGetNumChildren());
                return super.visit(node, data);
            }
        }, null);
    }
}



