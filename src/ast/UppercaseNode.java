package ast;

public class UppercaseNode implements INode {
    private INode value;
    public UppercaseNode(INode value) {
        this.value = value;
    }

    public INode getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "(UPPERCASE " + value.toString() + ")";
    }
}
