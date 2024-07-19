package ast;

public class BinaryOperationNode extends AbstractNode {
    private AbstractNode left;
    private String operator;
    private AbstractNode right;

    @Override
    public String toString() {
        return "("+left.toString() + operator + right.toString()+")";
    }

    public BinaryOperationNode(AbstractNode left, String operator, AbstractNode right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    public AbstractNode getLeft() {
        return left;
    }

    public String getOperator() {
        return operator;
    }

    public AbstractNode getRight() {
        return right;
    }

}
