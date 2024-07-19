package ast;

public class ReverseNode extends AbstractNode {
    private AbstractNode value;
    public ReverseNode(AbstractNode value) {
        this.value = value;
    }
    public AbstractNode getValue() {
        return value;
    }
    @Override
    public String toString() {
        return "(REVERSE " + value.toString() + ")";
    }
}
