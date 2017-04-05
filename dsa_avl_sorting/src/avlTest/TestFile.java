package avlTest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeMap;

/**
 * Created by calgarymichael on 3/26/17.
 */
public class TestFile {
    public static void main(String[] args) {
        System.out.println("Test 01: ");
        System.out.println("#####################");
        TreeMap<Integer, String> tree = firstTest();
        new RunTest<>(tree);

        System.out.println("Test 02: ");
        System.out.println("#####################");
        tree = secondTest();
        new RunTest<>(tree);
    }


    private static TreeMap<Integer, String> firstTest() {
        return readFile("data/test_01.csv");
    }


    private static TreeMap<Integer, String> secondTest() {
        return readFile("data/test_02.csv");
    }


    private static TreeMap<Integer, String> readFile(String file) {
        TreeMap<Integer, String> tree = new TreeMap<>();
        String line = "";

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                tree.put(Integer.parseInt(data[0]), data[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tree;
    }
}
