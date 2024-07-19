package ast;

public class IdentifierNode extends AbstractNode {
    private String variable;
    private AbstractNode accessor = null;

    public String getName() {
        return variable;
    }
    public AbstractNode getAccessor() { return accessor; }

    @Override
    public String toString() {
        if (accessor != null) {
            return "("+variable+"["+accessor.toString()+"])";
        } else {
            return "("+variable+")";
        }

    }

    public IdentifierNode(String variable) {
        this.variable = variable;
    }
    public IdentifierNode(String variable, AbstractNode accessor) {
        this.variable = variable;
        this.accessor = accessor;
    }
}
