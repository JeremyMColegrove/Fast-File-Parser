package ast;

import java.util.List;

public class ForNode implements INode {
    private IdentifierNode variable;
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

    public ForNode(IdentifierNode variable, INode start, INode end, List<INode> body) {
        this.variable = variable;
        this.start = start;
        this.end = end;
        this.body = body;
    }
    public IdentifierNode getVariable() {
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
