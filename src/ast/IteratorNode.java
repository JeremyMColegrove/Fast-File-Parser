package ast;

import java.util.List;

public class IteratorNode implements INode {
    private IdentifierNode variable;
    private INode iterator;
    private List<INode> body;

    @Override
    public String toString() {
        String res = "FOR " + variable.toString() + " IN " + iterator.toString() + " DO [\n";
        for (INode node : body) {
            res += node.toString() + "\n";
        }
        return res + "] ENDFOR";
    }

    public IteratorNode(IdentifierNode variable, INode iterator, List<INode> body) {
        this.variable = variable;
        this.iterator = iterator;
        this.body = body;
    }
    public IdentifierNode getVariable() {
        return variable;
    }
    public INode getIterator() {
        return iterator;
    }
    public List<INode> getBody() {
        return body;
    }
}
