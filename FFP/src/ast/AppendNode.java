package ast;

public class AppendNode implements INode {
    private INode content;
    private INode filename;

    @Override
    public String toString() {
        return "APPEND " + content.toString() + " TO " + filename;
    }

    public AppendNode(INode content, INode filename) {
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
