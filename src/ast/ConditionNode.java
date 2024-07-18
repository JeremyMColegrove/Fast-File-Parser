package ast;

public class ConditionNode extends ASTNode {
    private ASTNode left;
    private String operator;
    private ASTNode right;

    @Override
    public String toString() {
        return "("+left.toString()+operator+right.toString()+")";
    }

    public ConditionNode(ASTNode left, String operator, ASTNode right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }
    public ASTNode getLeft() {
        return left;
    }
    public String getOperator() {
        return operator;
    }
    public ASTNode getRight() {
        return right;
    }
}
