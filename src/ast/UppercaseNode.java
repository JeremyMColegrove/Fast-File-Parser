package ast;

public class UppercaseNode extends ASTNode {
    private ASTNode value;
    public UppercaseNode(ASTNode value) {
        this.value = value;
    }

    public ASTNode getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "(UPPERCASE " + value.toString() + ")";
    }
}
