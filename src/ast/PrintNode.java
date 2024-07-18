package ast;

public class PrintNode extends ASTNode {
    private ASTNode variable;

    @Override
    public String toString() {
        return "(PRINT " + variable.toString()+")";
    }

    public PrintNode(ASTNode variable) {
        this.variable = variable;
    }
    public ASTNode getVariable() {
        return variable;
    }
}
