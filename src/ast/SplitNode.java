package ast;

public class SplitNode extends ASTNode {
    private ASTNode variable;
    private ASTNode delimiter;
    private IdentifierNode target;

    @Override
    public String toString() {
        return "SPLIT " + variable.toString() + " BY " + delimiter.toString() + " INTO " + target.toString();
    }

    public SplitNode(ASTNode variable, ASTNode delimiter, IdentifierNode target) {
        this.variable = variable;
        this.delimiter = delimiter;
        this.target = target;
    }
    public ASTNode getVariable() {
        return variable;
    }
    public ASTNode getDelimiter() {
        return delimiter;
    }
    public IdentifierNode getTarget() {
        return target;
    }
}
