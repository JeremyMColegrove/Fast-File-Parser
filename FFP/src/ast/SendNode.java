package ast;

public class SendNode implements INode{
    private INode type;
    private INode url;
    private INode body;
    private INode headers;
    private INode response;

    public SendNode(INode type, INode url, INode body, INode headers, INode response) {
        this.type = type;
        this.url = url;
        this.body = body;
        this.headers = headers;
        this.response = response;
    }
    public INode getBody() {
        return body;
    }

    public INode getHeaders() {
        return headers;
    }

    public INode getResponse() {
        return response;
    }

    public INode getType() {
        return type;
    }

    public INode getUrl() {
        return url;
    }
}
