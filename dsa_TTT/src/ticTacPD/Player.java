package ticTacPD;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by calgarymichael on 4/6/17.
 */
public class Player {
    public enum PlayerType { HUMAN, COMPUTER };

    private PlayerType playerType;
    private MoveType moveType;
    private int win;
    private int loss;


    // ===================
    // Class            ==
    // ===================

    private class Node<T> {
        private List<Node<T>> children;
        public Node<T> parent = null;
        private T data = null;
        private int score;

        public Node(T data) {
            this.data = data;
            this.children = new ArrayList<>();
            this.score = 0;
        }

        public Node(T data, Node<T> parent) {
            this(data);
            this.parent = parent;
        }

        public List<Node<T>> getChildren() {
            return children;
        }

        public void setParent(Node<T> parent) {
            parent.addChild(this);
        }

        public void removeParent() {
            this.parent = null;
        }

        public Node<T> addChild(T data) {
            Node<T> child = new Node<T>(data);
            child.parent = this;
            this.children.add(child);
            return child;
        }

        public void addChild(Node<T> child) {
            child.parent = this;
            this.children.add(child);
        }

        public T getData() {
            return this.data;
        }

        public void setData(T data) {
            this.data = data;
        }

        public boolean isRoot() {
            return (this.parent == null);
        }

        public boolean isLeaf() {
            return this.children.isEmpty();
        }

        public int getScore() {
            return this.score;
        }

        public void setScore(int score) {
            this.score = score;
        }
    }


    // ===================
    // Constructor(s)   ==
    // ===================

    public Player(MoveType moveType) {
        this.win = 0;
        this.loss = 0;
        this.moveType = moveType;
        this.playerType = PlayerType.HUMAN;
    }

    public Player(PlayerType playerType, MoveType moveType) {
        this.win = 0;
        this.loss = 0;
        this.moveType = moveType;
        this.playerType = playerType;
    }


    // ===================
    // Method(s)        ==
    // ===================

    public int[] generateBestMove(Board board, Player opponent) {
        Node<Board> tree = new Node<>(board);
        buildTree(tree, this, opponent);
        ArrayList<Node<Board>> leaves = getLeaves(tree);
        return determineMove(leaves);
    }


    private void buildTree(Node<Board> node, Player active, Player passive) {
        Board current = node.getData();
        ArrayList<int[]> openMoves = current.getOpenCoordinates();
        for (int[] pos : openMoves) {
            Board boardOption = current.clone();
            if (!boardOption.isComplete()) {
                boardOption.move(active, pos[0], pos[1]);
                buildTree(node.addChild(boardOption), passive, active);
            }
            calculateScore(node);
        }
    }


    private void calculateScore(Node<Board> node) {
        int score = 0;

        if (!node.isRoot())
            score += node.parent.score;

        if (node.data.isComplete()) {
            if (node.data.getWinner() == MoveType.X)
                score -= 1;
            else if (node.data.getWinner() == MoveType.O)
                score += 1;
        }

        node.setScore(score);
    }


    private int[] determineMove(ArrayList<Node<Board>> leaves) {
        int i = ThreadLocalRandom.current().nextInt(leaves.size());
        Node<Board> current = leaves.get(i);
        Node<Board> previous = new Node<>(current.getData());

        while (!current.isRoot()) {
            previous = current;
            current = current.parent;
        }

        return current.getData().getDifference(previous.getData());
    }


    private ArrayList<Node<Board>> getLeaves(Node<Board> tree) {
        ArrayList<Node<Board>> nodes = new ArrayList<>();

        for (Node<Board> n : tree.getChildren()) {
            if (n.isLeaf()) {
                if (n.score > 0)
                    nodes.add(n);
            } else {
                nodes.addAll(getLeaves(n));
            }
        }

        return nodes;
    }


    // ===================
    // Get(s) & Set(s)  ==
    // ===================

    public int getWin() {
        return this.win;
    }

    public void setWin(int win) {
        this.win = win;
    }

    public int getLoss() {
        return this.loss;
    }

    public void setLoss(int loss) {
        this.loss = loss;
    }

    public MoveType getMoveType() {
        return this.moveType;
    }

    public void setMoveType(MoveType type) {
        this.moveType = type;
    }

    public PlayerType getPlayerType() {
        return this.playerType;
    }

    public void setPlayerType(PlayerType playerType) {
        this.playerType = playerType;
    }
}
