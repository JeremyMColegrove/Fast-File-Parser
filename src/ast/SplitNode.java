package ast;

public class SplitNode implements INode {
    private INode variable;
    private INode delimiter;
    private IdentifierNode target;

    @Override
    public String toString() {
        return "SPLIT " + variable.toString() + " BY " + delimiter.toString() + " INTO " + target.toString();
    }

    public SplitNode(INode variable, INode delimiter, IdentifierNode target) {
        this.variable = variable;
        this.delimiter = delimiter;
        this.target = target;
    }
    public INode getVariable() {
        return variable;
    }
    public INode getDelimiter() {
        return delimiter;
    }
    public IdentifierNode getTarget() {
        return target;
    }
}
