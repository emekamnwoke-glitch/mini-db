package minidb;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {

    private AST.Statement parse(String sql) {
        List<Lexer.Token> tokens = new Lexer(sql).tokenize();
        return new Parser(tokens).parse();
    }

    @Test
    void testParseSelectStar() {
        AST.SelectStatement s = (AST.SelectStatement) parse("SELECT * FROM users");
        assertEquals(List.of("*"), s.columns);
        assertEquals("USERS", s.tableName);
        assertNull(s.where);
    }

    @Test
    void testParseSelectWithWhere() {
        AST.SelectStatement s = (AST.SelectStatement) parse("SELECT name FROM users WHERE id = 1");
        assertEquals(List.of("NAME"), s.columns);
        assertNotNull(s.where);
        assertEquals("ID",  s.where.column);
        assertEquals("1",   s.where.value);
    }

    @Test
    void testParseInsert() {
        AST.InsertStatement s = (AST.InsertStatement) parse("INSERT INTO users VALUES (1, 'Alice')");
        assertEquals("USERS",              s.tableName);
        assertEquals(List.of("1", "Alice"), s.values);
    }

    @Test
    void testParseCreateTable() {
        AST.CreateTableStatement s = (AST.CreateTableStatement) parse("CREATE TABLE users (id INT, name TEXT)");
        assertEquals("USERS",  s.tableName);
        assertEquals(2,        s.columns.size());
        assertEquals("ID",     s.columns.get(0).name);
        assertEquals("INT",    s.columns.get(0).type);
        assertEquals("NAME",   s.columns.get(1).name);
        assertEquals("TEXT",   s.columns.get(1).type);
    }
}
