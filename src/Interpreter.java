import ast.*;
import core.Page;
import core.Pointer;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.*;
import java.util.regex.Pattern;

public class Interpreter {

    private boolean AUTONEWLINE = false;
    private final Page internalPage = new Page();
    private final Page publicPage = new Page();
    final FileManager fileManager = new FileManager();

    public Interpreter() {}
    public Interpreter(boolean autoNewLine) {
        AUTONEWLINE = autoNewLine;
    }

    public void execute(ProgramNode program) throws IOException {
        // open the files before reading or writing to them
        for (INode statement : program.getStatements()) {
            executeStatement(statement);
        }

        // close all files
        fileManager.closeAllFiles();
    }

    private void executeStatement(INode statement) throws IOException {
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
        } else if (statement instanceof SortNode) {
            executeSortStatement((SortNode) statement);
        } else {
            evaluateExpression(statement);
        }
    }

    private Object executeSortStatement(SortNode statement) {
        // sort the value of it or something
        Object value = resolvePointers(evaluateExpression(((SortNode) statement).getValue()));
        if (value instanceof ArrayList) {
            // sort array
            Collections.sort((ArrayList)value);
            return value;
        } else if (value instanceof String) {
            // sort string
            char[] chars = ((String) value).toCharArray();
            Arrays.sort(chars);
            return new String(chars);
        }
        throw new RuntimeException("Can only reverse STRING and ARRAY.");
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

        Object target = getVariableNameOrEvaluate(statement.getTarget());
        setPublicVariable(target, words);
    }

    /**
     * Used to retrieve the value of a variable for reading
     * @param variable
     * @return Object
     */
    private Object getVariableOrEvaluate(INode variable) {
        if (variable instanceof IdentifierNode) {
            return getVariable(((IdentifierNode) variable).getName());
        }
        return evaluateExpression(variable);
    }

    /**
     * Used for retrieving a variable name or type to write to.
     * @param variable
     * @return Object
     */
    private Object getVariableNameOrEvaluate(INode variable) {
        if (variable instanceof IdentifierNode) {
            return ((IdentifierNode) variable).getName();
        }
        return evaluateExpression(variable);
    }

    private void executeSetStatement(SetNode statement) {
        Object variableName = statement.getVariable();
        if (variableName instanceof IndexNode) {
            variableName = evaluateExpression((IndexNode)variableName);
        } else if (variableName instanceof IdentifierNode) {
            variableName = ((IdentifierNode) variableName).getName();
        }
        Object value = evaluateExpression(statement.getValue());    // str num or ptr
        setPublicVariable(variableName, value);
    }

    private void executeReadStatement(ReadNode statement) throws IOException {
        String filename = (String) evaluateExpression(statement.getFilename());
        // Here you would read the file contents into a variable
        // For simplicity, let's assume it reads the file and returns its contents as a string

        String fileContents = readFile(filename);
        Object variable;
        if (statement.getVariable() instanceof IdentifierNode) {
            variable = ((IdentifierNode) statement.getVariable()).getName();
        } else {
            variable = resolvePointers(evaluateExpression(statement.getVariable()));
        }
        setPublicVariable(variable, fileContents);
    }

    private void executePrintStatement(PrintNode statement) {
        Object value = evaluateExpression(statement.getVariable());
        value = resolvePointers(value);
        if (AUTONEWLINE) {
            System.out.println(value.toString());
        } else {
            System.out.printf(value.toString());
        }

    }

    private void executeWriteStatement(WriteNode statement) {
        Object value = evaluateExpression(statement.getContent());
        String filename = (String) evaluateExpression(statement.getFilename());
        try {
            String output = value.toString();
            if (AUTONEWLINE) {
                output += "\n";
            }
            // Here you would write the value to the file
            writeFile(filename, output, false);
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }

    }

    private void executeAppendStatement(AppendNode statement) {
        Object value = evaluateExpression(statement.getContent());
        String filename = (String) evaluateExpression(statement.getFilename());
        try {
            String output = value.toString();
            if (AUTONEWLINE) {
                output += "\n";
            }
            // Here you would append the value to the file
            writeFile(filename, output, true);
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    private void executeIteratorStatement(IteratorNode statement) throws IOException {
        // evaluates to either the pointer location or string of variable
        Object target = resolvePointers(getVariableOrEvaluate(statement.getIterator()));

        Iterator iterator;
        if (target instanceof String) {
            ArrayList letters = new ArrayList();
            for (Character letter : target.toString().toCharArray()) {
                letters.add(letter.toString());
            }
            target = letters;
        }
        if (target instanceof ArrayList<?>) {
            iterator = ((ArrayList<?>) target).iterator();
        } else {
            throw new RuntimeException("Can not iterate over " + target.getClass());
        }

        // Make sure iterator loops assign loop variable only to variable (cannot do FOR x[0] IN [1, 2, 3] DO)
        Object variable = statement.getVariable();
        if (!(variable instanceof IdentifierNode)) {
            throw new InvalidObjectException("Can assign iterator loop value outside of a variable.");
        }
        String name = ((IdentifierNode) variable).getName();
        // iterate over the iterator and assign local var
        for (Iterator it = iterator; it.hasNext(); ) {
            Object item = it.next();
            setPublicVariable(name, item);
            for (INode bodyStatement : statement.getBody()) {
                executeStatement(bodyStatement);
            }
        }
    }

    private void executeForStatement(ForNode statement) throws IOException {
        int start = (int) evaluateExpression(statement.getStart());
        int end = (int) evaluateExpression(statement.getEnd());
        // Make sure iterator loops assign loop variable only to variable (cannot do FOR x[0] IN [1, 2, 3] DO)
        Object variable = statement.getVariable();
        if (!(variable instanceof IdentifierNode)) {
            throw new InvalidObjectException("Can not assign for loop value outside of a variable.");
        }
        String name = ((IdentifierNode) variable).getName();
        for (int i = start; i < end; i++) {
            setPublicVariable(name, i);
            for (INode bodyStatement : statement.getBody()) {
                executeStatement(bodyStatement);
            }
        }
    }

    private void executeIfStatement(IfNode statement) throws IOException {
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
            Object value = getVariable(variableName);
            if (value == null) {
                throw new RuntimeException("Undeclared variable: " + variableName);
            }
            return value;
        } else if (expression instanceof Pointer) {

        } else if (expression instanceof NotNode) {
          Object value = evaluateExpression(((NotNode) expression).getValue());
          if (value instanceof Boolean) {
            return !(Boolean)value;
          }
          throw new RuntimeException("Can not apply NOT to " + value.getClass());
        } else if (expression instanceof IndexNode) {
            Object variable = evaluateExpression(((IndexNode) expression).getValue());
            Object index = evaluateExpression(((IndexNode) expression).getIndex());
            if (variable instanceof String) {
//                return getVariable(variable);
                return new Pointer(publicPage, variable, (Integer)index);
            } else if (variable instanceof Pointer) {
                // try to ge the value of the pointer, and index it at the index spot
                Object value = getVariable(variable);
                // try to index the variable
                ArrayList array = (ArrayList) value;
                return array.get((Integer)index);
            }
        } else if (expression instanceof AsNode) {
            AsNode as = (AsNode) expression;
            Object value = resolvePointers(evaluateExpression(as.getValue()));
            try {
                switch (as.getCast()) {
                    case NUMBER:
                        return Expressions.castToNumber(value);
                    case STRING:
                        return Expressions.castToString(value);
                    case ARRAY:
                        return Expressions.castToArray(value);
                    default:
                        throw new RuntimeException("Unsupported cast type: " + as.getCast());
                }
            } catch (Exception e) {
                throw new RuntimeException("Could not parse \"" + value.toString() + "\" to " + as.getCast().name() + ": " + e.getLocalizedMessage(), e);
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
            return executeSortStatement((SortNode) expression);
            // check if instance of array

        } else if (expression instanceof ReverseNode) {
            Object value = resolvePointers(evaluateExpression(((ReverseNode) expression).getValue()));
            // check if value is array
            if (value instanceof String) {
                // return reverse of string
                return new StringBuilder(value.toString()).reverse().toString();
            } else if (value instanceof ArrayList<?>) {
                // return reversed array
                ArrayList result = (ArrayList)value;
                Collections.reverse(result);
                return result;
            }
            throw new RuntimeException("Can only reverse STRING and ARRAY.");
        } else if (expression instanceof SubstringNode) {
            Object variable = resolvePointers(evaluateExpression(((SubstringNode) expression).getVariable()));
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
            Object left = resolvePointers(evaluateExpression(binaryOp.getLeft()));
            Object right = resolvePointers(evaluateExpression(binaryOp.getRight()));
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
                    if (left instanceof  String) {
                        return left + String.valueOf(right);
                    }
                    return (Integer)left + (Integer) right;
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
            Object inner = resolvePointers(evaluateExpression(((LengthNode) expression).getVariable()));
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

    // only pointers are used with the system table, we don't provide support for user pointers (Although this could be on the roadmap)
    private void setPublicVariable(Object location, Object value) {
        // check if value is an array
        if (value instanceof ArrayList) {
            // add array to internal page so user can't access and we are pointer based
            value = internalPage.insert(value);
        }

        if (location instanceof Pointer) {
            // find this location
            Pointer p = (Pointer) location;
            if (p.getOffset() != null) {
                Object entry = p.getPage().get(p);
                if (entry instanceof ArrayList) {
                    // replace index at offset with value
                    ArrayList array = (ArrayList<Object>) entry;
                    array.set(p.getOffset(), value);
                    value = array;
                } else {
                    throw new RuntimeException("Can not index into " + entry.getClass());
                }
            }
            if (p.getLocation() instanceof Pointer) {
                // recursively follow the ptr to the right path, and then set that variable
                setPublicVariable(p.getLocation(), value);
            } else {
                internalPage.insert((String)p.getLocation(), value);
            }
            // set/update the entry
        } else if (location instanceof String) {
            publicPage.insert((String)location, value);
        }
    }
    private Object resolvePointers(Object value) {
        if (value instanceof ArrayList) {
            ArrayList array = new ArrayList(List.copyOf((ArrayList) value));
            for (int i=0; i<array.size(); i++) {
                array.set(i, resolvePointers(array.get(i)));
            }
            return array;
        } else if (value instanceof Pointer) {
            Pointer p = (Pointer) value;
            return resolvePointers(((Pointer) value).getPage().getWithOffset(p));
        }
        return value;
    }

    private Object getVariable(Object location) {
        if (location instanceof Pointer) {
            Pointer pointer = (Pointer) location;
            // lookup the item
            return pointer.getPage().get(pointer);
        } else if (location instanceof String) {
            return publicPage.get((String)location);
        }
        throw new RuntimeException("Can not access page with class " + location.getClass());
    }
}
