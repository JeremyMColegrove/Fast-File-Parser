package ast;

public class LowercaseNode extends AbstractNode {
    private AbstractNode value;
    public LowercaseNode(AbstractNode value) {
        this.value = value;
    }

    public AbstractNode getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "(LOWERCASE " + value.toString() + ")";
    }
}
