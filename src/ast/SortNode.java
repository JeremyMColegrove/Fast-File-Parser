package ast;

public class SortNode implements INode {
    private INode value;
    public SortNode(INode value) {
        this.value = value;
    }
    public INode getValue() {
        return value;
    }
    @Override
    public String toString() {
        return "(SORT " + value.toString() + ")";
    }

}
