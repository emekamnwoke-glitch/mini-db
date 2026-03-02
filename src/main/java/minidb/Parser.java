package minidb;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    private final List<Lexer.Token> tokens;
    private int pos;

    public Parser(List<Lexer.Token> tokens) {
        this.tokens = tokens;
        this.pos = 0;
    }

    public AST.Statement parse() {
        String keyword = expectKeyword();
        return switch (keyword) {
            case "SELECT" -> parseSelect();
            case "INSERT" -> parseInsert();
            case "CREATE" -> parseCreate();
            default -> throw new RuntimeException("Unknown statement: " + keyword);
        };
    }

    // SELECT <columns> FROM <table> [WHERE <col> = <val>]
    private AST.SelectStatement parseSelect() {
        List<String> columns = new ArrayList<>();
        if (peek().type == Lexer.TokenType.STAR) {
            consume();
            columns.add("*");
        } else {
            columns.add(expectIdentifier());
            while (peek().type == Lexer.TokenType.COMMA) {
                consume();
                columns.add(expectIdentifier());
            }
        }
        expectKeywordValue("FROM");
        String table = expectIdentifier();
        AST.WhereClause where = null;
        if (peek().type == Lexer.TokenType.KEYWORD && peek().value.equals("WHERE")) {
            consume();
            String col = expectIdentifier();
            expect(Lexer.TokenType.EQUALS);
            String val = expectValue();
            where = new AST.WhereClause(col, val);
        }
        return new AST.SelectStatement(columns, table, where);
    }

    // INSERT INTO <table> VALUES (<val1>, <val2>, ...)
    private AST.InsertStatement parseInsert() {
        expectKeywordValue("INTO");
        String table = expectIdentifier();
        expectKeywordValue("VALUES");
        expect(Lexer.TokenType.LPAREN);
        List<String> values = new ArrayList<>();
        values.add(expectValue());
        while (peek().type == Lexer.TokenType.COMMA) {
            consume();
            values.add(expectValue());
        }
        expect(Lexer.TokenType.RPAREN);
        return new AST.InsertStatement(table, values);
    }

    // CREATE TABLE <name> (<col> <type>, ...)
    private AST.CreateTableStatement parseCreate() {
        expectKeywordValue("TABLE");
        String table = expectIdentifier();
        expect(Lexer.TokenType.LPAREN);
        List<AST.ColumnDef> cols = new ArrayList<>();
        cols.add(parseColumnDef());
        while (peek().type == Lexer.TokenType.COMMA) {
            consume();
            cols.add(parseColumnDef());
        }
        expect(Lexer.TokenType.RPAREN);
        return new AST.CreateTableStatement(table, cols);
    }

    private AST.ColumnDef parseColumnDef() {
        String name = expectIdentifier();
        String type = expectKeyword(); // INT or TEXT
        return new AST.ColumnDef(name, type);
    }

    // --- Helpers ---

    private Lexer.Token peek() {
        return tokens.get(pos);
    }

    private Lexer.Token consume() {
        return tokens.get(pos++);
    }

    private void expect(Lexer.TokenType type) {
        Lexer.Token t = consume();
        if (t.type != type) throw new RuntimeException("Expected " + type + " but got " + t);
    }

    private String expectKeyword() {
        Lexer.Token t = consume();
        if (t.type != Lexer.TokenType.KEYWORD) throw new RuntimeException("Expected keyword, got: " + t);
        return t.value;
    }

    private void expectKeywordValue(String value) {
        String kw = expectKeyword();
        if (!kw.equals(value)) throw new RuntimeException("Expected " + value + " but got " + kw);
    }

    private String expectIdentifier() {
        Lexer.Token t = consume();
        if (t.type != Lexer.TokenType.IDENTIFIER && t.type != Lexer.TokenType.KEYWORD)
            throw new RuntimeException("Expected identifier, got: " + t);
        return t.value;
    }

    private String expectValue() {
        Lexer.Token t = consume();
        if (t.type == Lexer.TokenType.INTEGER || t.type == Lexer.TokenType.STRING)
            return t.value;
        throw new RuntimeException("Expected a value, got: " + t);
    }
}
