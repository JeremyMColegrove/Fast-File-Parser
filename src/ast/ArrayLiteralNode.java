package ast;

import java.util.List;
import java.util.Objects;

public class ArrayLiteralNode extends LiteralNode {
    private List<ASTNode> array;
    public ArrayLiteralNode(List<ASTNode> array) {
        super();
        this.array = array;
    }
    public List<ASTNode> getArray() {
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
