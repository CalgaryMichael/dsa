package avlTest;

import com.sun.deploy.security.ruleset.RunRule;

import java.util.TreeMap;

/**
 * Created by calgarymichael on 3/26/17.
 */
public class TestCase {
    public static void main(String[] args) {
        TreeMap<Integer, String> tree = new TreeMap<>();

        tree.put(2, "First");
        tree.put(1, "Second");
        tree.put(4, "Third");
        tree.put(5, "Fourth");
        tree.put(9, "Fifth");
        tree.put(3, "Sixth");
        tree.put(6, "Seventh");
        tree.put(7, "Eighth");

        new RunTest<>(tree);
    }
}
