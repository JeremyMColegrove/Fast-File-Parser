package ast;

import java.util.List;

public class SortNode extends ASTNode {
    private ASTNode value;
    public SortNode(ASTNode value) {
        this.value = value;
    }
    public ASTNode getValue() {
        return value;
    }
    @Override
    public String toString() {
        return "(SORT " + value.toString() + ")";
    }

}
