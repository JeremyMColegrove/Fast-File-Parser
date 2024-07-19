package ast;

public class TrimNode extends AbstractNode {
    private AbstractNode value;
    public TrimNode(AbstractNode value) {
        this.value = value;
    }

    public AbstractNode getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "(TRIM " + value.toString() + ")";
    }
}
