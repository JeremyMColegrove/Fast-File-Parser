package ast;


import token.TokenType;

public class AsNode extends AbstractNode {
    private TokenType cast;
    private AbstractNode value;
    public AsNode(AbstractNode value, TokenType cast) {
        this.value = value;
        this.cast = cast;
    }

    public TokenType getCast() {
        return this.cast;
    }

    public AbstractNode getValue() {
        return this.value;
    }
    @Override
    public String toString() {
        return "("+value.toString() + " AS " + cast.name() + ")";
    }
}
