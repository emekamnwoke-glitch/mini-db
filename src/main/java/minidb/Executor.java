package minidb;

import java.util.ArrayList;
import java.util.List;

public class Executor {

    private final Storage storage;

    public Executor(Storage storage) {
        this.storage = storage;
    }

    public String execute(AST.Statement stmt) {
        if (stmt instanceof AST.CreateTableStatement s) return executeCreate(s);
        if (stmt instanceof AST.InsertStatement s)      return executeInsert(s);
        if (stmt instanceof AST.SelectStatement s)      return executeSelect(s);
        throw new RuntimeException("Unknown statement type");
    }

    private String executeCreate(AST.CreateTableStatement stmt) {
        storage.createTable(stmt.tableName, stmt.columns);
        return "Table '" + stmt.tableName + "' created.";
    }

    private String executeInsert(AST.InsertStatement stmt) {
        storage.insertRow(stmt.tableName, stmt.values);
        return "1 row inserted.";
    }

    private String executeSelect(AST.SelectStatement stmt) {
        Storage.Table table = storage.getTable(stmt.tableName);

        // Resolve which columns to display
        List<String> colNames = new ArrayList<>();
        List<Integer> colIndexes = new ArrayList<>();
        if (stmt.columns.size() == 1 && stmt.columns.get(0).equals("*")) {
            for (int i = 0; i < table.schema.size(); i++) {
                colNames.add(table.schema.get(i).name);
                colIndexes.add(i);
            }
        } else {
            for (String col : stmt.columns) {
                colNames.add(col);
                colIndexes.add(table.columnIndex(col));
            }
        }

        // Build output
        StringBuilder sb = new StringBuilder();
        sb.append(String.join(" | ", colNames)).append("\n");
        sb.append("-".repeat(colNames.stream().mapToInt(String::length).sum() + 3 * (colNames.size() - 1))).append("\n");

        int matched = 0;
        for (List<String> row : table.rows) {
            if (stmt.where != null) {
                int whereIdx = table.columnIndex(stmt.where.column);
                if (!row.get(whereIdx).equalsIgnoreCase(stmt.where.value)) continue;
            }
            List<String> displayRow = new ArrayList<>();
            for (int idx : colIndexes) displayRow.add(row.get(idx));
            sb.append(String.join(" | ", displayRow)).append("\n");
            matched++;
        }

        sb.append("(").append(matched).append(" row").append(matched == 1 ? "" : "s").append(")");
        return sb.toString();
    }
}
