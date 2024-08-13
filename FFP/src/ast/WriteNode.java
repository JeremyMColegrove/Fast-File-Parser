package ast;

public class WriteNode implements INode {
    private INode content;
    private INode filename;

    @Override
    public String toString() {
        return "WRITE " + content.toString() + " TO " + filename;
    }

    public WriteNode(INode content, INode filename) {
        this.content = content;
        this.filename = filename;
    }
    public INode getContent() {
        return content;
    }
    public INode getFilename() {
        return filename;
    }
}
