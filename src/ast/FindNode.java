package ast;

public class FindNode implements INode {

    private INode regex;
    private INode value;

    public FindNode(INode regex, INode value) {
        this.regex = regex;
        this.value = value;
    }

    public INode getValue() {
        return value;
    }

    public INode getRegex() {
        return regex;
    }

    @Override
    public String toString() {
        return "(FIND "+regex+" IN "+value+")";
    }
}
