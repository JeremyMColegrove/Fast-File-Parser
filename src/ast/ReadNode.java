package ast;

public class ReadNode implements INode {
    private INode filename;
    private IdentifierNode variable;

    @Override
    public String toString() {
        return "READ " + filename + " INTO " + variable.toString();
    }

    public ReadNode(INode filename, IdentifierNode variable) {
        this.filename = filename;
        this.variable = variable;
    }
    public INode getFilename() {
        return filename;
    }
    public IdentifierNode getVariable() {
        return variable;
    }
}
