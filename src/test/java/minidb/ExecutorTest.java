package minidb;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ExecutorTest {

    private Storage storage;
    private Executor executor;

    @BeforeEach
    void setup() {
        storage = new Storage();
        executor = new Executor(storage);
    }

    private String run(String sql) {
        List<Lexer.Token> tokens = new Lexer(sql).tokenize();
        AST.Statement stmt = new Parser(tokens).parse();
        return executor.execute(stmt);
    }

    @Test
    void testCreateAndInsertAndSelect() {
        run("CREATE TABLE users (id INT, name TEXT)");
        run("INSERT INTO users VALUES (1, 'Alice')");
        run("INSERT INTO users VALUES (2, 'Bob')");
        String result = run("SELECT * FROM users");
        assertTrue(result.contains("Alice"));
        assertTrue(result.contains("Bob"));
        assertTrue(result.contains("2 rows"));
    }

    @Test
    void testSelectWithWhere() {
        run("CREATE TABLE users (id INT, name TEXT)");
        run("INSERT INTO users VALUES (1, 'Alice')");
        run("INSERT INTO users VALUES (2, 'Bob')");
        String result = run("SELECT name FROM users WHERE id = 1");
        assertTrue(result.contains("Alice"));
        assertFalse(result.contains("Bob"));
        assertTrue(result.contains("1 row)"));
    }

    @Test
    void testSelectSpecificColumn() {
        run("CREATE TABLE users (id INT, name TEXT)");
        run("INSERT INTO users VALUES (42, 'Carol')");
        String result = run("SELECT name FROM users");
        assertTrue(result.contains("Carol"));
        assertFalse(result.contains("42"));
    }

    @Test
    void testDuplicateTableThrows() {
        run("CREATE TABLE users (id INT, name TEXT)");
        assertThrows(RuntimeException.class, () -> run("CREATE TABLE users (id INT)"));
    }

    @Test
    void testInsertWrongColumnCount() {
        run("CREATE TABLE users (id INT, name TEXT)");
        assertThrows(RuntimeException.class, () -> run("INSERT INTO users VALUES (1)"));
    }

    @Test
    void testSelectFromNonExistentTable() {
        assertThrows(RuntimeException.class, () -> run("SELECT * FROM ghost"));
    }
}
