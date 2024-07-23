import core.Page;
import core.Pointer;

import java.util.ArrayList;

public class FFPDebug {
    public static void main(String[] args) {
        ArrayList list = new ArrayList<Object>();
        ArrayList innerList = new ArrayList<Object>();
        innerList.add("a");
        innerList.add("b");
        innerList.add("c");
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(innerList);

        // setup the page
        Page page = new Page();

        // emulate trying to insert the array into the internal page, and retrive the pointers
        Pointer p = page.insert(list);
        System.out.println("Finished");
    }
}
