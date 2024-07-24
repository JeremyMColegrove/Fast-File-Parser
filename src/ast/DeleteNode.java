package ast;

public class DeleteNode implements INode {
    private INode value;
    public DeleteNode(INode value) {
        this.value = value;
    }

    public INode getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "(DELETE "+value+")";
    }
}
