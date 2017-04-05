package avlTest;

import avlPD.Avl;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by calgarymichael on 3/26/17.
 */
class RunTest<K extends Comparable<? super K>, V> {
    RunTest(TreeMap<K, V> data) {
        Avl<K, V> tree = new Avl<>();

        for (Map.Entry<K,V> entry : data.entrySet()) {
            K key = entry.getKey();
            V val = entry.getValue();
            tree.insert(key, val);
        }

        System.out.println("inOrder:");
        for (K i : tree.inOrder()) {
            System.out.println(i + " - " + tree.findValueForKey(i));
        }
        System.out.println("\n");

        System.out.println("preOrder:");
        for (K i : tree.preOrder()) {
            System.out.println(i + " - " + tree.findValueForKey(i));
        }
        System.out.println("\n");

        System.out.println("postOrder:");
        for (K i : tree.postOrder()) {
            System.out.println(i + " - " + tree.findValueForKey(i));
        }
        System.out.println("\n");
    }
}
