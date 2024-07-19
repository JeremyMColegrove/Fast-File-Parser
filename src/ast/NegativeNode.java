package ast;

public class NegativeNode extends AbstractNode {
    private AbstractNode value;
    public NegativeNode(AbstractNode value) {
        this.value = value;
    }

    public AbstractNode getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "(-"+value.toString()+")";
    }
}
