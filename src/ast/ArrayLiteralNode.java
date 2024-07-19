package ast;

import java.util.List;

public class ArrayLiteralNode extends LiteralNode {
    private List<INode> array;
    public ArrayLiteralNode(List<INode> array) {
        super();
        this.array = array;
    }
    public List<INode> getArray() {
        return array;
    }
    @Override
    public String toString() {
        String res = "[";
        for (int i =0; i<array.size(); i++) {
            res += array.get(i).toString();
            if (i < array.size() - 1) {
                res += ",";
            }
        }
        return res + "]";
    }

    @Override
    public Object getRealValue() {
        return array;
    }
}
