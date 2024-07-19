package ast;

public class SubstringNode implements INode {
    private INode variable;
    private INode start;
    private INode end;

    @Override
    public String toString() {
        return "(SUBSTRING " + variable.toString() + " FROM " + start.toString() + " TO " + end.toString() + ")";
    }

    public SubstringNode(INode variable, INode start, INode end) {
        this.variable = variable;
        this.start = start;
        this.end = end;
    }

    public INode getVariable() {
        return variable;
    }
    public INode getStart() {
        return start;
    }
    public INode getEnd() {
        return end;
    }
}
