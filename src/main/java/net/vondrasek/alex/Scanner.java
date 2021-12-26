package net.vondrasek.alex;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author berze
 */
public class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    
    public Scanner(String source) {
        this.source = source;
    }
    
    public List<Token> scanTokens() {
        return tokens;
    }
}
