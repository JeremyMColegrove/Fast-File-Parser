package ast;

import java.util.List;

public class IfNode implements INode {
    private INode condition;
    private List<INode> thenBody;
    private List<INode> elseBody;

    @Override
    public String toString() {
        String res = "IF " + condition.toString() + " THEN [\n";
        for (INode node : thenBody) {
            res += node.toString() + "\n";
        }
        res += "] ELSE [\n";
        for (INode node : elseBody) {
            res += node.toString() + "\n";
        }
        return res + "] ENDIF";
    }

    public IfNode(INode condition, List<INode> thenBody, List<INode> elseBody) {
        this.condition = condition;
        this.thenBody = thenBody;
        this.elseBody = elseBody;
    }
    public List<INode> getElseBody() {
        return elseBody;
    }
    public List<INode> getThenBody() {
        return thenBody;
    }
    public INode getCondition() {
        return condition;
    }
}
