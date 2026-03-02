package minidb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Storage {

    public static class Table {
        public final String name;
        public final List<AST.ColumnDef> schema;
        public final List<List<String>> rows;

        public Table(String name, List<AST.ColumnDef> schema) {
            this.name = name;
            this.schema = schema;
            this.rows = new ArrayList<>();
        }

        public int columnIndex(String colName) {
            for (int i = 0; i < schema.size(); i++) {
                if (schema.get(i).name.equalsIgnoreCase(colName)) return i;
            }
            throw new RuntimeException("Column not found: " + colName);
        }
    }

    private final Map<String, Table> tables = new HashMap<>();

    public void createTable(String name, List<AST.ColumnDef> schema) {
        if (tables.containsKey(name.toUpperCase()))
            throw new RuntimeException("Table already exists: " + name);
        tables.put(name.toUpperCase(), new Table(name, schema));
    }

    public Table getTable(String name) {
        Table t = tables.get(name.toUpperCase());
        if (t == null) throw new RuntimeException("Table not found: " + name);
        return t;
    }

    public void insertRow(String tableName, List<String> values) {
        Table t = getTable(tableName);
        if (values.size() != t.schema.size())
            throw new RuntimeException("Expected " + t.schema.size() + " values, got " + values.size());
        t.rows.add(new ArrayList<>(values));
    }
}
