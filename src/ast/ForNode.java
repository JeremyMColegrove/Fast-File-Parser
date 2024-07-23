package ast;

import java.util.List;

public class ForNode implements INode {
    private INode variable;
    private INode start;
    private INode end;
    private List<INode> body;

    @Override
    public String toString() {
        String res = "FOR " + variable.toString() + " FROM " + start.toString() + " TO " + end.toString() + " DO [\n";
        for (INode node : body) {
            res += node.toString() + "\n";
        }
        return res + "] ENDFOR";
    }

    public ForNode(INode variable, INode start, INode end, List<INode> body) {
        this.variable = variable;
        this.start = start;
        this.end = end;
        this.body = body;
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
    public List<INode> getBody() {
        return body;
    }
}
