import ast.*;
import ast.INode;
import ast.IfNode;
import ast.LengthNode;
import ast.ProgramNode;
import ast.ReadNode;
import ast.SplitNode;
import ast.WriteNode;
import token.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import token.TokenType;

public class Parser {
    private List<Token> tokens;
    private int position;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public ProgramNode parse() {
        List<INode> statements = new ArrayList<>();
        // remove begining newlines
        while (!isAtEnd() && match(TokenType.NEWLINE));
        while (!isAtEnd()) {
            statements.add(parseStatement());
            // check for newline here
            if (!check(TokenType.NEWLINE) && !isAtEnd()) {
                throw new RuntimeException("Expected newline after statement.");
            }
            // skip all newlines
            while (!isAtEnd() && match(TokenType.NEWLINE));
        }
        // attach other information about the program here
        ProgramNode program = new ProgramNode(statements);

        return program;
    }

    private INode parseStatement() {
        Token token = peek();
        switch (token.getType()) {
            case READ:
                return parseRead();
            case WRITE:
                return parseWrite();
            case APPEND:
                return parseAppend();
            case FOR:
                return parseFor();
            case IF:
                return parseIf();
            case SPLIT:
                return parseSplit();
            case SET:
                return parseSet();
            case PRINT:
                return parsePrint();
            // Handle other statements
            default:
                throw new RuntimeException("Unexpected token: " + token.getValue());
        }
    }

    private INode parsePrint() {
        consume(TokenType.PRINT);
        INode value = parseExpression();
        return new PrintNode(value);
    }

    private INode parseSet() {
        consume(TokenType.SET);
        IdentifierNode variable = parseIdentifier();
        consume(TokenType.TO);
        INode value = parseExpression();
        return new SetNode(variable, value);
    }

    private INode parseRead() {
        consume(TokenType.READ);
        INode filename = parseExpression();
        consume(TokenType.TO);
        IdentifierNode variable = parseIdentifier();
        return new ReadNode(filename, variable);
    }

    private INode parseWrite() {
        consume(TokenType.WRITE);
        INode content = parseExpression();
        consume(TokenType.TO);
        INode filename = parseExpression();
        return new WriteNode(content, filename);
    }

    private INode parseAppend() {
        consume(TokenType.APPEND);
        INode content = parseExpression();
        consume(TokenType.TO);
        INode filename = parseExpression();
        return new AppendNode(content, filename);
    }

    private INode parseFor() {
        consume(TokenType.FOR);
        IdentifierNode variable = parseIdentifier();
        // check if is iterator or for
        if (check(TokenType.IN)) {
            consume(TokenType.IN);
            INode iterator = parseExpression();
            consume(TokenType.DO);
            expectAndConsumeNewLines();
            List<INode> body = new ArrayList<>();
            while (!check(TokenType.ENDFOR)) {
                body.add(parseStatement());
                expectAndConsumeNewLines();
            }
            consume(TokenType.ENDFOR);
            return new IteratorNode(variable, iterator, body);
        }
        consume(TokenType.FROM);
        INode start = parseExpression();
        consume(TokenType.TO);
        INode end = parseExpression();
        consume(TokenType.DO);
        expectAndConsumeNewLines();
        List<INode> body = new ArrayList<>();
        while (!check(TokenType.ENDFOR)) {
            body.add(parseStatement());
            expectAndConsumeNewLines();
        }
        consume(TokenType.ENDFOR);
        return new ast.ForNode(variable, start, end, body);
    }


    private INode parseIf() {
        consume(TokenType.IF);
        INode expression = parseExpression();
        consume(TokenType.THEN);
        expectAndConsumeNewLines();
        List<INode> thenBody = new ArrayList<>();
        List<INode> elsebody = new ArrayList<>();
        // parse THEN body
        while (!check(TokenType.ENDIF) && !check(TokenType.ELSE)) {
            thenBody.add(parseStatement());
            expectAndConsumeNewLines();
        }
        // if ELSE exists, parse ELSE body
        if (match(TokenType.ELSE)) {
            expectAndConsumeNewLines();
            while (!check(TokenType.ENDIF)) {
                elsebody.add(parseStatement());
                expectAndConsumeNewLines();
            }
        }
        consume(TokenType.ENDIF);
        return new IfNode(expression, thenBody, elsebody);
    }

    private INode parseSplit() {
        consume(TokenType.SPLIT);
        INode variable = parseExpression();
        consume(TokenType.BY);
        INode delimiter = parseExpression();
        consume(TokenType.TO);
        IdentifierNode target = parseIdentifier();
        return new SplitNode(variable, delimiter, target);
    }



    private INode parseExpressionModifier(INode left) {
        INode right = left;
        TokenType operator = peek().getType();
        switch (operator) {
            case OPERATOR:
                String op = consume(TokenType.OPERATOR).getValue();
                right = parseExpression();
                return new BinaryOperationNode(left, op, right);
            case MATCHES:
                consume(TokenType.MATCHES);
                right = parseRegexLiteral();
                return new BinaryOperationNode(left, "MATCHES", right);
            case EQUALS:
                consume(TokenType.EQUALS);
                right = parseExpression();
                return new BinaryOperationNode(left, "EQUALS", right);
            case AS:
                while (match(TokenType.AS)) {
                    // get type
                    if (match(TokenType.NUMBER)) {
                        right = new  AsNode(right, TokenType.NUMBER);
                    } else if (match(TokenType.STRING)) {
                        right = new AsNode(right, TokenType.STRING);
                    } else if (match(TokenType.ARRAY)) {
                        right = new AsNode(right, TokenType.ARRAY);
                    } else {
                        throw new RuntimeException("Cannot cast to " + peek().getType());
                    }
                }
                break;
            case LEFT_BRACKET:
                while (match(TokenType.LEFT_BRACKET)) {
                    // get type
                    INode index = parseExpression();
                    consume(TokenType.RIGHT_BRACKET);
                    right = new IndexNode(right, index);
                }
                break;
            default:
               return right;
        }
        return right;
    }

    private INode parseExpression() {
        // get the initial expression
        INode expression = parseAdditionSubtraction();
        // parse expression modifiers (MATCHES, EQUALS, OPERATOR, CAST (AS), ARRAY INDEX)
        // example --> 5+5 AS STRING, 5 is the expression, and we can't see the + or the AS.
        // This is why we have to check expressionModifier, so we capture rest of the expression
        expression = parseExpressionModifier(expression);
        return expression;
    }

    private INode parseAdditionSubtraction() {
        INode left = parseMultiplicationDivision();

        while (matchOperator("+", "-")) {
            String operator = previous().getValue();
            INode right = parseMultiplicationDivision();
            left = new BinaryOperationNode(left, operator, right);
        }

        return left;
    }

    private INode parseMultiplicationDivision() {
        INode left = parsePrimary();

        while (matchOperator("*", "/")) {
            String operator = previous().getValue();
            INode right = parsePrimary();
            left = new BinaryOperationNode(left, operator, right);
        }

        return left;
    }

    private INode parsePrimary() {
        Token token = peek();
        switch (token.getType()) {
            case NUMBER_LITERAL:
                return parseNumberLiteral();
            case STRING_LITERAL:
                return parseStringLiteral();
            case REGEX_LITERAL:
                return parseRegexLiteral();
            case LEFT_BRACKET:
                return parseArrayLiteral();
            case IDENTIFIER:
                return parseIdentifier();
            case LENGTH:
                return parseLengthExpression();
            case TRIM:
                return parseTrimExpression();
            case UPPERCASE:
                return parseUppercaseExpression();
            case LOWERCASE:
                return parseLowercaseExpression();
            case SORT:
                return parseSortExpression();
            case REVERSE:
                return parseReverseExpression();
            case SUBSTRING:
                return parseSubstringExpression();
            case NOT:
                return parseNotExpression();
            case LEFT_PAREN:
                return parseGrouping();
            case OPERATOR:
                if (peek().getValue().equals("-")) {
                    return parseNegative();
                } else {
                    throw new RuntimeException("Unsupported operator " + peek().getValue());
                }
            default:
                throw new RuntimeException("Unexpected token in expression: " + token.getType() + " " + token.getValue());
        }
    }

    private NotNode parseNotExpression() {
        consume(TokenType.NOT);
        INode value = parseExpression();
        return new NotNode(value);
    }

    private NegativeNode parseNegative() {
        consume(TokenType.OPERATOR);
        INode value = parseExpression();
        return new NegativeNode(value);
    }

    private INode parseGrouping() {
        consume(TokenType.LEFT_PAREN); // Consume '('
        INode expression = parseExpression();
        if (!match(TokenType.RIGHT_PAREN)) {
            throw new RuntimeException("Expected ')' after expression");
        }
        return expression;
    }

    private SubstringNode parseSubstringExpression() {
        consume(TokenType.SUBSTRING);
        // get variable
        INode variable = parseExpression();
        consume(TokenType.FROM);
        INode from = parseExpression();
        consume(TokenType.TO);
        INode to = parseExpression();
        return new SubstringNode(variable, from, to);
    }

    private SortNode parseSortExpression() {
        consume(TokenType.SORT);
        INode value = parseExpression();
        return new SortNode(value);
    }

    private ReverseNode parseReverseExpression() {
        consume(TokenType.REVERSE);
        INode value = parseExpression();
        return new ReverseNode(value);
    }

    private UppercaseNode parseUppercaseExpression() {
        consume(TokenType.UPPERCASE);
        INode right = parseExpression();
        return new UppercaseNode(right);
    }

    private LowercaseNode parseLowercaseExpression() {
        consume(TokenType.LOWERCASE);
        INode right = parseExpression();
        return new LowercaseNode(right);
    }

    private TrimNode parseTrimExpression() {
        consume(TokenType.TRIM);
        INode value = parseExpression();
        return new TrimNode(value);
    }

    private ArrayLiteralNode parseArrayLiteral() {
        consume(TokenType.LEFT_BRACKET);
        List<INode> array = new ArrayList<>();

        // get first element in array
        if (!check(TokenType.RIGHT_BRACKET)) {
            array.add(parsePrimary());
        }

        while (!isAtEnd() && !match(TokenType.RIGHT_BRACKET)) {
            if (!match(TokenType.COMMA)) {
                throw new RuntimeException("Expected COMMA token in array");
            }
            array.add(parsePrimary());
        }
        return new ArrayLiteralNode(array);
    }

    private IdentifierNode parseIdentifier() {
        String variable = consume(TokenType.IDENTIFIER).getValue();
        // check if there is an accessor in here
        return new IdentifierNode(variable);
    }

    private RegexLiteralNode parseRegexLiteral() {
        Token token = consume(TokenType.REGEX_LITERAL);
        try {
            Pattern pattern = Pattern.compile(token.getValue());
            return new RegexLiteralNode(pattern);
        } catch (Exception e) {
            throw new RuntimeException("Invalid Regex expression " + token.getValue());
        }
    }

    private NumberLiteralNode parseNumberLiteral() {
        Token token = consume(TokenType.NUMBER_LITERAL);
        return new NumberLiteralNode(Integer.parseInt(token.getValue()));
    }

    private StringLiteralNode parseStringLiteral() {
        Token token = consume(TokenType.STRING_LITERAL);
        return new StringLiteralNode(token.getValue());
    }

    private INode parseLengthExpression() {
        consume(TokenType.LENGTH);
        INode node = parsePrimary();
        return new LengthNode(node);
    }

    private boolean matchOperator(String... values) {
        for (String value : values) {
            if (checkOperator(value)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private boolean match(TokenType type) {
        if (check(type)) {
            advance();
            return true;
        }
        return false;
    }

    private boolean checkOperator(String value) {
        if (isAtEnd()) return false;
        Token token = peek();
        return token.getType() == TokenType.OPERATOR && token.getValue().equals(value);
    }

    private boolean check(TokenType type) {
        return !isAtEnd() && peek().getType() == type;
    }

    private void expectAndConsumeNewLines() {
        // skip all newlines, but expect at least one
        if (!check(TokenType.NEWLINE)) {
            throw new RuntimeException("Expected NEWLINE but found " + peek().getType());
        }
        while (check(TokenType.NEWLINE) && !isAtEnd()) {
            consume(TokenType.NEWLINE);
        }
    }

    private Token consume(TokenType type) {
        if (check(type)) {
            return advance();
        }
        throw new RuntimeException("Expected token " + type + " but found " + peek().getType());
    }

    private Token advance() {
        if (!isAtEnd()) {
            position++;
        }
        return previous();
    }

    private boolean isAtEnd() {
        return tokens.get(position).type == TokenType.EOF;
    }

    private Token peek() {
        return tokens.get(position);
    }

    private Token previous() {
        return tokens.get(position - 1);
    }
}
