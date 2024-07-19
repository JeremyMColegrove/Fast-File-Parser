package ast;

public class PrintNode extends AbstractNode {
    private AbstractNode variable;

    @Override
    public String toString() {
        return "(PRINT " + variable.toString()+")";
    }

    public PrintNode(AbstractNode variable) {
        this.variable = variable;
    }
    public AbstractNode getVariable() {
        return variable;
    }
}
