package ast;

public class ReplaceNode implements INode {
    private INode search;
    private INode replacement;
    private INode target;
    private boolean first = false;
    public ReplaceNode(INode search, INode replacement, INode target, boolean all) {
        this.search = search;
        this.replacement = replacement;
        this.target = target;
        this.first = all;
    }

    public INode getReplacement() {
        return replacement;
    }

    public boolean isFirstOnly() {
        return first;
    }

    public INode getSearch() {
        return search;
    }

    public INode getTarget() {
        return target;
    }

    @Override
    public String toString() {
        return "(REPLACE " + (first ?"":"ALL ") + search + " WITH " + replacement + " IN " + target + ")";
    }
}
