package ast;

public class AssertNode implements INode {
    private INode value;
    public AssertNode(INode value) {
        this.value = value;
    }

    public INode getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "(ASSERT " + value + ")";
    }
}
