package ticTacUI;

import javax.swing.*;
import java.awt.*;

/**
 * Created by calgarymichael on 4/21/17.
 */
public class TicTacToeFrame extends JFrame {
    public TicTacToeFrame() {
        this.setSize(250, 250);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(false);

        TicTacToePanel pane = new TicTacToePanel();
        setContentPane(pane);
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                TicTacToeFrame frame = new TicTacToeFrame();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
