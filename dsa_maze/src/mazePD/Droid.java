package mazePD;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by calgarymichael on 3/23/17.
 */
public class Droid implements DroidInterface {
    private Maze currentMaze;
    private String name;
    private int moves;


    // ===================
    // Constructor(s)   ==
    // ===================

    public Droid() {
        this.moves = 0;
    }

    public Droid(Maze maze, String name) {
        this();
        this.currentMaze = maze;
        this.name = name;
    }


    // ===================
    // Method(s)        ==
    // ===================

    @Override
    public boolean runMaze() {
        Coordinates start = this.currentMaze.enterMaze(this);

        Stack<Coordinates> coordStack = new Stack<>();
        Coordinates current = null;
        coordStack.push(start);

        while (!coordStack.isEmpty()) {
            current = coordStack.peek();
            current.setVisited(true);
            Maze.Content cellContent = this.currentMaze.getContentForCoord(current);

            if (cellContent == Maze.Content.END)
                return true;

            // find next move
            Coordinates newCoord = current;
            ArrayList<Maze.Direction> availableDir = this.currentMaze.getAvailableMovesForDroid(this);
            int size = availableDir.size();

            if (size < 1) {
                coordStack.pop();
                Coordinates previous = coordStack.peek();
                this.currentMaze.moveBack(this, previous);
            } else {
                // check to see if droid should usePortal
                if (availableDir.contains(Maze.Direction.UP) && cellContent == Maze.Content.PORTAL_UP) {
                    newCoord = this.currentMaze.usePortal(this, Maze.Direction.UP);

                } else if (availableDir.contains(Maze.Direction.DN) && cellContent == Maze.Content.PORTAL_DN) {
                    newCoord = this.currentMaze.usePortal(this, Maze.Direction.DN);

                // check to see other moves
                } else {
                    int randomDir = this.currentMaze.getRandomNum().nextInt(size);
                    Maze.Direction next = availableDir.get(randomDir);
                    newCoord = this.currentMaze.move(this, next);
                }

                if (!newCoord.equals(current)) {
                    coordStack.push(newCoord);
                    moves++;
                }
            }
        }

        return false;
    }


    @Override
    public String getName() {
        return this.name;
    }


    @Override
    public int getMoveCount() {
        return this.moves;
    }


    public String[] toStringLevel(int z) {
        int mazeDim = this.currentMaze.getMazeDim();
        String[] mazeLevel = new String[mazeDim];
        for (int y = 0; y < mazeDim; y++) {
            mazeLevel[y] = "";
            for (int x = 0; x < mazeDim; x++) {
                Coordinates current = this.currentMaze.getCoordinatesForValues(x, y, z);
                if (current.getVisited()) {
                    mazeLevel[y] += this.currentMaze.getCellStringForCoord(current);
                } else {
                    mazeLevel[y] += "[#]";
                }
            }
        }

        return mazeLevel;
    }
}
