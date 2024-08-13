import java.util.ArrayList;

public class Expressions {



    public static Object castToNumber(Object value) {
        if (value instanceof String) {
            return Integer.parseInt((String) value);
        } else if (value instanceof Number) {
            return value;
        } else {
            throw new NumberFormatException("Cannot cast to number: " + value);
        }
    }

    public static Object castToString(Object value) {
        if (value instanceof Number) {
            return String.valueOf(value);
        } else if (value instanceof ArrayList<?>) {
            StringBuilder builder = new StringBuilder();
            for (Object element : (ArrayList<?>) value) {
                builder.append(String.valueOf(element));
            }
            return builder.toString();
        } else if (value instanceof String) {
            return value;
        } else {
            throw new RuntimeException("Cannot cast to string: " + value);
        }
    }

    public static Object castToArray(Object value) {
        if (value instanceof String) {
            char[] chars = ((String) value).toCharArray();
            ArrayList<Object> array = new ArrayList<>();
            for (char c : chars) {
                array.add(String.valueOf(c));
            }
            return array;
        } else if (value instanceof ArrayList<?>) {
            return value;
        } else {
            throw new RuntimeException("Cannot cast to array: " + value);
        }
    }
}
