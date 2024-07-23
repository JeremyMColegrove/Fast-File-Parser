package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Page {
    int counter = 0;
    private HashMap<String, Object> table = new HashMap<>(32);


    public Object getWithOffset(Pointer pointer) {
        Object item = this.get(pointer);
        if (pointer.getOffset() != null) {
            if (item instanceof ArrayList) {
                return ((ArrayList<?>) item).get(pointer.getOffset());
            }
        }
        return item;
    }
    public Object get(Pointer pointer) {
        return table.get(pointer.getLocation());
    }

    public Object get(String name) {
        return table.get(name);
    }

    public void update(Pointer pointer, Object value) {
        // maybe pointer is with offset, and we need to index that
        if (pointer.getLocation() instanceof Pointer) {
            update((Pointer)pointer.getLocation(), value);
        } else {
            update((String)pointer.getLocation(), value);
        }
    }

    public void update(String name, Object value) {
        if (table.containsKey(name)) {
            table.put(name, value);
        }
    }

    public Pointer insert(Object object) {
        return insert(generateInternalName(), object);
    }

    public Pointer insert(String name, Object object) {
        // insert the object into the table and return the point to it
        if (object instanceof ArrayList) {
            // recursively search for arrays as elements and convert to pointers
            ArrayList array = new ArrayList<>(List.copyOf((ArrayList) object));
            for (int i=0; i<array.size(); i++) {
                if (array.get(i) instanceof ArrayList) {
                    // insert this into another place in the table
                    array.set(i, this.insert(generateInternalName(), array.get(i)));
                }
            }
            object = array;
        }
        table.put(name, object);
        return new Pointer(this, name, null);
    }

    private String generateInternalName() {
        // make sure no names are used twice
        return "__internal_" + counter++;
    }
}
