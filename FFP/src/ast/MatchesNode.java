package ast;

public class MatchesNode implements INode {
    private INode left;
    private RegexLiteralNode regex;
    public INode getLeft() { return left; }
    public RegexLiteralNode getRegex() { return regex; }
    public MatchesNode(INode left, RegexLiteralNode regex) {
     this.left = left;
     this.regex = regex;
    }

    @Override
    public String toString() {
        return "("+left.toString() + " MATCHES " + regex.toString() + ")";
    }
}
