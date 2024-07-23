package ast;

import java.util.List;

public class IteratorNode implements INode {
    private INode variable;
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

    public IteratorNode(INode variable, INode iterator, List<INode> body) {
        this.variable = variable;
        this.iterator = iterator;
        this.body = body;
    }
    public INode getVariable() {
        return variable;
    }
    public INode getIterator() {
        return iterator;
    }
    public List<INode> getBody() {
        return body;
    }
}
