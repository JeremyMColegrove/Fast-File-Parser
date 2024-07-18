package ast;

import java.util.List;

public class SubstringNode extends ASTNode {
    private ASTNode variable;
    private ASTNode start;
    private ASTNode end;

    @Override
    public String toString() {
        return "(SUBSTRING " + variable.toString() + " FROM " + start.toString() + " TO " + end.toString() + ")";
    }

    public SubstringNode(ASTNode variable, ASTNode start, ASTNode end) {
        this.variable = variable;
        this.start = start;
        this.end = end;
    }

    public ASTNode getVariable() {
        return variable;
    }
    public ASTNode getStart() {
        return start;
    }
    public ASTNode getEnd() {
        return end;
    }
}
