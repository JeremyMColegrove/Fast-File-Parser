package ast;

public class SetNode extends ASTNode {
    private IdentifierNode variable;
    private ASTNode value;

    @Override
    public String toString() {
        return "SET " + variable.toString() + " TO " + value.toString();
    }

    public IdentifierNode getVariable() {
        return variable;
    }
    public ASTNode getValue() {
        return value;
    }
    public SetNode(IdentifierNode variable, ASTNode value) {
        this.variable = variable;
        this.value = value;
    }
}
