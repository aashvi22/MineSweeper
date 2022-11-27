package org.cis120.minesweeper;

/**
 * CIS 120 HW09 - TicTacToe Demo
 * (c) University of Pennsylvania
 * Created by Bayley Tuch, Sabrina Green, and Nicolas Corona in Fall 2020.
 */


import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;

/**
 * This class sets up the top-level frame and widgets for the GUI.
 *
 * This game adheres to a Model-View-Controller design framework. This
 * framework is very effective for turn-based games. We STRONGLY
 * recommend you review these lecture slides, starting at slide 8,
 * for more details on Model-View-Controller:
 * https://www.seas.upenn.edu/~cis120/current/files/slides/lec37.pdf
 *
 * In a Model-View-Controller framework, Game initializes the view,
 * implements a bit of controller functionality through the reset
 * button, and then instantiates a GameBoard. The GameBoard will
 * handle the rest of the game's view and controller functionality, and
 * it will instantiate a TicTacToe object to serve as the game's model.
 */
public class RunMineSweeper implements Runnable {
    int time = 0;
    JTextArea timeText = new JTextArea();
    Timer timer;
    JButton flags;
    GameBoard board;

    public void run() {
        // NOTE: the 'final' keyword denotes immutability even for local variables.

        // Top-level frame in which game components live
        final JFrame frame = new JFrame("Minesweeper");
        frame.setLocation(300, 300);
        frame.setBackground(Color.BLACK);
        //frame.setPreferredSize(new Dimension(500,600));
        //frame.setResizable(false);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("playing Minesweeper, you got this");
        status_panel.add(status);

        // Game board
        board = new GameBoard(status);
        frame.add(board, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        // Note here that when we add an action listener to the reset button, we
        // define it as an anonymous inner class that is an instance of
        // ActionListener with its actionPerformed() method overridden. When the
        // button is pressed, actionPerformed() will be called.
        final JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                time = 0;
                timeText.setText(time + "");
                board.reset();
            }
        });
        control_panel.add(reset);

        JButton easy = new JButton("easy");
        easy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                time = 0;
                timeText.setText(time + "");
                board.changeDifficulty("easy");
            }
        });
        JButton hard = new JButton("hard");
        hard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                time = 0;
                timeText.setText(time + "");
                board.changeDifficulty("hard");
            }
        });
        JButton save = new JButton("save");
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                board.saveGame(time);
            }
        });
        JButton load = new JButton("load");
        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                board.loadGame();
                time = board.getTime();
                timeText.setText(time + "");
            }
        });
        JButton instructions = new JButton("instructions");
        instructions.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = "-Click on all the squares that " +
                    "don't have mines to win the game\n" +
                    "-If you click on a mine, you lose :(\n" +
                    "-The numbers indicate how many " +
                    "mines are neighboring that particular square";
                JOptionPane.showMessageDialog(null, message,
                        "How to play", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        flags = new JButton();
        flags.setEnabled(false);
        ImageIcon flag = new ImageIcon("minesweeperImages/100.png");
        flag = new ImageIcon(flag.getImage().getScaledInstance(20,20,Image.SCALE_SMOOTH));
        flags.setOpaque(true);
        flags.setIcon(flag);
        flags.setPreferredSize(new Dimension(80,20));
        flags.setText(board.getFlagCount() + "");

        timer = new Timer();
        timer.schedule(new UpdateTimer(), 0, 1000);
        timeText.setPreferredSize(new Dimension(40,20));
        timeText.setEditable(false);
        timeText.setOpaque(true);
        timeText.setBackground(Color.RED);

        control_panel.add(timeText);
        control_panel.add(flags);
        control_panel.add(save);
        control_panel.add(load);
        control_panel.add(easy);
        control_panel.add(hard);
        control_panel.add(instructions);
        control_panel.setBackground(Color.BLACK);


        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start the game
        board.reset();
    }
    class UpdateTimer extends TimerTask {
        @Override
        public void run() {
            time++;
            flags.setText(board.getFlagCount() + "");
            timeText.setText(time + "");
        }
    }
}