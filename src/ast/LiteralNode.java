package ast;

public abstract class LiteralNode extends ASTNode {
    public LiteralNode() {
    }
    public abstract Object getRealValue();
}
