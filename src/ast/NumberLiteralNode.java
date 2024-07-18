package ast;

public class NumberLiteralNode extends LiteralNode {
    private Integer number;

    @Override
    public String toString() {
        return "("+number+")";
    }

    public Integer getNumber() {
        return number;
    }
    public NumberLiteralNode(Integer number) {
        super();
        this.number = number;
    }

    @Override
    public Object getRealValue() {
        return number;
    }
}
