package ast;

public class IdentifierNode implements INode {
    private String variable;

    public String getName() {
        return variable;
    }

    @Override
    public String toString() {
            return "("+variable+")";
    }

    public IdentifierNode(String variable) {
        this.variable = variable;
    }

}
