package net.vondrasek.alex;

import java.util.ArrayList;
import java.util.List;
import static net.vondrasek.alex.TokenType.*;

/**
 *
 * @author berze
 */
public class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private int line = 1;
    
    public Scanner(String source) {
        this.source = source;
    }
    
    public List<Token> scanTokens() {
        while (!isAtEnd()) {
            // beginning of the next lexeme
            start = current;
            scanToken();
        }
        
        tokens.add(new Token(EOF, "", null, line));
        return tokens;
    }
    
    private void scanToken() {
        char c = advance();
        switch (c) {
            case '(' -> addToken(LEFT_PAREN);
            case ')' -> addToken(RIGHT_PAREN);
            case '{' -> addToken(LEFT_BRACE);
            case '}' -> addToken(RIGHT_BRACE);
            case ',' -> addToken(COMMA);
            case '.' -> addToken(DOT);
            case '-' -> addToken(MINUS);
            case '+' -> addToken(PLUS);
            case ';' -> addToken(SEMICOLON);
            case '*' -> addToken(STAR); 
            case '!' -> addToken(match('=') ? BANG_EQUAL : BANG);
            case '=' -> addToken(match('=') ? EQUAL_EQUAL : EQUAL);
            case '<' -> addToken(match('=') ? LESS_EQUAL : LESS);
            case '>' -> addToken(match('=') ? GREATER_EQUAL : GREATER);
            case '/' -> {
                if (match('/')) {
                    // a comment goes until the end of the line
                    while (peek() != '\n' && !isAtEnd()) {
                        advance();
                    }
                } else {
                    addToken(SLASH);
                }
            }
            case ' ', '\r', '\t' -> { /* ignore whitespacs */ }
            case '\n' -> line++;
            case '"' -> string();
            
            default -> Alex.error(line, "Unexpected character.");
        }
    }
    
    private boolean match(char expected) {
        if (isAtEnd()) return false;
        if (source.charAt(current) != expected) return false;
        
        current++;
        return true;
    }
    
    private char peek() {
        if (isAtEnd()) {
            return '\0';
        }
        
        return source.charAt(current);
    }
    
    private char advance() {
        return source.charAt(current++);
    }
    
    private void addToken(TokenType type) {
        addToken(type, null);
    }
    
    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }
    
    private boolean isAtEnd() {
        return current >= source.length();
    }

    private void string() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') {
                line++;
            }
            
            advance();
        }
        
        if (isAtEnd()) {
            Alex.error(line, "Unterminated string.");
            return;
        }
        
        // the closing "
        advance();
        
        // trim the surrounding quotes
        String value = source.substring(start + 1, current - 1);
        addToken(STRING, value);
    }
}
