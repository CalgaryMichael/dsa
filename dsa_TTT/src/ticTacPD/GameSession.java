package ticTacPD;

/**
 * Created by calgarymichael on 4/6/17.
 */
public class GameSession {
    private enum Status { READY, IN_SESSION, COMPLETE };

    private Status sessionStatus;
    private Player currentPlayer;
    private Player computer;
    private Board board;


    // ===================
    // Constructor(s)   ==
    // ===================

    public GameSession(Player currentPlayer) {
        this.board = new Board();
        this.currentPlayer = currentPlayer;
        this.computer = new Player(Player.PlayerType.COMPUTER, getComputerType());
        this.sessionStatus = Status.READY;
    }


    // ===================
    // Method(s)        ==
    // ===================

    public void startGame() {
        this.sessionStatus = Status.IN_SESSION;
    }


    public boolean move(int x, int y) {
        if (sessionStatus == Status.IN_SESSION) {
            boolean moved = this.board.move(this.currentPlayer, x, y);
            if (moved) {
                if (!this.board.isComplete()) {
                    generateComputer();
                    return true;
                }
            }
        }

        return false;
    }


    public void generateComputer() {
        int[] bestMove = this.computer.generateBestMove(this.board, this.currentPlayer);
        if (bestMove != null)
            this.board.move(this.computer, bestMove[0], bestMove[1]);
        else
            System.out.println("No Moves");
    }


    public boolean isComplete() {
        if (this.board.isComplete()) {
            this.sessionStatus = Status.COMPLETE;
            return true;
        }

        return false;
    }


    public MoveType getWinner() {
        return this.board.winner();
    }


    private MoveType getComputerType() {
        return this.currentPlayer.getMoveType() == MoveType.X ? MoveType.O : MoveType.X;
    }


    public String toString() {
        return this.board.toString();
    }
}
