package lexer;

import token.Token;
import token.TokenType;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private String code;
    private int position;
    private List<Token> tokens = new ArrayList<Token>();
    public Lexer(String code) {
        this.code = code;
    }

    public List<Token> tokenize() {
        while (position < code.length()) {
            char currentChar = code.charAt(position);

            // Skip whitespace characters
            if (Character.isWhitespace(currentChar) && currentChar != '\n') {
                position++;
                continue;
            }

            // Handle newlines
            if (currentChar == '\n') {
                addToken(TokenType.NEWLINE);
                position++;
                continue;
            }

            // handle comments
            if (currentChar == '#') {
                stripComment();
                continue;
            }
            // Handle keywords and identifiers
            if (Character.isLetter(currentChar)) {
                tokenizeKeywordOrIdentifier();
                continue;
            }

            // Handle numeric literals
            if (Character.isDigit(currentChar)) {
                tokenizeNumberLiteral();
                continue;
            }

            // Handle string literals
            if (currentChar == '"') {
                tokenizeStringLiteral();
                continue;
            }

            // Handle regex literals
            if (currentChar == '/'
                    && code.charAt(position+1) != 'n'
                    && tokens.get(tokens.size()-1).getType()!=TokenType.NUMBER_LITERAL
                    && tokens.get(tokens.size()-1).getType()!=TokenType.RIGHT_PAREN
                    && tokens.get(tokens.size()-1).getType()!=TokenType.RIGHT_BRACKET )  {
                tokenizeRegexLiteral();
                continue;
            }

            // Handle single character tokens
            switch (currentChar) {
                case '+':
                case '-':
                case '*':
                case '>':
                case '<':
                case '^':
                case '/':
                case '=':
                    addToken(TokenType.OPERATOR, String.valueOf(currentChar));
                    break;
                case '(':
                    addToken(TokenType.LEFT_PAREN);
                    break;
                case ')':
                    addToken(TokenType.RIGHT_PAREN);
                    break;
                case '[':
                    addToken(TokenType.LEFT_BRACKET);
                    break;
                case ']':
                    addToken(TokenType.RIGHT_BRACKET);
                    break;
                case ',':
                    addToken(TokenType.COMMA);
                    break;
                default:
                    // Handle unrecognized characters or syntax errors
                    throw new RuntimeException("Unexpected character: " + currentChar);
            }

            position++;
        }

        // Add EOF token at the end
        tokens.add(new Token(TokenType.EOF, ""));

        return tokens;
    }


    // Helper method to add tokens
    private void addToken(TokenType type) {
        tokens.add(new Token(type, ""));
    }

    // Helper method to add tokens with value
    private void addToken(TokenType type, String value) {
        tokens.add(new Token(type, value));
    }

    private void tokenizeRegexLiteral() {
        StringBuilder lexeme = new StringBuilder();

        // Skip opening slash
        position++;
        boolean escaping = false;

        while (position < code.length()) {
            char currentChar = code.charAt(position);

            if (escaping) {
                lexeme.append(currentChar);
                escaping = false;
            } else if (currentChar == '\\') {
                lexeme.append(currentChar);
                escaping = true;
            } else if (currentChar == '/') {
                // End of regex literal
                break;
            } else {
                lexeme.append(currentChar);
            }
            position++;
        }

        // Skip closing slash
        position++;

        addToken(TokenType.REGEX_LITERAL, lexeme.toString());
    }

    // Method to tokenize keywords or identifiers
    private void tokenizeKeywordOrIdentifier() {
        StringBuilder lexeme = new StringBuilder();

        while (position < code.length() && (Character.isLetterOrDigit(code.charAt(position)) || code.charAt(position) == '_')) {
            lexeme.append(code.charAt(position));
            position++;
        }

        String lexemeStr = lexeme.toString().toUpperCase(); // Convert to uppercase for keyword matching

        // Determine token type based on lexeme
        switch (lexemeStr) {
            case "SET":
                addToken(TokenType.SET);
                break;
            case "READ":
                addToken(TokenType.READ);
                break;
            case "APPEND":
                addToken(TokenType.APPEND);
                break;
            case "WRITE":
                addToken(TokenType.WRITE);
                break;
            case "FOR":
                addToken(TokenType.FOR);
                break;
            case "NOT":
                addToken(TokenType.NOT);
                break;
            case "ENDFOR":
                addToken(TokenType.ENDFOR);
                break;
            case "LENGTH":
                addToken(TokenType.LENGTH);
                break;
            case "SPLIT":
                addToken(TokenType.SPLIT);
                break;
            case "BY":
                addToken(TokenType.BY);
                break;
            case "THEN":
                addToken(TokenType.THEN);
                break;
            case "IN":
                addToken(TokenType.IN);
                break;
            case "IF":
                addToken(TokenType.IF);
                break;
            case "ENDIF":
                addToken(TokenType.ENDIF);
                break;
            case "FROM":
                addToken(TokenType.FROM);
                break;
            case "TO":
                addToken(TokenType.TO);
                break;
            case "AS":
                addToken(TokenType.AS);
                break;
            case "NUMBER":
                addToken(TokenType.NUMBER);
                break;
            case "STRING":
                addToken(TokenType.STRING);
                break;
            case "ARRAY":
                addToken(TokenType.ARRAY);
                break;
            case "DO":
                addToken(TokenType.DO);
                break;
            case "CONTAINS":
                addToken(TokenType.CONTAINS);
                break;
            case "MATCHES":
                addToken(TokenType.MATCHES);
                break;
            case "EQUALS":
                addToken(TokenType.EQUALS);
                break;
            case "ELSE":
                addToken(TokenType.ELSE);
                break;
            case "REPLACE":
                addToken(TokenType.REPLACE);
                break;
            case "PRINT":
                addToken(TokenType.PRINT);
                break;
            case "JOIN":
                addToken(TokenType.JOIN);
                break;
            case "TRIM":
                addToken(TokenType.TRIM);
                break;
            case "UPPERCASE":
                addToken(TokenType.UPPERCASE);
                break;
            case "LOWERCASE":
                addToken(TokenType.LOWERCASE);
                break;
            case "SUBSTRING":
                addToken(TokenType.SUBSTRING);
                break;
            case "SORT":
                addToken(TokenType.SORT);
                break;
            case "REVERSE":
                addToken(TokenType.REVERSE);
                break;
            default:
                // Default case assumes it's an identifier
                addToken(TokenType.IDENTIFIER, lexeme.toString());
                break;
        }
    }

    // Method to tokenize number literals
    private void tokenizeNumberLiteral() {
        StringBuilder lexeme = new StringBuilder();

        while (position < code.length() && Character.isDigit(code.charAt(position))) {
            lexeme.append(code.charAt(position));
            position++;
        }

        addToken(TokenType.NUMBER_LITERAL, lexeme.toString());
    }
    // Method to strip comments
    private void stripComment() {
        //Skip #
        position++;
        while (position < code.length() && code.charAt(position) != '\n') {
            position++;
        }
        position++;
    }

    // Method to tokenize string literals
    private void tokenizeStringLiteral() {
        StringBuilder lexeme = new StringBuilder();

        // Skip opening double quote
        position++;

        while (position < code.length() && code.charAt(position) != '"') {
            lexeme.append(code.charAt(position));
            position++;
        }

        // Skip closing double quote
        position++;

        addToken(TokenType.STRING_LITERAL, lexeme.toString().replace("\\n", "\n"));
    }

}
