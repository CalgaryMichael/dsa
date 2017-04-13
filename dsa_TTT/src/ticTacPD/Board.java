package ticTacPD;

import java.util.ArrayList;

/**
 * Created by calgarymichael on 4/6/17.
 */
public class Board {
    private ArrayList<Position> cells;
    private MoveType winner;


    // ===================
    // Class            ==
    // ===================

    private class Position implements Cloneable {
        private int x;
        private int y;
        private MoveType playerOccupied;


        // ===================
        // Constructor(s)   ==
        // ===================

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
            this.playerOccupied = null;
        }

        public Position(MoveType playerOccupied, int x, int y) {
            this.x = x;
            this.y = y;
            this.playerOccupied = playerOccupied;
        }


        // ===================
        // Method(s)        ==
        // ===================

        public boolean equals(int x, int y) {
            return x == this.x && y == this.y;
        }


        public void clearPlayer() {
            this.playerOccupied = null;
        }


        // ===================
        // Get(s) & Set(s)  ==
        // ===================

        public int getX() {
            return this.x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return this.y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public MoveType getPlayerOccupied() {
            return this.playerOccupied;
        }

        public void setPlayerOccupied(MoveType player) {
            this.playerOccupied = player;
        }

        public void setPlayerOccupied(Player player) {
            this.playerOccupied = player.getMoveType();
        }

        public int[] toArray() {
            return new int[] {x, y};
        }

        public Object clone() throws CloneNotSupportedException {
//            return new Position(this.playerOccupied, this.x, this.y);
            return super.clone();
        }

        public String toString() {
            if (this.playerOccupied == null)
                return "[ ]";
            else
                return "[" + this.playerOccupied +"]";
        }
    }


    // ===================
    // Constructor(s)   ==
    // ===================

    public Board() {
        this.cells = initializeBoard();
    }


    public Board(ArrayList<Position> pos) {
        this.cells = pos;
    }


    // ===================
    // Method(s)        ==
    // ===================

    private ArrayList<Position> initializeBoard() {
        ArrayList<Position> cells = new ArrayList<>();
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                cells.add(new Position(x, y));
            }
        }

        return cells;
    }


    public boolean move(Player player, int x, int y) {
        Position pos = findPositionforXandY(x, y);
        if (isPosOpen(pos)) {
            pos.setPlayerOccupied(player);
            return true;
        }

        return false;
    }


    private ArrayList<Position> getOpenPos() {
        ArrayList<Position> open = new ArrayList<>();

        for (Position p : cells) {
            if (isPosOpen(p))
                open.add(p);
        }


        return open;
    }


    // used for tree generation
    public ArrayList<int[]> getOpenCoordinates() {
        ArrayList<int[]> open = new ArrayList<>();

        for (Position p : this.cells) {
            if (isPosOpen(p))
                open.add(new int[]{p.getX(), p.getY()});
        }

        return open;
    }


    private Position findPositionforXandY(int x, int y) {
        for (Position p : cells) {
            if (p.equals(x, y))
                return p;
        }

        return null;
    }


    public boolean isPosOpen(Position pos) {
        return pos != null && pos.getPlayerOccupied() == null;
    }


    public int[] getDifference(Board board) {
        for (Position p : this.cells) {
            if (p.getPlayerOccupied() != board.findPositionforXandY(p.getX(), p.getY()).getPlayerOccupied())
                return new int[] {p.getX(), p.getY()};
        }

        return null;
    }


    public MoveType winner() {
        // diagonal win (left-right)
        Position pos = findPositionforXandY(0, 0);
        if (pos.getPlayerOccupied() != null && pos.getPlayerOccupied() == findPositionforXandY(1, 1).getPlayerOccupied()) {
            if (pos.getPlayerOccupied() == findPositionforXandY(2, 2).getPlayerOccupied()) {
                this.winner = pos.getPlayerOccupied();
                return this.winner;
            }
        }

        // diagonal win (right-left)
        pos = findPositionforXandY(0, 2);
        if (pos.getPlayerOccupied() != null && pos.getPlayerOccupied() == findPositionforXandY(1, 1).getPlayerOccupied()) {
            if (pos.getPlayerOccupied() == findPositionforXandY(2, 0).getPlayerOccupied()) {
                this.winner = pos.getPlayerOccupied();
                return this.winner;
            }
        }

        for (int i = 0; i < 3; i++) {
            // vertical win
            pos = findPositionforXandY(0, i);
            if (pos.getPlayerOccupied() != null && pos.getPlayerOccupied() == findPositionforXandY(1, i).getPlayerOccupied()) {
                if (pos.getPlayerOccupied() == findPositionforXandY(2, i).getPlayerOccupied()) {
                    this.winner = pos.getPlayerOccupied();
                    return this.winner;
                }
            }

            // horizontal win
            pos = findPositionforXandY(i, 0);
            if (pos.getPlayerOccupied() != null && pos.getPlayerOccupied() == findPositionforXandY(i, 1).getPlayerOccupied()) {
                if (pos.getPlayerOccupied() == findPositionforXandY(i, 2).getPlayerOccupied()) {
                    this.winner = pos.getPlayerOccupied();
                    return this.winner;
                }
            }
        }

        return null;
    }


    public boolean isComplete() {
        return this.winner() != null || this.getOpenPos().isEmpty();
    }


    @Override
    protected Board clone() {
        ArrayList<Position> pos = new ArrayList<>();
        for (Position p : this.cells) {
            try {
                pos.add((Position)p.clone());
            } catch  (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        return new Board(pos);
    }


    public String toString() {
        String response = "";

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                Position pos = findPositionforXandY(x, y);
                if (pos != null)
                    response += pos.toString();
            }

            response += "\n";
        }

        return response;
    }


    // ===================
    // Get(s) & Set(s)  ==
    // ===================

    public MoveType getWinner() {
        return this.winner;
    }

    public void setWinner(MoveType winner) {
        this.winner = winner;
    }
}
