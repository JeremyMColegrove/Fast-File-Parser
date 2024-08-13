package ast;

public class CopyNode implements INode {
    private INode value;
    private INode destination;
    public CopyNode(INode value, INode destination) {
        this.value = value;
        this.destination = destination;
    }

    public INode getDestination() {
        return destination;
    }

    public INode getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "(COPY " + value + " TO " + destination + ")";
    }
}
