package ast;

public abstract class LiteralNode implements INode {
    public LiteralNode() {
    }
    public abstract Object getRealValue();
}
