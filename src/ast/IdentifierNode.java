package ast;

public class IdentifierNode extends ASTNode {
    private String variable;
    private ASTNode accessor = null;

    public String getName() {
        return variable;
    }
    public ASTNode getAccessor() { return accessor; }

    @Override
    public String toString() {
        if (accessor != null) {
            return "("+variable+"["+accessor.toString()+"])";
        } else {
            return "("+variable+")";
        }

    }

    public IdentifierNode(String variable) {
        this.variable = variable;
    }
    public IdentifierNode(String variable, ASTNode accessor) {
        this.variable = variable;
        this.accessor = accessor;
    }
}
