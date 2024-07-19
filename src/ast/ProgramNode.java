package ast;

import java.util.List;

public class ProgramNode implements INode {
    private List<INode> statements;

    @Override
    public String toString() {
        String res = "";
        for (INode node : statements) {
            res += node.toString() + "\n";
        }
        return res;
    }

    public ProgramNode(List<INode> statements) {
        this.statements = statements;
    }
    public List<INode> getStatements() {
        return statements;
    }
}
