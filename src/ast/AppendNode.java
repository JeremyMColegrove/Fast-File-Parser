package ast;

public class AppendNode extends AbstractNode {
    private AbstractNode content;
    private AbstractNode filename;

    @Override
    public String toString() {
        return "APPEND " + content.toString() + " TO " + filename;
    }

    public AppendNode(AbstractNode content, AbstractNode filename) {
        this.content = content;
        this.filename = filename;
    }
    public AbstractNode getContent() {
        return content;
    }
    public AbstractNode getFilename() {
        return filename;
    }
}
