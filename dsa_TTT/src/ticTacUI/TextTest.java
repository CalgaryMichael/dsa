package ticTacUI;

import ticTacPD.GameSession;
import ticTacPD.MoveType;
import ticTacPD.Player;

import java.util.Scanner;

/**
 * Created by calgarymichael on 4/11/17.
 */
public class TextTest {
    private static Player human;
    private static GameSession game;

    private static void setUp(MoveType moveType) {
        human = new Player(moveType);
        game = new GameSession(human);
        game.startGame();
    }

    public static void main(String[] args) {
//        System.out.println("MoveType: 1 for X, 2 for O");
//        Scanner sc = new Scanner(System.in);
//        int i = sc.nextInt();

//        MoveType moveType = MoveType.X;
//        if (i != 1)
//            moveType = MoveType.O;
        setUp(MoveType.X);
        System.out.println(game.toString());

        while(!game.isComplete()) {
            System.out.print("\nMove (x,y): ");
            Scanner sc = new Scanner(System.in);
            String line = sc.nextLine();
            String[] pos = line.split(",");
            game.move(Integer.parseInt(pos[0]) - 1, Integer.parseInt(pos[1]) - 1);
            System.out.println(game.toString());
        }

        System.out.println("Game Over!");
        if (game.getWinner() == null)
            System.out.println("Winner: Draw");
        else
            System.out.println("Winner: " + game.getWinner().toString());
    }
}
