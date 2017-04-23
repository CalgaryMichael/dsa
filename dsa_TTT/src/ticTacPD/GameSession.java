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
        this.board = new Board();
        this.sessionStatus = Status.IN_SESSION;
    }


    public int[] move(int x, int y) {
        if (isPlayable()) {
            boolean moved = this.board.move(this.currentPlayer, x, y);
            if (moved) {
                if (!isComplete())
                    return generateComputer();
            }
        }

        return null;
    }


    public int[] generateComputer() {
        int[] bestMove = this.computer.generateBestMove(this.board, this.currentPlayer);
        if (bestMove != null)
            this.board.move(this.computer, bestMove[0], bestMove[1]);
        else
            System.out.println("No Moves");
        return bestMove;
    }


    public boolean isComplete() {
        if (this.board.isComplete()) {
            this.sessionStatus = Status.COMPLETE;
            return true;
        }

        return false;
    }


    public boolean isPlayable() {
        return this.sessionStatus == Status.IN_SESSION;
    }


    public MoveType getWinner() {
        return this.board.winner();
    }


    public MoveType getComputerType() {
        return this.currentPlayer.getMoveType() == MoveType.X ? MoveType.O : MoveType.X;
    }


    public String toString() {
        return this.board.toString();
    }
}
