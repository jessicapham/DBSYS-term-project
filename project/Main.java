import java.util.*;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserTreeConstants;
import net.sf.jsqlparser.parser.CCJSqlParserDefaultVisitor;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.parser.SimpleNode;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.StatementVisitorAdapter;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.AllTableColumns;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectVisitorAdapter;
import net.sf.jsqlparser.statement.select.SelectItemVisitorAdapter;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.operators.relational.ComparisonOperator;
import net.sf.jsqlparser.util.TablesNamesFinder;
import java.io.File; 
import java.io.FileNotFoundException; 
import java.util.Scanner;



public class Main {

	public static void main(String[] args) throws Exception {
		try { 
            String query = "";
            try {
                File myObj = new File("sql.txt");
                Scanner myReader = new Scanner(myObj);
    
                while (myReader.hasNextLine()) {
                    query += myReader.nextLine() + " ";
                }
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }

            traverseASTNodes(query);
	
		} catch (Exception e) {
			System.out.println(e);
		}
	}

    public static void traverseASTNodes(String sql) throws JSQLParserException {
        SimpleNode node = (SimpleNode) CCJSqlParserUtil.parseAST(sql);
        Schema sch = new Schema();

        node.jjtAccept(new CCJSqlParserDefaultVisitor() {
            @Override
            public Object visit(SimpleNode node, Object data) {
                if (node.getId() == CCJSqlParserTreeConstants.JJTCOLUMN) {
                    sch.addColumn(node.jjtGetValue().toString().toLowerCase());
                } else if (node.getId() == CCJSqlParserTreeConstants.JJTTABLE) {
                    sch.addTable(node.jjtGetValue().toString().toLowerCase());
                }
                return super.visit(node, data);
            }
        }, null);

        Select stmt = (Select) CCJSqlParserUtil.parse(sql);
        PlainSelect plainSelect=(PlainSelect) stmt.getSelectBody();
        List<Join> js = plainSelect.getJoins();
        Expression where = plainSelect.getWhere();

        ExpressionVisitorAdapter visitor = new ExpressionVisitorAdapter() {
            @Override
            protected void visitBinaryExpression(BinaryExpression expr) {
                if (expr instanceof ComparisonOperator) {
                    String leftExpr = expr.getLeftExpression().toString().toLowerCase();
                    String rightExpr = expr.getRightExpression().toString().toLowerCase();
                    sch.addAlias(leftExpr, rightExpr);
                }
    
                super.visitBinaryExpression(expr); 
            }
        };

        for (Join j: js) {
            Collection<Expression> expressions = j.getOnExpressions();

            for (Expression expr: expressions){
                expr.accept(visitor);
            }
        }

        where.accept(visitor);


        System.out.println(sch);
    }
}



