package ast;

public class SplitNode extends AbstractNode {
    private AbstractNode variable;
    private AbstractNode delimiter;
    private IdentifierNode target;

    @Override
    public String toString() {
        return "SPLIT " + variable.toString() + " BY " + delimiter.toString() + " INTO " + target.toString();
    }

    public SplitNode(AbstractNode variable, AbstractNode delimiter, IdentifierNode target) {
        this.variable = variable;
        this.delimiter = delimiter;
        this.target = target;
    }
    public AbstractNode getVariable() {
        return variable;
    }
    public AbstractNode getDelimiter() {
        return delimiter;
    }
    public IdentifierNode getTarget() {
        return target;
    }
}
