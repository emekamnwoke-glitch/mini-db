package minidb;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class LexerTest {

    private List<Lexer.Token> tokenize(String input) {
        return new Lexer(input).tokenize();
    }

    @Test
    void testSelectStar() {
        List<Lexer.Token> tokens = tokenize("SELECT * FROM users");
        assertEquals(Lexer.TokenType.KEYWORD,    tokens.get(0).type);
        assertEquals("SELECT",                   tokens.get(0).value);
        assertEquals(Lexer.TokenType.STAR,       tokens.get(1).type);
        assertEquals(Lexer.TokenType.KEYWORD,    tokens.get(2).type);
        assertEquals("FROM",                     tokens.get(2).value);
        assertEquals(Lexer.TokenType.IDENTIFIER, tokens.get(3).type);
        assertEquals("USERS",                    tokens.get(3).value);
    }

    @Test
    void testInsertWithStringAndInt() {
        List<Lexer.Token> tokens = tokenize("INSERT INTO users VALUES (1, 'Alice')");
        assertEquals(Lexer.TokenType.INTEGER, tokens.get(5).type);
        assertEquals("1",                    tokens.get(5).value);
        assertEquals(Lexer.TokenType.STRING,  tokens.get(7).type);
        assertEquals("Alice",                tokens.get(7).value);
    }

    @Test
    void testCreateTable() {
        List<Lexer.Token> tokens = tokenize("CREATE TABLE users (id INT, name TEXT)");
        assertEquals("CREATE", tokens.get(0).value);
        assertEquals("TABLE",  tokens.get(1).value);
        assertEquals("USERS",  tokens.get(2).value);
    }

    @Test
    void testUnexpectedCharacterThrows() {
        assertThrows(RuntimeException.class, () -> tokenize("SELECT @ FROM users"));
    }
}
