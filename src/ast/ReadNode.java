package ast;

public class ReadNode implements INode {
    private INode filename;
    private INode variable;

    @Override
    public String toString() {
        return "READ " + filename + " INTO " + variable.toString();
    }

    public ReadNode(INode filename, INode variable) {
        this.filename = filename;
        this.variable = variable;
    }
    public INode getFilename() {
        return filename;
    }
    public INode getVariable() {
        return variable;
    }
}
