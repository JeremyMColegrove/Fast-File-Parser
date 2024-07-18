package ast;

import java.util.regex.Pattern;

public class RegexLiteralNode extends LiteralNode {
    private Pattern pattern;

    @Override
    public String toString() {
        return "/"+pattern.toString()+"/";
    }
    public Pattern getRegex() {
        return pattern;
    }
    public RegexLiteralNode(Pattern pattern) {
        this.pattern = pattern;
    }

    @Override
    public Object getRealValue() {
        return pattern;
    }
}
