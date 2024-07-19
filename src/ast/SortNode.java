package ast;

public class SortNode extends AbstractNode {
    private AbstractNode value;
    public SortNode(AbstractNode value) {
        this.value = value;
    }
    public AbstractNode getValue() {
        return value;
    }
    @Override
    public String toString() {
        return "(SORT " + value.toString() + ")";
    }

}
