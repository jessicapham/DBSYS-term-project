import java.util.*;
import java.util.stream.Collectors;

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

    String processTPCHQuery(String query) {
        for (String key : TPCH_TABLE_MAP.keySet()) {
            query = query.replace(key, TPCH_TABLE_MAP.get(key) + "." + key);
        }

        return query;
    }
}