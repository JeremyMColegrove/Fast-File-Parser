package ast;

public class BinaryOperationNode implements INode {
    private INode left;
    private String operator;
    private INode right;

    @Override
    public String toString() {
        return "("+left.toString() + operator + right.toString()+")";
    }

    public BinaryOperationNode(INode left, String operator, INode right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    public INode getLeft() {
        return left;
    }

    public String getOperator() {
        return operator;
    }

    public INode getRight() {
        return right;
    }

}
