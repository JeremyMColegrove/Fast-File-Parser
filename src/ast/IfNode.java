package ast;

import java.util.List;

public class IfNode extends ASTNode {
    private ASTNode condition;
    private List<ASTNode> thenBody;
    private List<ASTNode> elseBody;

    @Override
    public String toString() {
        String res = "IF " + condition.toString() + " THEN [\n";
        for (ASTNode node : thenBody) {
            res += node.toString() + "\n";
        }
        res += "] ELSE [\n";
        for (ASTNode node : elseBody) {
            res += node.toString() + "\n";
        }
        return res + "] ENDIF";
    }

    public IfNode(ASTNode condition, List<ASTNode> thenBody, List<ASTNode> elseBody) {
        this.condition = condition;
        this.thenBody = thenBody;
        this.elseBody = elseBody;
    }
    public List<ASTNode> getElseBody() {
        return elseBody;
    }
    public List<ASTNode> getThenBody() {
        return thenBody;
    }
    public ASTNode getCondition() {
        return condition;
    }
}
