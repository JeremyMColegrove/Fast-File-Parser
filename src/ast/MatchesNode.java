package ast;

public class MatchesNode extends AbstractNode {
    private AbstractNode left;
    private RegexLiteralNode regex;
    public AbstractNode getLeft() { return left; }
    public RegexLiteralNode getRegex() { return regex; }
    public MatchesNode(AbstractNode left, RegexLiteralNode regex) {
     this.left = left;
     this.regex = regex;
    }

    @Override
    public String toString() {
        return "("+left.toString() + " MATCHES " + regex.toString() + ")";
    }
}
