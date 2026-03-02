package minidb;

import java.util.List;

public class AST {

    public interface Statement {}

    public static class CreateTableStatement implements Statement {
        public final String tableName;
        public final List<ColumnDef> columns;

        public CreateTableStatement(String tableName, List<ColumnDef> columns) {
            this.tableName = tableName;
            this.columns = columns;
        }
    }

    public static class InsertStatement implements Statement {
        public final String tableName;
        public final List<String> values;

        public InsertStatement(String tableName, List<String> values) {
            this.tableName = tableName;
            this.values = values;
        }
    }

    public static class SelectStatement implements Statement {
        public final List<String> columns; // "*" means all
        public final String tableName;
        public final WhereClause where;    // null means no WHERE

        public SelectStatement(List<String> columns, String tableName, WhereClause where) {
            this.columns = columns;
            this.tableName = tableName;
            this.where = where;
        }
    }

    public static class ColumnDef {
        public final String name;
        public final String type; // INT or TEXT

        public ColumnDef(String name, String type) {
            this.name = name;
            this.type = type;
        }
    }

    public static class WhereClause {
        public final String column;
        public final String value;

        public WhereClause(String column, String value) {
            this.column = column;
            this.value = value;
        }
    }
}
