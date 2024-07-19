package ast;

public class NegativeNode implements INode {
    private INode value;
    public NegativeNode(INode value) {
        this.value = value;
    }

    public INode getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "(-"+value.toString()+")";
    }
}
