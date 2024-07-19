import ast.*;
import ast.AbstractNode;
import ast.ConditionNode;
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
        List<AbstractNode> statements = new ArrayList<>();
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

    private AbstractNode parseStatement() {
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

    private AbstractNode parsePrint() {
        consume(TokenType.PRINT);
        AbstractNode value = parseExpression();
        return new PrintNode(value);
    }

    private AbstractNode parseSet() {
        consume(TokenType.SET);
        IdentifierNode variable = parseIdentifier();
        consume(TokenType.TO);
        AbstractNode value = parseExpression();
        return new SetNode(variable, value);
    }

    private AbstractNode parseRead() {
        consume(TokenType.READ);
        AbstractNode filename = parseExpression();
        consume(TokenType.TO);
        IdentifierNode variable = parseIdentifier();
        return new ReadNode(filename, variable);
    }

    private AbstractNode parseWrite() {
        consume(TokenType.WRITE);
        AbstractNode content = parseExpression();
        consume(TokenType.TO);
        AbstractNode filename = parseExpression();
        return new WriteNode(content, filename);
    }

    private AbstractNode parseAppend() {
        consume(TokenType.APPEND);
        AbstractNode content = parseExpression();
        consume(TokenType.TO);
        AbstractNode filename = parseExpression();
        return new AppendNode(content, filename);
    }

    private AbstractNode parseFor() {
        consume(TokenType.FOR);
        IdentifierNode variable = parseIdentifier();
        // check if is iterator or for
        if (check(TokenType.IN)) {
            consume(TokenType.IN);
            AbstractNode iterator = parseExpression();
            consume(TokenType.DO);
            expectAndConsumeNewLines();
            List<AbstractNode> body = new ArrayList<>();
            while (!check(TokenType.ENDFOR)) {
                body.add(parseStatement());
                expectAndConsumeNewLines();
            }
            consume(TokenType.ENDFOR);
            return new IteratorNode(variable, iterator, body);
        }
        consume(TokenType.FROM);
        AbstractNode start = parseExpression();
        consume(TokenType.TO);
        AbstractNode end = parseExpression();
        consume(TokenType.DO);
        expectAndConsumeNewLines();
        List<AbstractNode> body = new ArrayList<>();
        while (!check(TokenType.ENDFOR)) {
            body.add(parseStatement());
            expectAndConsumeNewLines();
        }
        consume(TokenType.ENDFOR);
        return new ast.ForNode(variable, start, end, body);
    }


    private AbstractNode parseIf() {
        consume(TokenType.IF);
        AbstractNode condition = parseCondition();
        consume(TokenType.THEN);
        expectAndConsumeNewLines();
        List<AbstractNode> thenBody = new ArrayList<>();
        List<AbstractNode> elsebody = new ArrayList<>();
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
        return new IfNode(condition, thenBody, elsebody);
    }

    private AbstractNode parseSplit() {
        consume(TokenType.SPLIT);
        AbstractNode variable = parseExpression();
        consume(TokenType.BY);
        AbstractNode delimiter = parseExpression();
        consume(TokenType.TO);
        IdentifierNode target = parseIdentifier();
        return new SplitNode(variable, delimiter, target);
    }

    private AbstractNode parseCondition() {
        AbstractNode right;
        AbstractNode left = parseExpression();
        TokenType operator = peek().getType();
        switch (operator) {
            case OPERATOR:
                consume(TokenType.OPERATOR).getValue();
                right = parseExpression();
                return new ConditionNode(left, peek().getValue(), right);
            case MATCHES:
                consume(TokenType.MATCHES);
                right = parseRegexLiteral();
                return new ConditionNode(left, "MATCHES", right);
            case EQUALS:
                consume(TokenType.EQUALS);
                right = parseExpression();
                return new ConditionNode(left, "EQUALS", right);
            default:
                throw new RuntimeException("Condition expected.");
        }
    }

    private AbstractNode parseExpression() {
        AbstractNode expression = parseAdditionSubtraction();
        // check if cast
        if (match(TokenType.AS)) {
            // get type
            if (match(TokenType.NUMBER)) {
                return new AsNode(expression, TokenType.NUMBER);
            } else if (match(TokenType.STRING)) {
                return new AsNode(expression, TokenType.STRING);
            } else if (match(TokenType.ARRAY)) {
                return new AsNode(expression, TokenType.ARRAY);
            }
            throw new RuntimeException("Cannt cast to " + peek().getType());
        }
        return expression;
    }

    private AbstractNode parseAdditionSubtraction() {
        AbstractNode left = parseMultiplicationDivision();

        while (matchOperator("+", "-")) {
            String operator = previous().getValue();
            AbstractNode right = parseMultiplicationDivision();
            left = new BinaryOperationNode(left, operator, right);
        }

        return left;
    }

    private AbstractNode parseMultiplicationDivision() {
        AbstractNode left = parsePrimary();

        while (matchOperator("*", "/")) {
            String operator = previous().getValue();
            AbstractNode right = parsePrimary();
            left = new BinaryOperationNode(left, operator, right);
        }

        return left;
    }

    private AbstractNode parsePrimary() {
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

    private NegativeNode parseNegative() {
        consume(TokenType.OPERATOR);
        AbstractNode value = parseExpression();
        return new NegativeNode(value);
    }

    private AbstractNode parseGrouping() {
        consume(TokenType.LEFT_PAREN); // Consume '('
        AbstractNode expression = parseExpression();
        if (!match(TokenType.RIGHT_PAREN)) {
            throw new RuntimeException("Expected ')' after expression");
        }
        return expression;
    }

    private SubstringNode parseSubstringExpression() {
        consume(TokenType.SUBSTRING);
        // get variable
        AbstractNode variable = parseExpression();
        consume(TokenType.FROM);
        AbstractNode from = parseExpression();
        consume(TokenType.TO);
        AbstractNode to = parseExpression();
        return new SubstringNode(variable, from, to);
    }

    private SortNode parseSortExpression() {
        consume(TokenType.SORT);
        AbstractNode value = parseExpression();
        return new SortNode(value);
    }

    private ReverseNode parseReverseExpression() {
        consume(TokenType.REVERSE);
        AbstractNode value = parseExpression();
        return new ReverseNode(value);
    }

    private UppercaseNode parseUppercaseExpression() {
        consume(TokenType.UPPERCASE);
        AbstractNode right = parseExpression();
        return new UppercaseNode(right);
    }

    private LowercaseNode parseLowercaseExpression() {
        consume(TokenType.LOWERCASE);
        AbstractNode right = parseExpression();
        return new LowercaseNode(right);
    }

    private TrimNode parseTrimExpression() {
        consume(TokenType.TRIM);
        AbstractNode value = parseExpression();
        return new TrimNode(value);
    }

    private ArrayLiteralNode parseArrayLiteral() {
        consume(TokenType.LEFT_BRACKET);
        List<AbstractNode> array = new ArrayList<>();

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
        if (match(TokenType.LEFT_BRACKET)) {
            // get the accessor
            AbstractNode accessor = parseExpression();
            consume(TokenType.RIGHT_BRACKET);
            return new IdentifierNode(variable, accessor);
        }
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

    private AbstractNode parseLengthExpression() {
        consume(TokenType.LENGTH);
        AbstractNode node = parsePrimary();
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
