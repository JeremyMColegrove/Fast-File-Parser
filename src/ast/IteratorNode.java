package ast;

import java.util.List;

public class IteratorNode extends AbstractNode {
    private IdentifierNode variable;
    private AbstractNode iterator;
    private List<AbstractNode> body;

    @Override
    public String toString() {
        String res = "FOR " + variable.toString() + " IN " + iterator.toString() + " DO [\n";
        for (AbstractNode node : body) {
            res += node.toString() + "\n";
        }
        return res + "] ENDFOR";
    }

    public IteratorNode(IdentifierNode variable, AbstractNode iterator, List<AbstractNode> body) {
        this.variable = variable;
        this.iterator = iterator;
        this.body = body;
    }
    public IdentifierNode getVariable() {
        return variable;
    }
    public AbstractNode getIterator() {
        return iterator;
    }
    public List<AbstractNode> getBody() {
        return body;
    }
}
