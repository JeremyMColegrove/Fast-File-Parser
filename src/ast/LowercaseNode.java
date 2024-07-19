package ast;

public class LowercaseNode implements INode {
    private INode value;
    public LowercaseNode(INode value) {
        this.value = value;
    }

    public INode getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "(LOWERCASE " + value.toString() + ")";
    }
}
