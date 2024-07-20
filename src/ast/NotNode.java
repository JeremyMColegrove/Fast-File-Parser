package ast;

public class NotNode implements INode {
    INode value;
    public NotNode(INode value) {
        this.value = value;
    }
    public INode getValue() {
        return this.value;
    }
    @Override
    public String toString() {
        return "(NOT "+value.toString()+")";
    }
}
