package ast;

public abstract class LiteralNode extends AbstractNode {
    public LiteralNode() {
    }
    public abstract Object getRealValue();
}
