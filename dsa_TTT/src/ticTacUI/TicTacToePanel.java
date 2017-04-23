package ticTacUI;

import ticTacPD.GameSession;
import ticTacPD.MoveType;
import ticTacPD.Player;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by calgarymichael on 4/21/17.
 */
public class TicTacToePanel extends JPanel {
    private static final int ROWS = 3;
    private JButton[][] buttonGrid = new JButton[ROWS][ROWS];
    private JLabel message = new JLabel();
    private JPanel buttonPanel = new JPanel();
    private Player currentPlayer = new Player(MoveType.X);
    private GameSession gs;

    public TicTacToePanel() {
        this.gs = new GameSession(currentPlayer);
        gs.startGame();

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(2, 1, 1, 1));

        JButton start = new JButton("Reset");
        start.addActionListener(new resetGame());
        controlPanel.add(start);
        controlPanel.add(message);
        add(controlPanel);

        buttonPanel = setBoard();
        add(buttonPanel);
    }


    private JPanel setBoard() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(ROWS, ROWS, 1, 1));
        buttonGrid = new JButton[ROWS][ROWS];

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < ROWS; j++) {
                JButton button = new JButton();

                // add move
                button.addActionListener(new move(i, j));
                buttonPanel.add(button);
                buttonGrid[i][j] = button;
            }
        }

        return buttonPanel;
    }


    private class resetGame implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            gs.startGame();
            message.setText("");
            remove(buttonPanel);
            buttonPanel = setBoard();
            add(buttonPanel);
            revalidate();
            repaint();
        }
    }


    private class move implements ActionListener {
        private int x;
        private int y;

        public move(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (gs.isPlayable()) {
                int[] move = gs.move(x, y);
                buttonGrid[x][y].setText(currentPlayer.getMoveType().toString());
                if (move != null)
                    buttonGrid[move[0]][move[1]].setText(gs.getComputerType().toString());
                else
                    message.setText("Not a valid move");

                checkComplete();
            } else {
                message.setText("Please restart game");
            }
        }

        private void checkComplete() {
            if (gs.isComplete()) {
                message.setText("Game Over. Winner: " + gs.getWinner().toString());
                disableGrid();
            }
        }

        private void disableGrid() {
            for (int i = 0; i < ROWS; i++) {
                for (int j = 0; j < ROWS; j++) {
                    buttonGrid[i][j].setEnabled(false);
                }
            }
        }
    }
}
