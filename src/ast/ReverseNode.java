package ast;

public class ReverseNode implements INode {
    private INode value;
    public ReverseNode(INode value) {
        this.value = value;
    }
    public INode getValue() {
        return value;
    }
    @Override
    public String toString() {
        return "(REVERSE " + value.toString() + ")";
    }
}
