package ast;

public class UppercaseNode extends AbstractNode {
    private AbstractNode value;
    public UppercaseNode(AbstractNode value) {
        this.value = value;
    }

    public AbstractNode getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "(UPPERCASE " + value.toString() + ")";
    }
}
