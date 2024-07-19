package ast;

import java.util.List;

public class IfNode extends AbstractNode {
    private AbstractNode condition;
    private List<AbstractNode> thenBody;
    private List<AbstractNode> elseBody;

    @Override
    public String toString() {
        String res = "IF " + condition.toString() + " THEN [\n";
        for (AbstractNode node : thenBody) {
            res += node.toString() + "\n";
        }
        res += "] ELSE [\n";
        for (AbstractNode node : elseBody) {
            res += node.toString() + "\n";
        }
        return res + "] ENDIF";
    }

    public IfNode(AbstractNode condition, List<AbstractNode> thenBody, List<AbstractNode> elseBody) {
        this.condition = condition;
        this.thenBody = thenBody;
        this.elseBody = elseBody;
    }
    public List<AbstractNode> getElseBody() {
        return elseBody;
    }
    public List<AbstractNode> getThenBody() {
        return thenBody;
    }
    public AbstractNode getCondition() {
        return condition;
    }
}
