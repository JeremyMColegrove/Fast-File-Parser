package ast;

public class TrimNode extends ASTNode {
    private ASTNode value;
    public TrimNode(ASTNode value) {
        this.value = value;
    }

    public ASTNode getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "(TRIM " + value.toString() + ")";
    }
}
