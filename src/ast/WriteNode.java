package ast;

public class WriteNode extends AbstractNode {
    private AbstractNode content;
    private AbstractNode filename;

    @Override
    public String toString() {
        return "WRITE " + content.toString() + " TO " + filename;
    }

    public WriteNode(AbstractNode content, AbstractNode filename) {
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
