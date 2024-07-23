package ast;

public class SplitNode implements INode {
    private INode variable;
    private INode delimiter;
    private INode target;

    @Override
    public String toString() {
        return "SPLIT " + variable.toString() + " BY " + delimiter.toString() + " INTO " + target.toString();
    }

    public SplitNode(INode variable, INode delimiter, INode target) {
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
    public INode getTarget() {
        return target;
    }
}
