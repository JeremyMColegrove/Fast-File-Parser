package ast;

public class SetNode extends AbstractNode {
    private IdentifierNode variable;
    private AbstractNode value;

    @Override
    public String toString() {
        return "SET " + variable.toString() + " TO " + value.toString();
    }

    public IdentifierNode getVariable() {
        return variable;
    }
    public AbstractNode getValue() {
        return value;
    }
    public SetNode(IdentifierNode variable, AbstractNode value) {
        this.variable = variable;
        this.value = value;
    }
}
