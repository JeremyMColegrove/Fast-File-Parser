package ast;

public class SubstringNode extends AbstractNode {
    private AbstractNode variable;
    private AbstractNode start;
    private AbstractNode end;

    @Override
    public String toString() {
        return "(SUBSTRING " + variable.toString() + " FROM " + start.toString() + " TO " + end.toString() + ")";
    }

    public SubstringNode(AbstractNode variable, AbstractNode start, AbstractNode end) {
        this.variable = variable;
        this.start = start;
        this.end = end;
    }

    public AbstractNode getVariable() {
        return variable;
    }
    public AbstractNode getStart() {
        return start;
    }
    public AbstractNode getEnd() {
        return end;
    }
}
