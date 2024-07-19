package ast;

import java.util.List;

public class ForNode extends AbstractNode {
    private IdentifierNode variable;
    private AbstractNode start;
    private AbstractNode end;
    private List<AbstractNode> body;

    @Override
    public String toString() {
        String res = "FOR " + variable.toString() + " FROM " + start.toString() + " TO " + end.toString() + " DO [\n";
        for (AbstractNode node : body) {
            res += node.toString() + "\n";
        }
        return res + "] ENDFOR";
    }

    public ForNode(IdentifierNode variable, AbstractNode start, AbstractNode end, List<AbstractNode> body) {
        this.variable = variable;
        this.start = start;
        this.end = end;
        this.body = body;
    }
    public IdentifierNode getVariable() {
        return variable;
    }
    public AbstractNode getStart() {
        return start;
    }
    public AbstractNode getEnd() {
        return end;
    }
    public List<AbstractNode> getBody() {
        return body;
    }
}
