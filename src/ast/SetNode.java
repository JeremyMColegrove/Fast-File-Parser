package ast;

public class SetNode implements INode {
    private IdentifierNode variable;
    private INode value;

    @Override
    public String toString() {
        return "SET " + variable.toString() + " TO " + value.toString();
    }

    public IdentifierNode getVariable() {
        return variable;
    }
    public INode getValue() {
        return value;
    }
    public SetNode(IdentifierNode variable, INode value) {
        this.variable = variable;
        this.value = value;
    }
}
