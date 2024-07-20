import ast.*;
import token.TokenType;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class Interpreter {
    private Map<String, Object> symbolTable = new HashMap<>();
    FileManager fileManager = new FileManager();

    public void execute(ProgramNode program) {
        // open the files before reading or writing to them
        for (INode statement : program.getStatements()) {
            executeStatement(statement);
        }
        try {
            // close all files
            fileManager.closeAllFiles();
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }

    }

    private void executeStatement(INode statement) {
        if (statement instanceof SetNode) {
            executeSetStatement((SetNode) statement);
        } else if (statement instanceof ReadNode) {
            executeReadStatement((ReadNode) statement);
        } else if (statement instanceof WriteNode) {
            executeWriteStatement((WriteNode) statement);
        } else if (statement instanceof AppendNode) {
            executeAppendStatement((AppendNode) statement);
        }else if (statement instanceof ForNode) {
            executeForStatement((ForNode) statement);
        } else if (statement instanceof IteratorNode) {
            executeIteratorStatement((IteratorNode) statement);
        } else if (statement instanceof IfNode) {
            executeIfStatement((IfNode) statement);
        } else if (statement instanceof PrintNode) {
            executePrintStatement((PrintNode) statement);
        } else if (statement instanceof SplitNode) {
            executeSplitStatement((SplitNode) statement);
        }else {
            throw new RuntimeException("Unexpected statement type: " + statement.getClass().getSimpleName());
        }
    }

    private void executeSplitStatement(SplitNode statement) {
        Object left = evaluateExpression(statement.getVariable());
        Object delimiter = evaluateExpression(statement.getDelimiter());
        if (!(left instanceof String || delimiter instanceof String)) {
            throw new RuntimeException("Can only split strings.");
        }
        ArrayList words = new ArrayList<>();
        String[] split = left.toString().split(delimiter.toString());
        for (String word : split) {
            words.add(word);
        }
        symbolTable.put(statement.getTarget().getName(), words);
    }

    private void executeSetStatement(SetNode statement) {
        String variableName = statement.getVariable().getName();
        Object value = evaluateExpression(statement.getValue());
        symbolTable.put(variableName, value);
    }

    private void executeReadStatement(ReadNode statement) {
        String filename = (String) evaluateExpression(statement.getFilename());
        // Here you would read the file contents into a variable
        // For simplicity, let's assume it reads the file and returns its contents as a string
        try {
            String fileContents = readFile(filename);
            symbolTable.put(statement.getVariable().getName(), fileContents);
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    private void executePrintStatement(PrintNode statement) {
        Object value = evaluateExpression(statement.getVariable());
        System.out.println(value);
    }

    private void executeWriteStatement(WriteNode statement) {
        Object value = evaluateExpression(statement.getContent());
        String filename = (String) evaluateExpression(statement.getFilename());
        try {
            // Here you would write the value to the file
            writeFile(filename, value.toString(), false);
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }

    }

    private void executeAppendStatement(AppendNode statement) {
        Object value = evaluateExpression(statement.getContent());
        String filename = (String) evaluateExpression(statement.getFilename());
        try {
            // Here you would append the value to the file
            writeFile(filename, value.toString(), true);
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    private void executeIteratorStatement(IteratorNode statement) {
        Object value = evaluateExpression(statement.getIterator());
        // check if can iterate
        Iterator iterator;
        if (value instanceof String) {
            ArrayList letters = new ArrayList();
            for (Character letter : value.toString().toCharArray()) {
                letters.add(new StringLiteralNode(letter.toString()));
            }
            value = letters;
        }
        if (value instanceof ArrayList<?>) {
            iterator = ((ArrayList<?>) value).iterator();
        } else {
            throw new RuntimeException("Can not iterate over " + value.getClass());
        }

        // iterate over it
        for (Iterator it = iterator; it.hasNext(); ) {
            Object item = it.next();
            symbolTable.put(statement.getVariable().getName(), item);
            for (INode bodyStatement : statement.getBody()) {
                executeStatement(bodyStatement);
            }
        }
    }

    private void executeForStatement(ForNode statement) {
        int start = (int) evaluateExpression(statement.getStart());
        int end = (int) evaluateExpression(statement.getEnd());
        for (int i = start; i < end; i++) {
            symbolTable.put(statement.getVariable().getName(), i);
            for (INode bodyStatement : statement.getBody()) {
                executeStatement(bodyStatement);
            }
        }
    }

    private void executeIfStatement(IfNode statement) {
        Object expression = evaluateExpression(statement.getCondition());

        Boolean valid = expression instanceof Boolean || expression instanceof Integer;
        if (!valid) {
            throw new RuntimeException("Expected BOOLEAN, found " + expression.getClass());
        }
        Boolean cond = expression instanceof Boolean ? (Boolean)expression : (Integer)expression!=0;
        if (cond) {
            for (INode thenStatement : statement.getThenBody()) {
                executeStatement(thenStatement);
            }
        } else {
            for (INode elseStatement : statement.getElseBody()) {
                executeStatement(elseStatement);
            }
        }
    }

    private Object evaluateExpression(INode expression) {
        if (expression instanceof IdentifierNode) {
            String variableName = ((IdentifierNode) expression).getName();
            if (!symbolTable.containsKey(variableName)) {
                throw new RuntimeException("Undeclared variable: " + variableName);
            }
            return symbolTable.get(variableName);
        } else if (expression instanceof NotNode) {
          Object value = evaluateExpression(((NotNode) expression).getValue());
          if (value instanceof Boolean) {
            return !(Boolean)value;
          }
          throw new RuntimeException("Can not apply NOT to " + value.getClass());
        } else if (expression instanceof IndexNode) {
            Object value = evaluateExpression(((IndexNode) expression).getValue());
            Object index = evaluateExpression(((IndexNode) expression).getIndex());
            // check if we can access it
            if (index instanceof Integer) {
                Integer i = (Integer) index;
                try {
                    if (value instanceof ArrayList<?>) {
                        return ((ArrayList<?>) value).get(i);
                    } else if (value instanceof String) {
                        return String.valueOf(((String) value).toCharArray()[i]);
                    }
                } catch (IndexOutOfBoundsException e) {
                    throw new RuntimeException("Index "+i+" out of bounds");
                } catch (Exception e){
                    throw new RuntimeException(e.getLocalizedMessage());
                }
            }
            throw new RuntimeException("Can not index " + value.toString() + " at position ");
        } else if (expression instanceof AsNode) {
            AsNode as = (AsNode) expression;
            Object value = evaluateExpression(as.getValue());
            try {
                if (as.getCast() == TokenType.NUMBER) {
                    // try and convert whatever the object is as a number
                    // can only convert string to number
                    if (value instanceof String) {
                        return Integer.parseInt((String)value);
                    } else if (value instanceof Number) {
                        return value;
                    } else {
                        throw new NumberFormatException();
                    }
                } else if (as.getCast() == TokenType.STRING) {
                    if (value instanceof Number) {
                        return String.valueOf(value);
                    }  else if (value instanceof ArrayList<?>) {
                        // try and combine all of the
                        StringBuilder builder = new StringBuilder();
                        for (Object node : (ArrayList<?>)value) {
                            // check to make sure the node is string_literal
                            builder.append(String.valueOf(node));
                        }
                        return builder.toString();
                    } else if (value instanceof String) {
                        return value;
                    }
                    throw new RuntimeException();
                } else if (as.getCast() == TokenType.ARRAY) {
                    if (value instanceof String) {
                        // convert "hello" to ["h", "e", "l", "l", "o"]
                        char[] chars = ((String) value).toCharArray();
                        ArrayList<Object> res = new ArrayList<>();
                        for (char c : chars) {
                            res.add(String.valueOf(c));
                        }
                        return res;
                    } else if (value instanceof ArrayList<?>) {
                        return value;
                    }
                    throw new RuntimeException();
                }
            } catch (Exception e) {
                throw new RuntimeException("Could not parse \""+value.toString()+"\" to " + as.getCast().name() + ": " + e.getLocalizedMessage());
            }
        }
        else if (expression instanceof StringLiteralNode) {
            return ((StringLiteralNode) expression).getValue();
        } else if (expression instanceof NumberLiteralNode) {
            return ((NumberLiteralNode) expression).getNumber();
        } else if (expression instanceof RegexLiteralNode) {
            return ((RegexLiteralNode) expression).getRegex();
        } else if (expression instanceof ArrayLiteralNode) {
            ArrayList values = new ArrayList();
            for (INode node: ((ArrayList<INode>)((ArrayLiteralNode) expression).getArray())) {
                values.add(evaluateExpression(node));
            }
            return values;
        }  else if (expression instanceof UppercaseNode) {
            Object node = evaluateExpression(((UppercaseNode) expression).getValue());
            if (!(node instanceof String)) {
                throw new RuntimeException("Can only apply UPPERCASE to STRINGS");
            }
            return node.toString().toUpperCase();
        } else if (expression instanceof LowercaseNode) {
            Object node = evaluateExpression(((LowercaseNode) expression).getValue());
            if (!(node instanceof String)) {
                throw new RuntimeException("Can only apply LOWERCASE to STRINGS");
            }
            return node.toString().toLowerCase();
        }else if (expression instanceof TrimNode) {
            Object node = evaluateExpression(((TrimNode) expression).getValue());
            if (!(node instanceof String)) {
                throw new RuntimeException("Can only apply TRIM to STRINGS");
            }
            return node.toString().trim();
        } else if (expression instanceof SortNode) {
            Object value = evaluateExpression(((SortNode) expression).getValue());
            if (value instanceof ArrayList<?>) {
                // reverse array
                ((ArrayList)value).sort(new Comparator() {
                    @Override
                    public int compare(Object o1, Object o2) {
                        // create and execute new compare node
                        INode greater = new BinaryOperationNode((INode) o1, ">", (INode) o2);
                        Boolean greater_result = (Boolean) evaluateExpression(greater);
                        INode equals = new BinaryOperationNode((INode) o1, "EQUALS", (INode) o2);
                        Boolean equals_result = (Boolean) evaluateExpression(equals);
                        if (greater_result) {
                            return 1;
                        } else if (equals_result) {
                            return 0;
                        } else {
                            return -1;
                        }
                    }
                });
                return value;
            } else if (value instanceof String) {
                // sort string
                 char[] chars = ((String) value).toCharArray();
                Arrays.sort(chars);
                return new String(chars);
            }
            throw new RuntimeException("Can only reverse STRING and ARRAY.");
            // check if instance of array

        } else if (expression instanceof ReverseNode) {
            Object value = evaluateExpression(((ReverseNode) expression).getValue());
            // check if value is array
            if (value instanceof String) {
                // return reverse of string
                return new StringBuilder(value.toString()).reverse().toString();
            } else if (value instanceof ArrayList<?>) {
                // return reversed array
                ArrayList result = new ArrayList(((ArrayList)value).reversed());
                return result;
            }
            throw new RuntimeException("Can only reverse STRING and ARRAY.");
        } else if (expression instanceof SubstringNode) {
            Object variable = evaluateExpression(((SubstringNode) expression).getVariable());
            Object start = evaluateExpression(((SubstringNode) expression).getStart());
            Object end = evaluateExpression(((SubstringNode) expression).getEnd());

            // make sure start and end are number
            if (!(start instanceof Integer)) {
                throw new RuntimeException("SUBSTRING FROM index must be integer");
            }
            if (!(end instanceof Integer)) {
                throw new RuntimeException("SUBSTRING TO index must be integer");
            }

            if (variable instanceof ArrayList) {
                List result = ((ArrayList<?>) variable).subList((Integer)start, (Integer)end);
                return new ArrayList<>(result);
            } else if (variable instanceof String) {
                return ((String) variable).substring((Integer)start, (Integer)end);
            }
            throw new RuntimeException("Can only substring STRING and ARRAY.");
        } else if (expression instanceof BinaryOperationNode) {
            BinaryOperationNode binaryOp = (BinaryOperationNode) expression;
            Object left = evaluateExpression(binaryOp.getLeft());
            Object right = evaluateExpression(binaryOp.getRight());
            // we can assume all is the same for everything but string and regex
            switch (binaryOp.getOperator()) {
                case "EQUALS":
                    return left instanceof String && right instanceof String ? left.equals(right) : left == right;
                case "MATCHES":
                    return ((Pattern) right).matcher((String) left).find();
                case ">":
                    return (Integer) left > (Integer) right;
                case "<":
                    return (Integer) left < (Integer) right;
                case "+":
                    return left instanceof String && right instanceof String ? (String) left + right : (Integer) left + (Integer) right;
                case "-":
                    return (Integer) left - (Integer) right;
                case "*":
                    return (Integer) left * (Integer) right;
                case "^":
                    return Math.pow((Integer) left, (Integer) right);
                case "/":
                    return (Integer) left / (Integer) right;
                default:
                    throw new RuntimeException("Type mismatch in binary operation.");
            }
        } else if (expression instanceof NegativeNode) {
            // return the value, but negative
            Object value = evaluateExpression(((NegativeNode) expression).getValue());
            if (value instanceof Integer) {
                return -(Integer)value;
            } else if (value instanceof Float) {
                return -(Float)value;
            }
            throw new RuntimeException("Can not apply - to " + value.getClass());
        } else if (expression instanceof LengthNode) {
            Object inner = evaluateExpression(((LengthNode) expression).getVariable());
            if (inner instanceof String) {
                return ((String) inner).length();
            } else if (inner instanceof List) {
                return ((List<?>) inner).size();
            } else {
                throw new RuntimeException("LENGTH can only be applied to STRING or ARRAY.");
            }
        }
        throw new RuntimeException("Unexpected expression type: " + expression.getClass().getSimpleName());

    }

    private String readFile(String filename) throws IOException {
        // Implement file reading logic here
        return fileManager.readFile(filename);
    }

    private void writeFile(String filename, String content, Boolean append) throws IOException {
        // Implement file writing logic here
        if (append) {
            fileManager.appendFile(filename, content);
        } else {
            fileManager.writeFile(filename, content);
        }
    }

}
