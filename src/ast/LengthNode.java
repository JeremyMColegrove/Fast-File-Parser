package ast;

public class LengthNode extends AbstractNode {
    private AbstractNode variable;

    @Override
    public String toString() {
        return "(LENGTH " + variable.toString()+")";
    }

    public LengthNode(AbstractNode variable) {
        this.variable = variable;
    }
    public AbstractNode getVariable() {
        return variable;
    }
}
