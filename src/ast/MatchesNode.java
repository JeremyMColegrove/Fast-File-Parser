package ast;

public class MatchesNode extends ASTNode {
    private ASTNode left;
    private RegexLiteralNode regex;
    public ASTNode getLeft() { return left; }
    public RegexLiteralNode getRegex() { return regex; }
    public MatchesNode(ASTNode left, RegexLiteralNode regex) {
     this.left = left;
     this.regex = regex;
    }

    @Override
    public String toString() {
        return "("+left.toString() + " MATCHES " + regex.toString() + ")";
    }
}
