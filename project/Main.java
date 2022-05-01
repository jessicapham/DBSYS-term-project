import java.util.*;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserTreeConstants;
import net.sf.jsqlparser.parser.CCJSqlParserDefaultVisitor;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.parser.SimpleNode;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.Statements;
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
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
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
            String schemaString = readFile("../" + args[0]);
            String query = readFile("../" + args[1]);

            traverseASTNodes(query, schemaString);
	
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

    public static void traverseASTNodes(String sql, String schemaString) throws JSQLParserException, FileNotFoundException {


        SimpleNode node = (SimpleNode) CCJSqlParserUtil.parseAST(sql);
        Schema orgSchema = new Schema();
        Schema sch = new Schema();
        
        createOrgSchema(orgSchema, schemaString);

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
                if (expr != null)
                    expr.accept(visitor);
            }
        }

        if (where != null)
            where.accept(visitor);

        sch.addMissingArgs(orgSchema);
        sch.genPrimalGraph();
        sch.genJoinGraph();
    }

    public static void createOrgSchema(Schema sch, String schemaString) {
        try {
            Statements schemaStmts = CCJSqlParserUtil.parseStatements(schemaString);
            for (Statement schemaStmt : schemaStmts.getStatements()) {
                try {
                    CreateTable tbl = (CreateTable) schemaStmt;
    
                    String tableName = tbl.getTable().getName().toLowerCase();
                    LinkedList<String> attributes = new LinkedList<>();
                    for (ColumnDefinition cdef : tbl.getColumnDefinitions()) {
                        sch.addColumn(tableName + "." + cdef.getColumnName().toLowerCase());
                    }
                } catch (ClassCastException c) {
                    System.err.println("\"" + schemaStmt + "\" is not a CREATE statement.");
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}



