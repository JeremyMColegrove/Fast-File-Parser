package ast;

public class IndexNode implements INode {
    INode value;
    INode index;
    public IndexNode(INode value, INode index) {
        this.value = value;
        this.index = index;
    }
    public INode getValue() {
        return this.value;
    }

    public INode getIndex() {
        return this.index;
    }
    @Override
    public String toString() {
        return "("+value.toString()+"["+index.toString()+"])";
    }
}
