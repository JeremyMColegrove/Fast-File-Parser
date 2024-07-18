package ast;

import java.util.List;

public class ProgramNode extends ASTNode {
    private List<ASTNode> statements;

    @Override
    public String toString() {
        String res = "";
        for (ASTNode node : statements) {
            res += node.toString() + "\n";
        }
        return res;
    }

    public ProgramNode(List<ASTNode> statements) {
        this.statements = statements;
    }
    public List<ASTNode> getStatements() {
        return statements;
    }
}
