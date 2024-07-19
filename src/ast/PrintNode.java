package ast;

public class PrintNode implements INode {
    private INode variable;

    @Override
    public String toString() {
        return "(PRINT " + variable.toString()+")";
    }

    public PrintNode(INode variable) {
        this.variable = variable;
    }
    public INode getVariable() {
        return variable;
    }
}
