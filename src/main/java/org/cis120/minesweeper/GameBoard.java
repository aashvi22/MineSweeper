package org.cis120.minesweeper;

/**
 * CIS 120 HW09 - TicTacToe Demo
 * (c) University of Pennsylvania
 * Created by Bayley Tuch, Sabrina Green, and Nicolas Corona in Fall 2020.
 */


import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

/**
 * This class instantiates a MineSweeper object, which is the model for the game.
 * As the user clicks the game board, the model is updated. Whenever the model
 * is updated, the game board repaints itself and updates its status JLabel to
 * reflect the current state of the model.
 * 
 * This game adheres to a Model-View-Controller design framework. This
 * framework is very effective for turn-based games. We STRONGLY
 * recommend you review these lecture slides, starting at slide 8,
 * for more details on Model-View-Controller:
 * https://www.seas.upenn.edu/~cis120/current/files/slides/lec37.pdf
 * 
 * In a Model-View-Controller framework, GameBoard stores the model as a field
 * and acts as both the controller (with a MouseListener) and the view (with
 * its paintComponent method and the status JLabel).
 */
@SuppressWarnings("serial")
public class GameBoard extends JPanel {

    private MineSweeper ms; // model for the game
    private JLabel status; // current status text
    //int squareSideLength;
    int boxwidth;
    int boxheight;
    int flagcount;

    // Game constants
    public static final int BOARD_WIDTH = 500;
    public static final int BOARD_HEIGHT = 500;


    /**
     * Initializes the game board.
     */
    public GameBoard(JLabel statusInit) {
        ms = new MineSweeper(12,12,18); // initializes model for the game

        flagcount = 30;
        status = statusInit; // initializes the status JLabel
        boxwidth =  BOARD_WIDTH / ms.getCols();
        boxheight = BOARD_WIDTH / ms.getRows();

        this.setBackground(Color.BLACK);

        //squareSideLength = BOARD_HEIGHT/12;

        //creating the grid
        reset();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                Point p = e.getPoint();
                int r = p.y / boxheight;
                int c = p.x / boxwidth;
                if (e.getButton() == MouseEvent.BUTTON3 && flagcount > 0) {

                    if (ms.getSquare(r, c).isFlagged()) {
                        //ms.getSquare(r, c).setValue(ms.getSurroundingMines(r,c));
                        ms.getSquare(r, c).setFlagged(false);
                        //ms.getSquare(r, c).setVisible(false);
                    } else {
                        ms.getSquare(r, c).setFlagged(true);
                        //ms.getSquare(r, c).setValue(100);
                        //ms.getSquare(r, c).setVisible(true);
                        System.out.println(r + " " + c);
                        flagcount--;
                    }

                } else {
                    // updates the model given the coordinates of the mouseclick
                    ms.playTurn(r, c);
                    System.out.println(r + " " + c);
                    System.out.println(r + " " + c);
                    updateStatus(); // updates the status JLabel

                }
                repaint(); // repaints the game board

            }
        });

        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

    }

    /**
     * (Re-)sets the game to its initial state.
     */
    public void reset() {
        int rows = ms.getRows();
        int cols = ms.getCols();
        int nmines = ms.getNumMines();
        flagcount = 30;
        ms = new MineSweeper(rows, cols, nmines);
        ms.printGameState();
        repaint();
        updateStatus();

        requestFocusInWindow();

    }


    /**
     * Updates the JLabel to reflect the current state of the game.
     */
    private void updateStatus() {
        boolean hasWon = ms.checkWinner();
        if (ms.getHasLost()) {
            status.setText("you lose!");
            repaint();
        } else if (hasWon) {
            status.setText("you win!");
            repaint();
        } else {
            status.setText("playing Minesweeper, you got this");
        }
    }


    /**
     * Draws the game board.
     * 
     * There are many ways to draw a game board. This approach
     * will not be sufficient for most games, because it is not
     * modular. All of the logic for drawing the game board is
     * in this method, and it does not take advantage of helper
     * methods. Consider breaking up your paintComponent logic
     * into multiple methods or classes, like Mushroom of Doom.
     */
    @Override
    public void paintComponent(Graphics g) {
        System.out.println("repaint");

        super.paintComponent(g);
        //draw horizontal lines
        int boxheight = BOARD_HEIGHT / ms.getRows();
        for (int r = 0; r < ms.getRows(); r++) {
            g.drawLine(0, boxheight * r, BOARD_WIDTH, boxheight * r);

        }
        //draw vertical lines
        int boxwidth = BOARD_WIDTH / ms.getCols();
        for (int c = 0; c < ms.getCols(); c++) {
            g.drawLine(boxwidth * c, 0, boxwidth * c, BOARD_HEIGHT);
        }
        for (int r = 0; r < ms.getRows(); r++) {
            for (int c = 0; c < ms.getRows(); c++) {
                Square square = ms.getSquare(r,c);
                String squarevalue = ms.getSquare(r,c).getValue() + "";
                if (ms.checkWinner() || ms.getHasLost()) {
                    g.drawImage(getCorrespondingImage(squarevalue),
                            c * boxwidth, r * boxheight,
                            boxwidth, boxheight,null);
                } else if (square.isFlagged()) {
                    g.drawImage(getCorrespondingImage("100"),
                            c * boxwidth, r * boxheight,
                            boxwidth, boxheight,null);
                } else if (square.getVisible()) {
                    g.drawImage(getCorrespondingImage(squarevalue),
                            c * boxwidth, r * boxheight,
                            boxwidth, boxheight,null);
                } else if (!square.getEnabled()) {
                    g.drawImage(getCorrespondingImage("0"), c * boxwidth,
                            r * boxheight, boxwidth,
                            boxheight,null);
                } else {
                    g.drawImage(getCorrespondingImage("facingDown"),
                            c * boxwidth, r * boxheight, boxwidth,
                            boxheight,null);
                }
            }
        }

    }
    public Image getCorrespondingImage(String value) {
        BufferedImage img = null;
        if (value.equals("-1")) {
            value = "200";
        }
        try {
            img = ImageIO.read(
                    new File("/Users/aashvimanakiwala/Downloads/hw09_local_temp/minesweeperImages/"
                            + value + ".png"));
            return img;
        } catch (IOException e) {
            return null;
        }
    }



    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }

    public void changeDifficulty(String difficulty) {
        if (difficulty.equals("easy")) {
            ms = new MineSweeper(12,12,18); // initializes model for the game
            boxwidth = BOARD_WIDTH / ms.getCols();
            boxheight = BOARD_HEIGHT / ms.getRows();
            flagcount = 30;
            System.out.println("pressed easy");
            reset();
        }
        if (difficulty.equals("hard")) {
            ms = new MineSweeper(16,16,30); // initializes model for the game
            boxwidth = BOARD_WIDTH / ms.getCols();
            boxheight = BOARD_HEIGHT / ms.getRows();
            flagcount = 50;
            System.out.println("pressed hard");
            reset();
        }
    }

    public void loadGame() {
        ms.loadFile();
        repaint();
        flagcount = ms.getFlagCount();
        updateStatus();
    }
    public void saveGame(int time) {
        ms.saveBoardToFile(time, flagcount);
        repaint();
    }
    public int getTime() {
        return ms.getTime();
    }
    public int getFlagCount() {
        return flagcount;
    }

}
