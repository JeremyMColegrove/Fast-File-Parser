package ast;

import java.util.List;

public class IteratorNode extends ASTNode {
    private IdentifierNode variable;
    private ASTNode iterator;
    private List<ASTNode> body;

    @Override
    public String toString() {
        String res = "FOR " + variable.toString() + " IN " + iterator.toString() + " DO [\n";
        for (ASTNode node : body) {
            res += node.toString() + "\n";
        }
        return res + "] ENDFOR";
    }

    public IteratorNode(IdentifierNode variable, ASTNode iterator, List<ASTNode> body) {
        this.variable = variable;
        this.iterator = iterator;
        this.body = body;
    }
    public IdentifierNode getVariable() {
        return variable;
    }
    public ASTNode getIterator() {
        return iterator;
    }
    public List<ASTNode> getBody() {
        return body;
    }
}
