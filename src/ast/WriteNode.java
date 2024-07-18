package ast;

public class WriteNode extends ASTNode {
    private ASTNode content;
    private ASTNode filename;

    @Override
    public String toString() {
        return "WRITE " + content.toString() + " TO " + filename;
    }

    public WriteNode(ASTNode content, ASTNode filename) {
        this.content = content;
        this.filename = filename;
    }
    public ASTNode getContent() {
        return content;
    }
    public ASTNode getFilename() {
        return filename;
    }
}
