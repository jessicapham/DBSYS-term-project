import java.util.*;

public class Column {
    String name;
    int alias;

    Column(String n) {
        name = n;
        alias = 0;
    }

    void setAlias(int a) {
      alias = a;
    }

    int getAlias() {
      return alias;
    }

    String getName() {
      return name;
    }
}