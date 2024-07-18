package ast;

import java.util.List;

public class ForNode extends ASTNode {
    private IdentifierNode variable;
    private ASTNode start;
    private ASTNode end;
    private List<ASTNode> body;

    @Override
    public String toString() {
        String res = "FOR " + variable.toString() + " FROM " + start.toString() + " TO " + end.toString() + " DO [\n";
        for (ASTNode node : body) {
            res += node.toString() + "\n";
        }
        return res + "] ENDFOR";
    }

    public ForNode(IdentifierNode variable, ASTNode start, ASTNode end, List<ASTNode> body) {
        this.variable = variable;
        this.start = start;
        this.end = end;
        this.body = body;
    }
    public IdentifierNode getVariable() {
        return variable;
    }
    public ASTNode getStart() {
        return start;
    }
    public ASTNode getEnd() {
        return end;
    }
    public List<ASTNode> getBody() {
        return body;
    }
}
