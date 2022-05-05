import java.util.*;
import java.util.stream.Collectors;

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

public class Preprocessor {
    private Map<String, String> TPCH_TABLE_MAP = new HashMap<>();

    Preprocessor() {
        TPCH_TABLE_MAP = Map.of(
            "p_", "part",
            "ps_", "part_supplier",
            "l_", "lineitem",
            "o_", "orders",
            "s_", "supplier",
            "n_", "nation",
            "c_", "customer",
            "r_", "region"
        );
    }

    String processTPCHQuery(String query) throws JSQLParserException{
        Map<String, String> map = new HashMap<>();

        SimpleNode node = (SimpleNode) CCJSqlParserUtil.parseAST(query);
        node.jjtAccept(new CCJSqlParserDefaultVisitor() {
            @Override
            public Object visit(SimpleNode node, Object data) {
                if (node.getId() == CCJSqlParserTreeConstants.JJTCOLUMN) {
                    String column = node.jjtGetValue().toString().toLowerCase();
                    String replacement = getReplacementString(column);
                    map.put(column, replacement);
                } else {                    
                    if (node.jjtGetValue() != null) {
                        String column = node.jjtGetValue().toString().toLowerCase();
                        String replacement = getReplacementString(column);
                        map.put(column, replacement);
                    }
                }
                return super.visit(node, data);
            }
        }, null);

        for (String key : map.keySet()) {
            query = query.replace(key, map.get(key));
        }

        return query;
    }

    String getReplacementString(String string) {
        for (Map.Entry<String, String> entry : TPCH_TABLE_MAP.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            string = string.replaceFirst(key, value + ".");
        }

        return string;
    }


}