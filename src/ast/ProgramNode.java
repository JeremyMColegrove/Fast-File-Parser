package ast;

import java.util.List;

public class ProgramNode extends AbstractNode {
    private List<AbstractNode> statements;

    @Override
    public String toString() {
        String res = "";
        for (AbstractNode node : statements) {
            res += node.toString() + "\n";
        }
        return res;
    }

    public ProgramNode(List<AbstractNode> statements) {
        this.statements = statements;
    }
    public List<AbstractNode> getStatements() {
        return statements;
    }
}
