package ast;

public class LengthNode extends ASTNode {
    private ASTNode variable;

    @Override
    public String toString() {
        return "(LENGTH " + variable.toString()+")";
    }

    public LengthNode(ASTNode variable) {
        this.variable = variable;
    }
    public ASTNode getVariable() {
        return variable;
    }
}
