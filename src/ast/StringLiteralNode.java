package ast;

public class StringLiteralNode extends LiteralNode {
    private String value;
    public StringLiteralNode(String value) {
        super();
        this.value = value;
    }
    public String getValue() {
        return value;
    }
    @Override
    public String toString() {
        return "(\"" + value + "\")";
    }

    @Override
    public Object getRealValue() {
        return value;
    }
}
