package ast;

public class ReadNode extends AbstractNode {
    private AbstractNode filename;
    private IdentifierNode variable;

    @Override
    public String toString() {
        return "READ " + filename + " INTO " + variable.toString();
    }

    public ReadNode(AbstractNode filename, IdentifierNode variable) {
        this.filename = filename;
        this.variable = variable;
    }
    public AbstractNode getFilename() {
        return filename;
    }
    public IdentifierNode getVariable() {
        return variable;
    }
}
