package ast;

public class ReadNode extends ASTNode {
    private ASTNode filename;
    private IdentifierNode variable;

    @Override
    public String toString() {
        return "READ " + filename + " INTO " + variable.toString();
    }

    public ReadNode(ASTNode filename, IdentifierNode variable) {
        this.filename = filename;
        this.variable = variable;
    }
    public ASTNode getFilename() {
        return filename;
    }
    public IdentifierNode getVariable() {
        return variable;
    }
}
