package minidb;

import java.util.ArrayList;
import java.util.List;

public class Lexer {

    public enum TokenType {
        KEYWORD, IDENTIFIER, INTEGER, STRING, COMMA, LPAREN, RPAREN, STAR, EQUALS, EOF
    }

    public static class Token {
        public final TokenType type;
        public final String value;

        public Token(TokenType type, String value) {
            this.type = type;
            this.value = value;
        }

        @Override
        public String toString() {
            return String.format("[%s:%s]", type, value);
        }
    }

    private static final java.util.Set<String> KEYWORDS = new java.util.HashSet<>(
        List.of("SELECT", "FROM", "WHERE", "INSERT", "INTO", "VALUES",
                "CREATE", "TABLE", "INT", "TEXT")
    );

    private final String input;
    private int pos;

    public Lexer(String input) {
        this.input = input.trim();
        this.pos = 0;
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();
        while (pos < input.length()) {
            skipWhitespace();
            if (pos >= input.length()) break;

            char c = input.charAt(pos);

            if (c == ',')       { tokens.add(new Token(TokenType.COMMA,   ",")); pos++; }
            else if (c == '(')  { tokens.add(new Token(TokenType.LPAREN,  "(")); pos++; }
            else if (c == ')')  { tokens.add(new Token(TokenType.RPAREN,  ")")); pos++; }
            else if (c == '*')  { tokens.add(new Token(TokenType.STAR,    "*")); pos++; }
            else if (c == '=')  { tokens.add(new Token(TokenType.EQUALS,  "=")); pos++; }
            else if (c == '\'') { tokens.add(readString()); }
            else if (Character.isDigit(c)) { tokens.add(readInteger()); }
            else if (Character.isLetter(c) || c == '_') { tokens.add(readWord()); }
            else { throw new RuntimeException("Unexpected character: " + c); }
        }
        tokens.add(new Token(TokenType.EOF, ""));
        return tokens;
    }

    private void skipWhitespace() {
        while (pos < input.length() && Character.isWhitespace(input.charAt(pos))) pos++;
    }

    private Token readWord() {
        int start = pos;
        while (pos < input.length() && (Character.isLetterOrDigit(input.charAt(pos)) || input.charAt(pos) == '_')) pos++;
        String word = input.substring(start, pos).toUpperCase();
        TokenType type = KEYWORDS.contains(word) ? TokenType.KEYWORD : TokenType.IDENTIFIER;
        return new Token(type, word);
    }

    private Token readInteger() {
        int start = pos;
        while (pos < input.length() && Character.isDigit(input.charAt(pos))) pos++;
        return new Token(TokenType.INTEGER, input.substring(start, pos));
    }

    private Token readString() {
        pos++; // skip opening quote
        int start = pos;
        while (pos < input.length() && input.charAt(pos) != '\'') pos++;
        String value = input.substring(start, pos);
        pos++; // skip closing quote
        return new Token(TokenType.STRING, value);
    }
}
