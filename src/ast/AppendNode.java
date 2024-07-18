package ast;

public class AppendNode extends ASTNode {
    private ASTNode content;
    private ASTNode filename;

    @Override
    public String toString() {
        return "APPEND " + content.toString() + " TO " + filename;
    }

    public AppendNode(ASTNode content, ASTNode filename) {
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
