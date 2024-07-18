package token;

// Define token.Token class
public class Token {
    public TokenType type;
    public String value;

    public TokenType getType() {
        return type;
    }
    public String getValue() {
        return value;
    }
    public Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }
}
