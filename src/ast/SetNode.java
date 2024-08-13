package ast;

public class SetNode implements INode {
    private INode variable;
    private INode value;

    @Override
    public String toString() {
        return "SET " + variable.toString() + " TO " + value.toString();
    }

    public INode getVariable() {
        return variable;
    }
    public INode getValue() {
        return value;
    }
    public SetNode(INode variable, INode value) {
        this.variable = variable;
        this.value = value;
    }
}
