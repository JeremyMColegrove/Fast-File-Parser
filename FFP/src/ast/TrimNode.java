package ast;

public class TrimNode implements INode {
    private INode value;
    public TrimNode(INode value) {
        this.value = value;
    }

    public INode getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "(TRIM " + value.toString() + ")";
    }
}
