package ast;

public class ReverseNode extends ASTNode {
    private ASTNode value;
    public ReverseNode(ASTNode value) {
        this.value = value;
    }
    public ASTNode getValue() {
        return value;
    }
    @Override
    public String toString() {
        return "(REVERSE " + value.toString() + ")";
    }
}
