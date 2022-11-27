package org.cis120.minesweeper;

import java.io.*;
import java.util.Scanner;
import java.util.TreeSet;


/**
 * CIS 120 HW09 - TicTacToe Demo
 * (c) University of Pennsylvania
 * Created by Bayley Tuch, Sabrina Green, and Nicolas Corona in Fall 2020.
 */

/**
 * This class is a model for MineSweeper.
 *
 * This game adheres to a Model-View-Controller design framework.
 * This framework is very effective for turn-based games. We
 * STRONGLY recommend you review these lecture slides, starting at
 * slide 8, for more details on Model-View-Controller:
 * https://www.seas.upenn.edu/~cis120/current/files/slides/lec36.pdf
 *
 * This model is completely independent of the view and controller.
 * This is in keeping with the concept of modularity! We can play
 * the whole game from start to finish without ever drawing anything
 * on a screen or instantiating a Java Swing object.
 *
 * Run this file to see the main method play a game of TicTacToe,
 * visualized with Strings printed to the console.
 */
public class MineSweeper {

    private Square[][] board;
    private int numRows;
    private int numCols;
    private int numMines;
    private boolean hasLost;
    int time;
    private int flagcount;

    /**
     * Constructor sets up game state.
     */
    public MineSweeper(int nr, int nc, int nmines) {
        numRows = nr;
        numCols = nc;
        numMines = nmines;
        board = new Square[numRows][numCols];
        hasLost = false;
        reset(numMines);

    }

    /**
     * playTurn allows players to play a turn. Returns true if the move is
     * successful and false if a player tries to play in a location that is
     * taken or after the game has ended. If the turn is successful and the game
     * has not ended, the player is changed. If the turn is unsuccessful or the
     * game has ended, the player is not changed.
     *
     * @ param c column to play in
     * @ param r row to play in
     *
     * @return whether the turn was successful
     */

    // this reads an IO file and sets the board
    public void loadFile() {
        System.out.println("loading file");

        File file = new File("GameBoardText");
        Scanner reader = null;
        try {
            reader = new Scanner(file);
            int i = 0;
            while (reader.hasNextLine()) {
                String str = reader.nextLine();
                if (i == 0) {
                    String [] strparts = str.split(" ");
                    time = Integer.parseInt(strparts[1]);
                    flagcount = Integer.parseInt(strparts[2]);
                    if (strparts[0].equals("easy")) {
                        numCols = 12;
                        numRows = 12;
                        numMines = 18;
                        board = new Square[numRows][numCols];
                    } else if (strparts[0].equals("hard")) {
                        numCols = 16;
                        numRows = 16;
                        numMines = 30;
                        board = new Square[numRows][numCols];
                    }
                } else {
                    String[] squares = str.split(" ");
                    for (int j = 0; j < numCols; j++) {
                        try {
                            String [] parts = squares[j].split(",");
                            board[i - 1][j] = new Square(Integer.parseInt(parts[0]));
                            if (parts.length > 1 && parts[1].equals("E")) {
                                board[i - 1][j].setEnabled(true);
                            } else {
                                board[i - 1][j].setEnabled(false);
                            }
                            if (parts.length > 2 && parts[2].equals("V")) {
                                board[i - 1][j].setVisible(true);
                            } else {
                                board[i - 1][j].setVisible(false);
                            }
                        } catch (ArrayIndexOutOfBoundsException e) {
                            System.out.println(e);
                        }
                    }
                    System.out.println(str);
                }
                i++;
            }
            reader.close();
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        }


        printGameState();

    }

    //this is used for tests
    public void loadFilePath(String filepath) {
        File file = new File(filepath);
        Scanner reader = null;
        try {
            reader = new Scanner(file);
            int i = 0;
            while (reader.hasNextLine()) {
                String str = reader.nextLine();
                int length = 0;
                if (str.equals("easy")) {
                    numCols = 12;
                    numRows = 12;
                    numMines = 18;
                    board = new Square[numRows][numCols];
                } else if (str.equals("hard")) {
                    numCols = 16;
                    numRows = 16;
                    numMines = 30;
                    board = new Square[numRows][numCols];
                } else {
                    String[] squares = str.split(" ");

                    for (int j = 0; j < numCols; j++) {
                        try {
                            String [] parts = squares[j].split(",");
                            board[i][j] = new Square(Integer.parseInt(parts[0]));
                            if (parts.length > 1 && parts[1].equals("E")) {
                                board[i][j].setEnabled(true);
                            } else {
                                board[i][j].setEnabled(false);
                            }
                            if (parts.length > 2 && parts[2].equals("V")) {
                                board[i][j].setVisible(true);
                            } else {
                                board[i][j].setVisible(false);
                            }
                        } catch (ArrayIndexOutOfBoundsException e) {
                            System.out.println("loadfilepath: " + e);
                        }
                    }
                    System.out.println(str);
                    i++;
                }

            }
            reader.close();
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        }
    }

    public void saveBoardToFile(int time, int flagcount) {
        String difficulty = "";
        if (numCols == 12) {
            difficulty = "easy";
        }
        if (numCols == 16) {
            difficulty = "hard";
        }
        File file = new File("GameBoardText");
        String output = "";
        output += difficulty + " " + time + " " + flagcount + "\n";
        try {

            for (int r = 0; r < numRows; r++) {
                for (int c = 0; c < numCols; c++) {
                    Square s = board[r][c];
                    output += s.getValue() + ",";
                    if (s.getEnabled()) {
                        output += "E";
                    }
                    output += ",";
                    if (s.getVisible()) {
                        output += "V";
                    }
                    output += " ";
                }
                output += "\n";
            }
            BufferedWriter outputStream =
                    new BufferedWriter(new FileWriter(file.getAbsolutePath()));
            outputStream.write(output);
            outputStream.close();
        } catch (IOException exc) {
            System.out.println(exc);
        }
    }
    public void saveBoardToFile() {
        String difficulty = "";
        if (numCols == 12) {
            difficulty = "easy";
        }
        if (numCols == 16) {
            difficulty = "hard";
        }
        File file = new File("GameBoardText");
        String output = "";
        output += difficulty + "\n";
        try {

            for (int r = 0; r < numRows; r++) {
                for (int c = 0; c < numCols; c++) {
                    Square s = board[r][c];
                    output += s.getValue() + ",";
                    if (s.getEnabled()) {
                        output += "E";
                    }
                    output += ",";
                    if (s.getVisible()) {
                        output += "V";
                    }
                    output += " ";
                }
                output += "\n";
            }
            BufferedWriter outputStream =
                    new BufferedWriter(new FileWriter(file.getAbsolutePath()));
            outputStream.write(output);
            outputStream.close();
        } catch (IOException exc) {
            System.out.println(exc);
        }
    }

    public void playTurn(int r, int c) {
        if (board[r][c].getValue() == -1) {
            hasLost = true;
            board[r][c].setValue(200);
            board[r][c].setVisible(true);
        } else {
            recursion(r, c);
        }
    }


    public void recursion(int r, int c) {
        if (!board[r][c].getVisible()) {
            board[r][c].setVisible(true);
            board[r][c].setEnabled(false);
        }
        if (board[r][c].isFlagged()) {
            board[r][c].setFlagged(false);
        }
        //if there's a mine surrounding it, make the number show and end recursion
        //if there's not mine, keep recursing

        if (board[r][c].getValue() > 0) {
            board[r][c].setVisible(true);
        } else {
            for (int r2 = r - 1; r2 <= r + 1; r2++) {
                for (int c2 = c - 1; c2 <= c + 1; c2++) {
                    try {
                        if (!board[r2][c2].getVisible()) {
                            recursion(r2,c2);
                        }
                    } catch (IndexOutOfBoundsException e) { }
                }
            }
        }
    }

    /**
     * checkWinner checks whether the game has reached a win condition.
     * checkWinner only looks for horizontal wins.
     *
     * @return 0 if nobody has won yet, 1 if player 1 has won, and 2 if player 2
     *         has won, 3 if the game hits stalemate
     */

    public boolean checkWinner() {
        //win if all squares except the mines are disabled
        int disabledCount = 0;
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                if (!board[r][c].getEnabled()) {
                    disabledCount++;
                }
            }
        }
        return (numCols * numRows - disabledCount == numMines);
    }
    public boolean getHasLost() {
        return hasLost;
    }
    public int getTime() {
        return time;
    }


    /**
     * printGameState prints the current game state
     * for debugging.
     */
    public void printGameState() {
        int nmines = 0;
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                if (board[r][c].getValue() == -1) {
                    nmines++;
                }
                if (!board[r][c].getEnabled()) {
                    System.out.print(board[r][c].getValue() + "* ");
                } else {
                    System.out.print(board[r][c].getValue() + " ");
                }
            }
            System.out.println("");
        }
        System.out.println("total mines: " + nmines);
    }

    /**
     * reset (re-)sets the game state to start a new game.
     */
    public void reset(int numMines) {
        //first, I am initializing the board with values 0 in each square
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                board[r][c] = new Square(0);
            }
        }

        TreeSet usedPositions = new TreeSet();
        //here, I am assigning numMines
        //random squares in the grid to hold the mines
        //tracking the positions of the mines in a TreeSet to
        //prevent duplicate squares holding mines
        while (usedPositions.size() < numMines) {
            int randomRow = (int)(Math.random() * numRows);
            int randomCol = (int)(Math.random() * numCols);
            String position = randomRow + "," + randomCol;
            usedPositions.add(position);
            board[randomRow][randomCol].setValue(-1);
        }
        //here, I am setting the values of the nonmine squares
        //the value of a square is the number of mines it is neighboring (0-8)
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                Square currentSquare = board[r][c];
                if (currentSquare.getValue() != -1) {
                    currentSquare.setValue(getSurroundingMines(r,c));
                }

            }
        }


    }
    public boolean inBounds(int r, int c) {
        return (r < numRows && r >= 0) && (c < numCols && c >= 0);
    }

    public int getSurroundingMines(int r, int c) {
        int surroundingMines = 0;
        for (int r2 = r - 1; r2 <= r + 1; r2++) {
            for (int c2 = c - 1; c2 <= c + 1; c2++) {

                if (inBounds(r2,c2)) {
                    if (!(r2 == r && c2 == c) && board[r2][c2].getValue() == -1) {
                        surroundingMines++;
                    }
                }

            }
        }

        return surroundingMines;
    }



    /**
     * getCurrentPlayer is a getter for the player
     * whose turn it is in the game.
     *
     * @return true if it's Player 1's turn,
     *         false if it's Player 2's turn.
     */
//    public boolean getCurrentPlayer() {
//        return player1;
//    }

    /**
     * getCell is a getter for the contents of the cell specified by the method
     * arguments.
     *
     * @param c column to retrieve
     * @param r row to retrieve
     * @return an integer denoting the contents of the corresponding cell on the
     *         game board. 0 = empty, 1 = Player 1, 2 = Player 2
     */
    public Square getCell(int r, int c) {
        return board[r][c];
    }

    public int getRows() {
        return numRows;
    }

    public int getCols() {
        return numCols;
    }
    public int getNumMines() {
        return numMines;
    }
    public int getFlagCount() {
        return flagcount;
    }

    /**
     * This main method illustrates how the model is completely independent of
     * the view and controller. We can play the game from start to finish
     * without ever creating a Java Swing object.
     *
     * This is modularity in action, and modularity is the bedrock of the
     * Model-View-Controller design framework.
     *
     * Run this file to see the output of this method in your console.
     */

    public Square getSquare(int r, int c) {
        return board[r][c];
    }


    public static void main(String[] args) {
        MineSweeper t = new MineSweeper(12,12,10);

        t.playTurn(1, 1);
        t.printGameState();

        t.playTurn(0, 0);
        t.printGameState();

        t.playTurn(0, 2);
        t.printGameState();

        t.playTurn(2, 0);
        t.printGameState();

        t.playTurn(1, 0);
        t.printGameState();

        t.playTurn(1, 2);
        t.printGameState();

        t.playTurn(0, 1);
        t.printGameState();

        t.playTurn(2, 2);
        t.printGameState();

        t.playTurn(2, 1);
        t.printGameState();
        System.out.println();
        System.out.println();
        //System.out.println("Winner is: " + t.checkWinner());
    }
}
