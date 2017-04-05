package mazeUI;


import mazePD.Droid;
import mazePD.Maze;

/**
 * Created by calgarymichael on 3/23/17.
 */
public class TextOutput {
    public static void main(String[] args) {
        mazeTest(4, 8);
    }

    public static void mazeTest(int levels, int dim) {
        System.out.println("Maze Test");
        Maze maze = new Maze(dim, levels, Maze.MazeMode.NORMAL);
        Droid droid = new Droid(maze, "test");
        if (droid.runMaze())
            System.out.println("Run Successful - Moves: " + droid.getMoveCount());
        else
            System.out.println("Run Failed - Moves: " + droid.getMoveCount());
        System.out.println("Maze - " + maze.toString());

        for (int z = 0; z < levels; z++) {
            System.out.println("Level - " + z);
            String[] mazeArray = maze.toStringLevel(z);
            String[] droidArray = droid.toStringLevel(z);

            for (int y = 0; y < dim; y++)
                System.out.println(mazeArray[y] + "\t\t" + droidArray[y]);
        }
    }
}
