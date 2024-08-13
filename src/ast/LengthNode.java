package ast;

public class LengthNode implements INode {
    private INode variable;

    @Override
    public String toString() {
        return "(LENGTH " + variable.toString()+")";
    }

    public LengthNode(INode variable) {
        this.variable = variable;
    }
    public INode getVariable() {
        return variable;
    }
}
