package avlPD;

import java.util.ArrayList;

/**
 * Created by calgarymichael on 3/21/17.
 */
public class Avl<K extends Comparable<? super K>, V> {
    private int insertCount;
    private int singleRotationCount;
    private int doubleRotationCount;
    private Node root;


    // ===================
    // Class(es)        ==
    // ===================

    private class Node<K,V> {
        private K key;
        private V val;
        private Node left, right;
        private int height;

        // ===================
        // Constructor(s)   ==
        // ===================

        // *********************************************************************
        protected Node() {
            height = 0;
        }


        // *********************************************************************
        protected Node(K key, V val) {
            this();
            this.key = key;
            this.val = val;
        }

        // ===================
        // Get(s) & Set(s)  ==
        // ===================

        // *********************************************************************
        protected void setK(K k) {
            this.key = k;
        }

        // *********************************************************************
        protected K getK() {
            return this.key;
        }

        // *********************************************************************
        protected void setV(V v) {
            this.val = v;
        }

        // *********************************************************************
        protected V getV() {
            return this.val;
        }

        // *********************************************************************
        protected void setLeft(Node<K,V> left) {
            this.left = left;
        }

        // *********************************************************************
        protected Node<K, V> getLeft() {
            return this.left;
        }

        // *********************************************************************
        protected void setRight(Node<K,V> right) {
            this.right = right;
        }

        // *********************************************************************
        protected Node<K, V> getRight() {
            return this.right;
        }

        // *********************************************************************
        protected void setHeight(int h) {
            this.height = h;
        }

        // *********************************************************************
        protected int getHeight() {
            return this.height;
        }
    }


    // ===================
    // Constructor(s)   ==
    // ===================

    public Avl() {
        root = null;
    }


    // ===================
    // Method(s)        ==
    // ===================

    public boolean insert(K key, V val) {
        try {
            setRoot(insert(key, val, this.getRoot()));
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    // *********************************************************************
    private Node<K, V> insert(K key, V val, Node<K, V> n) throws Exception {
        if (n == null) {
            n = new Node<K,V>(key, val);
        } else if (key.compareTo(n.getK()) < 0) {
            n.setLeft(insert(key, val, n.getLeft()));

            if (getNodeHeight(n.getLeft()) - getNodeHeight(n.getRight()) == 2) {
                if (key.compareTo(n.getLeft().getK()) < 0)
                    n = rotateWithLeftChild(n);
                else
                    n = doubleWithLeftChild(n);
            }
        } else if (key.compareTo(n.key) > 0) {
            n.setRight(insert(key, val, n.getRight()));

            if (getNodeHeight(n.getRight()) - getNodeHeight(n.getLeft()) == 2)
                if (key.compareTo(n.getRight().getK()) > 0)
                    n = rotateWithRightChild(n);
                else
                    n = doubleWithRightChild(n);

        } else {
            throw new Exception("Attempting to insert duplicate value");
        }

        n.setHeight(max(getNodeHeight(n.getLeft()), getNodeHeight(n.getRight())) + 1);
        insertCount++;
        return n;
    }


    // *********************************************************************
    private Node<K, V> rotateWithLeftChild(Node<K, V> k2) {
        Node<K, V> k1 = k2.getLeft();

        k2.setLeft(k1.getRight());
        k1.setRight(k2);

        k2.setHeight(max(getNodeHeight(k2.getLeft()), getNodeHeight(k2.getRight())) + 1);
        k1.setHeight(max(getNodeHeight(k1.getLeft()), k2.getHeight()) + 1);

        singleRotationCount++;
        return (k1);
    }


    // *********************************************************************
    private Node<K, V> rotateWithRightChild(Node<K, V> k1) {
        Node<K, V> k2 = k1.getRight();

        k1.setRight(k2.getLeft());
        k2.setLeft(k1);

        k1.setHeight(max(getNodeHeight(k1.getLeft()), getNodeHeight(k1.getRight())) + 1);
        k2.setHeight(max(getNodeHeight(k2.getRight()), k1.getHeight()) + 1);

        singleRotationCount++;
        return (k2);
    }


    // *********************************************************************
    private Node<K, V> doubleWithLeftChild(Node<K, V> k3) {
        k3.setLeft(rotateWithRightChild(k3.getLeft()));
        doubleRotationCount++;
        return rotateWithLeftChild(k3);
    }


    // *********************************************************************
    private Node<K, V> doubleWithRightChild(Node<K, V> k1) {
        k1.setRight(rotateWithLeftChild(k1.getRight()));
        doubleRotationCount++;
        return rotateWithRightChild(k1);
    }


    // *********************************************************************
    public void delete(K key) {
        root = delete(key, this.getRoot());
    }


    // *********************************************************************
    private Node<K, V> delete(K key, Node<K, V> n) {
        if (n == null) {
            System.out.println("Sorry but you're mistaken, " + n + " doesn't exist in this tree :)\n");
            return null;
        }
        System.out.println("Remove starts... " + n.getK() + " and " + key);

        if (key.compareTo(n.getK()) < 0) {
            n.setLeft(delete(key, n.getLeft()));
            int l = n.getLeft() != null ? n.getLeft().getHeight() : 0;

            if ((n.getRight() != null) && (n.getRight().getHeight() - l >= 2)) {
                int rightHeight = n.getRight().getRight() != null ? n.getRight().getRight().getHeight() : 0;
                int leftHeight = n.getRight().getLeft() != null ? n.getRight().getLeft().getHeight() : 0;

                if (rightHeight >= leftHeight)
                    n = rotateWithLeftChild(n);
                else
                    n = doubleWithRightChild(n);
            }
        } else if (key.compareTo(n.getK()) > 0) {
            n.setRight(delete(key, n.getRight()));
            int r = n.getRight() != null ? n.getRight().getHeight() : 0;

            if ((n.getLeft() != null) && (n.getLeft().getHeight() - r >= 2)) {
                int leftHeight = n.getLeft().getLeft() != null ? n.getLeft().getLeft().getHeight() : 0;
                int rightHeight = n.getLeft().getRight() != null ? n.getLeft().getRight().getHeight() : 0;

                if (leftHeight >= rightHeight)
                    n = rotateWithRightChild(n);
                else
                    n = doubleWithLeftChild(n);
            }
        } else if (n.getLeft() != null) {
            Node<K,V> max = findMax(n.getLeft());
            n.setK(max.getK());
            n.setV(max.getV());
            Node newNode = delete(n.getK(), n.getLeft());

            if ((n.getRight() != null) && (n.getRight().getHeight() - n.getLeft().getHeight() >= 2)) {
                int rightHeight = n.getRight().getRight() != null ? n.getRight().getRight().getHeight() : 0;
                int leftHeight = n.getRight().getLeft() != null ? n.getRight().getLeft().getHeight() : 0;

                if (rightHeight >= leftHeight)
                    n = rotateWithLeftChild(n);
                else
                    n = doubleWithRightChild(n);
            }

            if (newNode == null)
                n.setLeft(null);
        } else {
            n = (n.getLeft() != null) ? n.getLeft() : n.getRight();
        }

        if (n != null) {
            int leftHeight = n.getLeft() != null ? n.getLeft().getHeight() : 0;
            int rightHeight = n.getRight() != null ? n.getRight().getHeight() : 0;
            n.setHeight(Math.max(leftHeight, rightHeight) + 1);
        }

        return n;
    }


    // *********************************************************************
    public K findMin() {
        if (isEmpty())
            return null;
        return findMin(this.getRoot()).getK();
    }


    // *********************************************************************
    private Node<K,V> findMin(Node<K,V> n) {
        if (n == null)
            return null;

        while (n.getLeft() != null)
            n = n.getLeft();
        return n;
    }


    // *********************************************************************
    public K findMax() {
        if (isEmpty())
            return null;
        return findMax(this.getRoot()).getK();
    }


    // *********************************************************************
    private Node<K,V> findMax(Node<K,V> n) {
        if (n == null)
            return null;

        while (n.getRight() != null)
            n = n.getRight();
        return n;
    }


    // *********************************************************************
    private int max(int a, int b) {
        return a > b ? a : b;
    }


    // *********************************************************************
    private int getNodeHeight(Node<K, V> n) {
        return n == null ? -1 : n.getHeight();
    }


    // *********************************************************************
    public V findValueForKey(K key){
        return contains(key, this.getRoot()).getV();
    }


    // *********************************************************************
    private Node<K,V> contains(K key, Node<K,V> n) {
        if (n == null){
            return null;

        } else if (key.compareTo(n.getK()) < 0){
            return contains(key, n.getLeft());
        } else if (key.compareTo(n.getK()) > 0){
            return contains(key, n.getRight());
        }

        return n;
    }


    // *********************************************************************
    public ArrayList<K> inOrder(){
        ArrayList<K> nodes = new ArrayList<>();
        inOrder(this.getRoot(), nodes);
        return nodes;
    }


    // *********************************************************************
    private void inOrder(Node<K,V> n, ArrayList<K> nodes){
        if (n != null){
            inOrder(n.getLeft(), nodes);
            nodes.add(n.getK());
            inOrder(n.getRight(), nodes);
        }
    }


    // *********************************************************************
    public ArrayList<K> preOrder(){
        ArrayList<K> nodes = new ArrayList<>();
        preOrder(this.getRoot(), nodes);
        return nodes;
    }


    // *********************************************************************
    private void preOrder(Node<K,V> n, ArrayList<K> nodes){
        if (n != null){
            nodes.add(n.getK());
            preOrder(n.getLeft(), nodes);
            preOrder(n.getRight(), nodes);
        }
    }


    // *********************************************************************
    public ArrayList<K> postOrder(){
        ArrayList<K> nodes = new ArrayList<>();
        postOrder(this.getRoot(), nodes);
        return nodes;
    }


    // *********************************************************************
    private void postOrder(Node<K,V> n, ArrayList<K> nodes){
        if (n != null){
            preOrder(n.getLeft(), nodes);
            preOrder(n.getRight(), nodes);
            nodes.add(n.getK());
        }
    }


    // ===================
    // Diagnostic(s)    ==
    // ===================

    // *********************************************************************
    private boolean checkBalanceOfTree(Node<K,V> n) {

        boolean balancedRight = true, balancedLeft = true;
        int leftHeight = 0, rightHeight = 0;

        if (n.getRight() != null) {
            balancedRight = checkBalanceOfTree(n.getRight());
            rightHeight = getDepth(n.getRight());
        }

        if (n.left != null) {
            balancedLeft = checkBalanceOfTree(n.getLeft());
            leftHeight = getDepth(n.getLeft());
        }

        return balancedLeft && balancedRight && Math.abs(leftHeight - rightHeight) < 2;
    }


    // *********************************************************************
    private int getDepth(Node<K,V> n) {
        int leftHeight = 0, rightHeight = 0;

        if (n.right != null)
            rightHeight = getDepth(n.getRight());
        if (n.left != null)
            leftHeight = getDepth(n.getLeft());

        return Math.max(rightHeight, leftHeight)+1;
    }


    // *********************************************************************
    private boolean checkOrderingOfTree(Node<K,V> n) {
        if (n.getLeft() != null) {
            if (n.getLeft().getK().compareTo(n.getK()) > 0)
                return false;
            else
                return checkOrderingOfTree(n.getLeft());
        } else if (n.getRight() != null) {
            if (n.getRight().getK().compareTo(n.getK()) < 0)
                return false;
            else
                return checkOrderingOfTree(n.getRight());
        } else if (n.getLeft() == null && n.getRight() == null)
            return true;

        return true;
    }


    // *********************************************************************
    public void makeEmpty() {
        root = null;
    }


    // *********************************************************************
    public boolean isEmpty() {
        return root == null;
    }


    // ===================
    // Gets(s) & Set(s) ==
    // ===================

    // *********************************************************************
    public Node<K, V> getRoot() {
        return this.root;
    }


    // *********************************************************************
    public void setRoot(Node<K, V> node) {
        this.root = node;
    }
}
