package ast;


import token.TokenType;

public class AsNode implements INode {
    private TokenType cast;
    private INode value;
    public AsNode(INode value, TokenType cast) {
        this.value = value;
        this.cast = cast;
    }

    public TokenType getCast() {
        return this.cast;
    }

    public INode getValue() {
        return this.value;
    }
    @Override
    public String toString() {
        return "("+value.toString() + " AS " + cast.name() + ")";
    }
}
