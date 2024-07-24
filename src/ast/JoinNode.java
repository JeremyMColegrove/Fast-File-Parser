package ast;

public class JoinNode implements INode {

    private INode value;
    private INode delimiter;

    public JoinNode(INode value, INode delimiter) {
        this.value = value;
        this.delimiter = delimiter;
    }

    public INode getValue() {
        return value;
    }

    public INode getDelimiter() {
        return delimiter;
    }

    @Override
    public String toString() {
        return "(JOIN " + value + " BY " + delimiter + ")";
    }
}
