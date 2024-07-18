package ast;

public class LowercaseNode extends ASTNode {
    private ASTNode value;
    public LowercaseNode(ASTNode value) {
        this.value = value;
    }

    public ASTNode getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "(LOWERCASE " + value.toString() + ")";
    }
}
