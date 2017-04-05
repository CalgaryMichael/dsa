package mazePD;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;
import java.util.TreeMap;

/**
 * Maze is a public class that represents a maze.
 * The maze is a 3D collection of cells.
 * <p>
 * Each cell contains one of these values:
 * EMPTY - this space is empty and a droid is allowed to occupy this cell
 * BLOCK - this space is full (wall) and a droid is not allowed to occupy this cell
 * PORTAL_DN - this space contains a portal to a lower level, a droid is allowed to occupy this cell
 * PORTAL_UP - this space contains a portal to a upper level, a droid is allowed to occupy this cell
 * END - - this space contains the exit, a droid is allowed to occupy this cell
 * <p>
 * The maze is oriented where:
 * each level of the maze is number(z) 0 (top) to depth-1(bottom)
 * each level is a grid of cells where x=0 and y=0 is the top left cell.
 * <p>
 * Movement in the maze made by direction where:
 * D00  - decrease y (south)
 * D180 - increase y (north)
 * D90  - increase x (east)
 * D270	 - decrease x (west)
 * UP   - decrease z
 * DN	 - increase z
 *
 * @author(David North)
 */
public class Maze {
    public enum Direction {D00, D180, D90, D270, UP, DN}
    public enum Content {EMPTY, BLOCK, PORTAL_DN, PORTAL_UP, COIN, END, NA}
    public enum MazeMode {NORMAL, TEST}

    private int mazeDim;                            // number of cells in the x and y directions
    private int mazeDepth;                          // number of cells in the z direction (levels)
    private Cell mazeStartCell;                     // cell on level 0 that is the entrance
    private Cell[][][] maze;                        // array of cells that make up the maze
    private Random randomNum;                       // random number seed
    private TreeMap<String, DroidEntry> droids;     // collection of droids, searchable by name


    /**
     * Cell is a private class that represents a cell in a maze.
     * It contains the coordinates and contents of cell.
     *
     * @package mazePD
     * @author(David North)
     */
    private class Cell {
        private Coordinates coordinates;            // coordinates of this cell
        private Content cellContent;                // contents of this cell
        private boolean genVisit;                   // has this cell been visited during generation
        private int moveFromX, moveFromY;           // cell moved from during creation


        /**
         * Cell constructor creates an instance setting the values of x,y,z and contents
         *
         * @param x            int - x coordinate for a cell location (0..maze dim-1)
         * @param y            int - y coordinate for a cell location (0..maze dim-1)
         * @param z            int - z coordinate (level) for a cell location (0..maze dim-1)
         * @param cellContent  Contents - what is in the cell
         */
        protected Cell(int x, int y, int z, Content cellContent) {
            coordinates = new Coordinates(x, y, z);
            setCellContent(cellContent);
        }


        /**
         * canBeOccupied() based on the contents of the cell can a droid move to it
         *
         * @return Boolean - true if the cell can be occupied and false if it can't
         */
        protected boolean canBeOccupied() {
            // return getCellContent() != Content.BLOCK && getCellContent != Content.NA
            return getCellContent() == Content.EMPTY || getCellContent() == Content.COIN ||
                    getCellContent() == Content.PORTAL_DN ||
                    getCellContent() == Content.PORTAL_UP ||
                    getCellContent() == Content.END;
        }


        /**
         * canBeStart() based on the contents of the cell can this cell be the start
         * cell for the level.  It can if it is EMPTY or COIN.
         *
         * @return Boolean - true if the cell can be level start and false if it can't
         */
        protected boolean canBeStart() {
            return getCellContent() == Content.EMPTY || getCellContent() == Content.COIN;
        }


        /**
         * isGenVisit() has this cell been visit during the maze generation process
         *
         * @return Boolean - true if the cell has been visit and false if it has not
         */
        protected boolean isGenVisit() {
            return genVisit;
        }


        /**
         * setGenVisit() set the value of genVisit
         *
         * @param genVisit - value to set genVisit to
         */
        protected void setGenVisit(boolean genVisit) {
            this.genVisit = genVisit;
        }


        protected int getMoveFromY() {
            return moveFromY;
        }


        protected void setMoveFromY(int moveFromY) {
            this.moveFromY = moveFromY;
        }


        protected int getMoveFromX() {
            return moveFromX;
        }


        protected void setMoveFromX(int moveFromX) {
            this.moveFromX = moveFromX;
        }


        protected void setMoveFrom(int x, int y) {
            setMoveFromX(x);
            setMoveFromY(y);
        }


        protected int getLocX() {
            return getCoordinates().getX();
        }


        protected void setLocX(int locX) {
            getCoordinates().setX(locX);
        }


        protected int getLocY() {
            return getCoordinates().getY();
        }


        protected void setLocY(int locY) {
            getCoordinates().setY(locY);
        }


        protected int getLocZ() {
            return getCoordinates().getZ();
        }


        protected void setLocZ(int locZ) {
            this.getCoordinates().setZ(locZ);
        }


        protected Content getCellContent() {
            return cellContent;
        }


        protected void setCellContent(Content cellContent) {
            this.cellContent = cellContent;
        }


        protected Coordinates getCoordinates() {
            return coordinates;
        }


        protected void setCoordinates(Coordinates coordinates) {
            this.coordinates = coordinates;
        }


        protected String locString() {
            return "[" + Integer.toString(getLocX()) + "," + Integer.toString(getLocY()) + "," +
                    Integer.toString(getLocZ()) + "]";
        }


        public String toString() {
            switch (cellContent) {
                case EMPTY:
                    return "[ ]";
                case BLOCK:
                    return "[*]";
                case PORTAL_DN:
                    return "[D]";
                case PORTAL_UP:
                    return "[P]";
                case COIN:
                    return "[O]";
                case END:
                    return "[E]";
                default:
                    return "[X]";
            }
        }
    }


    /**
     * DroidEntry is a private class that represents a droid in a maze.
     * It contains a pointer to the droid and current coordinates of the droid in the maze.
     *
     * @package mazePD
     * @author(David North)
     */
    private class DroidEntry {
        private DroidInterface droid;
        private Coordinates coordinates;


        public DroidEntry(DroidInterface droid, Coordinates coordinates) {
            this.droid = droid;
            this.coordinates = coordinates;
        }


        protected DroidInterface getDroid() {
            return this.droid;
        }


        protected void runMaze() {
            this.getDroid().runMaze();
        }


        protected Coordinates getCoordinates() {
            return coordinates;
        }


        protected void setCoordinates(Coordinates coordinates) {
            this.coordinates = coordinates;
        }


        protected String getName() {
            return getDroid().getName();
        }
    }


    /**
     * Maze() default constructor creates the collection of Droids
     */
    public Maze() {
        droids = new TreeMap<String, DroidEntry>();
    }


    /**
     * Maze() constructor creates an instance setting the dim,depth, mode
     * and creating the maze.
     *
     * @param mazeDim   - the dimension for a level of the maze
     * @param mazeDepth - the depth of the maze (number of levels)
     * @param mode      - NORMAL (random maze), TEST (same maze every time)
     */
    public Maze(int mazeDim, int mazeDepth, MazeMode mode) {
        this();
        setMazeDim(mazeDim);
        setMazeDepth(mazeDepth);
        randomNum = new Random();
        if (mode == MazeMode.NORMAL)
            randomNum.setSeed(System.currentTimeMillis());
        else
            randomNum.setSeed(37);
        initializeMaze();
    }


    // ===================
    // Maze Behavior    ==
    // ===================

    private void initializeMaze() {
        maze = new Cell[getMazeDim()][getMazeDim()][getMazeDepth()];
        for (int z = getMazeDepth() - 1; z >= 0; z--) {
            for (int x = 0; x < getMazeDim(); x++)
                for (int y = 0; y < getMazeDim(); y++)
                    maze[x][y][z] = new Cell(x, y, z, Content.BLOCK);
            _createMaze(z);
        }

        setMazeStartCell(getStart(-1));
    }


    private void _createMaze(int level) {
        int portalX, portalY;

        if (level + 1 == getMazeDepth()) {
            portalX = randomNum.nextInt(getMazeDim());
            portalY = randomNum.nextInt(getMazeDim());
        } else {
            Cell cell = getStart(level);
            portalX = cell.getLocX();
            portalY = cell.getLocY();
        }

        if (level < getMazeDepth() - 1)
            maze[portalX][portalY][level].setCellContent(Content.PORTAL_DN);
        else
            maze[portalX][portalY][level].setCellContent(Content.END);
        _createPath(maze[portalX][portalY][level]);
    }


    private void _createPath(Cell startCell) {
        Stack<Cell> cellStack = new Stack<>();
        Cell currentCell = null;
        cellStack.push(startCell);

        while (!cellStack.isEmpty()) {
            currentCell = cellStack.pop();
            currentCell.setGenVisit(true);
            ArrayList<Cell> adjacentCells = getAdjCells(currentCell);
            int randomAdj = randomNum.nextInt(adjacentCells.size());

            for (int i = 0; i < adjacentCells.size(); i++) {
                int index = (i + randomAdj) % adjacentCells.size();
                Cell cell = adjacentCells.get(index);

                if (isOkForPath(currentCell, cell)) {
                    cell.setMoveFrom(currentCell.getLocX(), currentCell.getLocY());
                    cell.setCellContent(Content.EMPTY);
                    cellStack.push(cell);
                }
            }
        }
    }


    private Cell getStart(int level) {
        ArrayList<Cell> openCells = new ArrayList<>();

        for (int i = 0; i < getMazeDim(); i++) {
            for (int j = 0; j < getMazeDim(); j++) {
                if (maze[i][j][level + 1].canBeStart()) {
                    openCells.add(maze[i][j][level + 1]);
                }
            }
        }

        Cell startCell = openCells.get(randomNum.nextInt(openCells.size()));

        if (level < getMazeDepth() - 1)
            maze[startCell.getLocX()][startCell.getLocY()][level == -1 ? 0 : level + 1].setCellContent(Content.PORTAL_UP);
        return maze[startCell.getLocX()][startCell.getLocY()][level == -1 ? 0 : level];
    }


    private ArrayList<Cell> getAdjCells(Cell cell) {
        int[][] offset = {{-1, 0}, {0, 1}, {0, -1}, {1, 0}};

        ArrayList<Cell> cells = new ArrayList<Cell>();

        for (int i = 0; i < 4; i++) {
            int adjX = cell.getLocX() + offset[i][0];
            int adjY = cell.getLocY() + offset[i][1];

            if (adjX >= 0 && adjX < getMazeDim() && adjY >= 0 && adjY < getMazeDim())
                cells.add(maze[adjX][adjY][cell.getLocZ()]);
        }

        return cells;

    }


    private Cell getCellDir(Cell currentCell, Direction direction) {
        switch (direction) {
            case D00:
                if (currentCell.getLocY() - 1 >= 0)
                    return maze[currentCell.getLocX()][currentCell.getLocY() - 1][currentCell.getLocZ()];
                else
                    return currentCell;
            case D90:
                if (currentCell.getLocX() + 1 < getMazeDim())
                    return maze[currentCell.getLocX() + 1][currentCell.getLocY()][currentCell.getLocZ()];
                else
                    return currentCell;
            case D180:
                if (currentCell.getLocY() + 1 < getMazeDim())
                    return maze[currentCell.getLocX()][currentCell.getLocY() + 1][currentCell.getLocZ()];
                else
                    return currentCell;
            case D270:
                if (currentCell.getLocX() - 1 >= 0)
                    return maze[currentCell.getLocX() - 1][currentCell.getLocY()][currentCell.getLocZ()];
                else
                    return currentCell;
            case DN:
                if (currentCell.getLocZ() + 1 < getMazeDepth() && currentCell.getCellContent() == Content.PORTAL_DN)
                    return maze[currentCell.getLocX()][currentCell.getLocY()][currentCell.getLocZ() + 1];
                else
                    return currentCell;
            case UP:
                if (currentCell.getLocZ() - 1 >= 0 && currentCell.getCellContent() == Content.PORTAL_UP)
                    return maze[currentCell.getLocX()][currentCell.getLocY()][currentCell.getLocZ() - 1];
                else
                    return currentCell;
            default:
                return currentCell;
        }
    }



    // ===================
    // Move Behavior    ==
    // ===================

    /**
     * enterMaze() have droid enter the maze at the start location
     *
     * @param droid - a droid that implements the DroidInterface that is in the maze
     * @return coord Coordinates - coordinates of the cell were the droid is now located
     */
    public Coordinates enterMaze(DroidInterface droid) {
        Coordinates coord = getMazeStartCell().getCoordinates();
        addDroid(droid, coord);
        return coord;
    }


    /**
     * move() move the droid one cell in the indicated direction:
     * D00  - decrease y
     * D180 - increase y
     * D90  - increase x
     * D270	 - decrease x
     * UP   - decrease z
     * DN	 - increase z
     * <p>
     * if the cell the droid wants to move to contains BLOCK or outside the maze
     * then the droid remains in its current location and does not move
     *
     * @param droid     DroidInterface - a droid that is in the maze
     * @param direction Direction - the direction the droid wants to move
     * @return Coordinates - coordinates of the droid's current location in maze
     */
    public Coordinates move(DroidInterface droid, Direction direction) {
        Cell currentCell = getCellForDroid(droid);
        if ((direction == Direction.DN && currentCell.getCellContent() != Content.PORTAL_DN) ||
                (direction == Direction.UP && currentCell.getCellContent() != Content.PORTAL_UP))
            return currentCell.getCoordinates();

        Cell nextCell = getCellDir(currentCell, direction);
        if (nextCell.canBeOccupied()) {
            if (currentCell.getCellContent() == Content.EMPTY)
                currentCell.setCellContent(Content.COIN);
            findDroid(droid).setCoordinates(nextCell.getCoordinates());
            return nextCell.getCoordinates();
        } else {
            return currentCell.getCoordinates();
        }
    }


    public boolean moveBack(DroidInterface droid, Coordinates coordinates) {
        Cell current = getCellForDroid(droid);
        if (current.getCellContent() == Content.COIN)
            current.setCellContent(Content.EMPTY);

        for (Cell c : getAdjCells(current)) {
            if (c.getCoordinates() == coordinates) {
                findDroid(droid).setCoordinates(c.getCoordinates());
                return true;
            }
        }

        return false;
    }


    /**
     * usePortal() move the droid through a portal to a different level
     *
     * @param droid     DroidInterface - a droid that implements the DroidInterface that is in the maze
     * @param direction Direction - the direction to move through portal
     * @return Coordinates - coordinates of the cell were the droid is now located
     */
    public Coordinates usePortal(DroidInterface droid, Direction direction) {
        Cell currentCell = getCellForDroid(droid);
        if (currentCell.getCellContent() == Content.PORTAL_DN) {
            if (currentCell.getLocZ() < getMazeDim() - 1) {
                Coordinates newCoord = getCellDir(currentCell, direction).getCoordinates();
                findDroid(droid).setCoordinates(newCoord);
                newCoord.setVisited(true);
                return newCoord;
            }
        }
        if (currentCell.getCellContent() == Content.PORTAL_UP) {
            if (currentCell.getLocZ() > 0) {
                Coordinates newCoord = getCellDir(currentCell, direction).getCoordinates();
                findDroid(droid).setCoordinates(newCoord);
                newCoord.setVisited(true);
                return newCoord;
            }
        }
        return currentCell.getCoordinates();
    }


    public ArrayList<Direction> getAvailableMovesForDroid(DroidInterface droid) {
        Cell current = getCellForDroid(droid);
        ArrayList<Direction> availableDir = new ArrayList<>();

        for (Direction d : Direction.values()) {
            Cell next = getCellDir(current, d);
            Coordinates nextCoord = next.getCoordinates();
            if (next.canBeOccupied() && !nextCoord.getVisited())
                availableDir.add(d);
        }

        return availableDir;
    }


    private Boolean isOkForPath(Cell moveFromCell, Cell moveToCell) {
        if (moveToCell.isGenVisit())
            return false;

        if (moveFromCell.getLocX() == moveToCell.getLocX()) {
            if (moveToCell.getLocX() - 1 >= 0 && moveToCell.getLocX() + 1 < getMazeDim())

                return maze[moveToCell.getLocX() - 1][moveToCell.getLocY()][moveToCell.getLocZ()].getCellContent() == Content.BLOCK &&
                        maze[moveToCell.getLocX() + 1][moveToCell.getLocY()][moveToCell.getLocZ()].getCellContent() == Content.BLOCK;

            if (moveToCell.getLocX() - 1 < 0)
                if (maze[moveToCell.getLocX() + 1][moveToCell.getLocY()][moveToCell.getLocZ()].getCellContent() == Content.BLOCK)
                    return true;

            if (moveToCell.getLocX() + 1 >= getMazeDim())
                if (maze[moveToCell.getLocX() - 1][moveToCell.getLocY()][moveToCell.getLocZ()].getCellContent() == Content.BLOCK)
                    return true;

        } else {
            if (moveToCell.getLocY() - 1 >= 0 && moveToCell.getLocY() + 1 < getMazeDim())

                if (maze[moveToCell.getLocX()][moveToCell.getLocY() - 1][moveToCell.getLocZ()].getCellContent() == Content.BLOCK &&
                        maze[moveToCell.getLocX()][moveToCell.getLocY() + 1][moveToCell.getLocZ()].getCellContent() == Content.BLOCK)
                    return true;

            if (moveToCell.getLocY() - 1 < 0)
                if (maze[moveToCell.getLocX()][moveToCell.getLocY() + 1][moveToCell.getLocZ()].getCellContent() == Content.BLOCK)
                    return true;

            if (moveToCell.getLocY() + 1 >= getMazeDim())
                if (maze[moveToCell.getLocX()][moveToCell.getLocY() - 1][moveToCell.getLocZ()].getCellContent() == Content.BLOCK)
                    return true;
        }

        return false;
    }


    // ========================
    // DroidEntry Behavior   ==
    // ========================

    private TreeMap<String, DroidEntry> getDroids() {
        return this.droids;
    }


    private void addDroid(DroidInterface droid, Coordinates coordinates) {
        DroidEntry droidEntry = new DroidEntry(droid, coordinates);
        getDroids().put(droidEntry.getName(), droidEntry);
    }


    private DroidEntry findDroid(DroidInterface droid) {
        return getDroids().get(droid.getName());
    }


    private Cell getCellForDroid(DroidInterface droid) {
        Coordinates coord = findDroid(droid).getCoordinates();
        return maze[coord.getX()][coord.getY()][coord.getZ()];
    }



    // ===================
    // Get(s) & Set(s)  ==
    // ===================

    private void setMazeDim(int mazeDim) {
        this.mazeDim = mazeDim;
    }


    public int getMazeDim() {
        return this.mazeDim;
    }


    private void setMazeDepth(int mazeDepth) {
        this.mazeDepth = mazeDepth;
    }


    public int getMazeDepth() {
        return this.mazeDepth;
    }


    private void setMazeStartCell(Cell startCell) {
        this.mazeStartCell = startCell;
    }


    private Cell getMazeStartCell() {
        return this.mazeStartCell;
    }


    public Coordinates getMazeStartCoord() {
        return getMazeStartCell().getCoordinates();
    }


    public String getCellStringForCoord(Coordinates coordinates) {
        return maze[coordinates.getX()][coordinates.getY()][coordinates.getZ()].toString();
    }


    public Coordinates getCoordinatesForValues(int x, int y, int z) {
        return maze[x][y][z].getCoordinates();
    }


    public Content getContentForCoord(Coordinates coordinates) {
        return maze[coordinates.getX()][coordinates.getY()][coordinates.getZ()].getCellContent();
    }


    public Random getRandomNum() {
        return this.randomNum;
    }


    /**
     * toStringLevel() get string that represents a level of the maze
     *
     * @param level int - level of maze to represent in string
     * @return String - represents a level of the maze
     */
    public String[] toStringLevel(int level) {
        String[] mazeLevel = new String[getMazeDim()];
        for (int y = 0; y < getMazeDim(); y++) {
            mazeLevel[y] = "";
            for (int x = 0; x < getMazeDim(); x++) {
                mazeLevel[y] += maze[x][y][level].toString();
            }
        }

        return mazeLevel;
    }


    /**
     * toString() get string that represents the maze
     *
     * @return String - represents the maze
     */
    public String toString() {
        return "Depth: " + getMazeDepth() + " Dim: " + getMazeDim() + getMazeStartCell().getCoordinates().toString();
    }
}
