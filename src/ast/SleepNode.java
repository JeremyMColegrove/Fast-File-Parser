package ast;

public class SleepNode implements INode{
    private INode value;
    public SleepNode(INode value) {
        this.value = value;
    }

    public INode getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "(SLEEP "+value.toString()+")";
    }
}
