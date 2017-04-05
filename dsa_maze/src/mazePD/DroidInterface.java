/**
* DroidInterface is a public interface that sets the methods that maze expects
* of a droid that enters the maze. 
* 
* @package mazePD
* @author(David North) 
*/

package mazePD;


import java.util.ArrayList;

public interface DroidInterface {
	public String getName();

    public int getMoveCount();

    public boolean runMaze();
}
