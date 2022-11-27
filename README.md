=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: _______
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after going
  submitting your proposal.

  1. 2D Arrays
  I'm using a 2D array of Square objects (see the description of Square below).
  Each object in the array corresponds to a square in the
  Minesweeper grid. That is, each element in the array corresponds to an image drawn at the position of the array scaled to the size of the board.
  I chose to use a 2D array it is a convenient way to keep track of the state of each square based on its location.
  In the proposal, I said I would use a 2D JToggleButton array. However, my TAs advised against this because it would be difficult to test.

  2. File I/O
  I'm using File I/O to save and load a game. This allows them to quit in the middle of a game and resume it later.
  When the user clicks on the save button, the current 2D array
  representing the board gets printed onto a particular text file (GameBoardText).
  This is formatted such that every row on the board is a row in the file, every square is separated by a space,
  and the different aspects of the state of each square (value, whether it's enabled, and whether it's visible) is delimited with a comma.
  Further, the difficulty of the game is also recorded.
  When the user clicks on the load button, it loads the game saved on GameBoardText.txt. It creates a new instance of MineSweeper and sets
  the state of each Square according to the information from the file. It also reads in the difficulty and sets the board size and number of mines accordingly.
  File I/O was also helpful in testing the game, since I could simulate different scenarios by loading certain board configurations.
  For testing, I created a new loadFilePath method that was similar to loadFile but took a String filepath.
  Using File I/O was appropriate for this function because it is difficult to store the game state in GameBoard or MineSweeper, as a new
  instance of the GameBoard and MineSweeper is created at the start of every game.

  3. Recursion
  This is used every time a player clicks on the board. The function disables/makes visible the clicked square and keeps disabling/making visible its neighboring squares
  until it reaches a square whose value is greater than 0 (in other words, it is neighboring a mine). Recursion is appropriate because it
  applies the same disabling/visibility function to every square until it reaches the base case of the square having a nonzero value.
  The reason squares touched by the recursion are disabled and made visible is because they can not be clickable and their values must be shown
  (as in the classic Minesweeper)


  4. JUnit Testable Component
  This was used to test certain functionality, such as whether the status is correct (win/lose/still playing) and whether the recursion is done correctly.
  This was useful because it is difficult to test whether functionality like recursion is done properly when you can not directly see the values of each square in the GUI.
  To do this, I stored certain game states in files using the save function. For example, "testwinnerText.txt" is a game one square away from winning.
  I then internally clicked on certain squares using "playTurn(int r, int c)" and checked whether the game status update properly.
  To check whether the board looked how it was supposed to, I created a function called checkEquals, which takes two filepath strings,
  reads the corresponding files, and checks whether they are identical.




=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.

Square class: Each Square corresponds to a square in the Minesweeper grid
A Square holds information about:
    - the value (the number of surrounding mines) of a square in the grid. value = -1 for a mine
    - Whether the square is enabled. If it is enabled, then it can be clicked on in the game to be disabled
    knowing whether a square is enabled is useful in checking whether the game is won because a player has won when the number of disabled squares equals the number of mines
    - Whether the square is visible. If so, then the value of the square can be displayed on the grid.
    enabled and visible were useful in the recursion
    - Whether the square is flagged

MineSweeper:
This is the model component of the model, view, controller system. It acts to recurse on the board after every click and change the states of
the Square corresponding to the click. It also checks whether the player has won/lost.
Fields:
    - private Square[][] board - this is the 2D array of Squares corresponding to the grid. Its function is described above
    - private int numRows - the number of rows in the board (easy = 12, hard = 16)
    - private int numCols - the number of columns in the board. For this game, numCols = numRows, but I made two variables for the flexibility of creating rectangular boards
    - private int numMines - the number of mines in the game (easy = 18, hard = 30). used to check when the game is won
    - private boolean hasLost - true when the player has lost (when the player clicks on a mine, or a square whose value is -1)
    All of these values are set in the constructor, which takes numRows, numCols, numMines
    - int time - stores the time from a loaded file
    - private int flagcount - stores the flag count from a loaded file

Important Methods
    - loadFile() - takes text on GameBoardText and sets the board [][] accordingly
    - loadFilePath (String filepath) - similar to loadFile() but for testing and can give in any file
    - saveBoardToFile() - saves the game state in a file
    - playTurn (int r, int c) - if the player clicks on a mine, they automatically lose. otherwise, the recursion is called on that square
    - recursion(int r, int c) - expands Squares based on click
    - checkWinner() - checks whether the number of disabled squares equals the number of mines. if so, returns true
    - printGameState() - prints formatted board[][] for testing
    - reset(int numMines) - first initializes board with each square of value 0. Then randomly assigns mines to Squares numMine Squares.
    Then goes through the remaining nonmine Squares and calculates the neighboring mines.
    - getSurroundingMines (int r, int c) - a helper function for reset()

GameBoard:
Updates and displays information on the GUI based on the state of the model and the user's actions
Fields:
    - private MineSweeper ms -  model for the game
    - private JLabel status - current status text
    - int boxwidth - the width of a square on the grid. Equal to BOARD_WIDTH/numCols
    - int boxheight - the height of a square on the grid. Equal to BOARD_HEIGHT/numRows
    - int flagcount - the number of flags a user is allowed to have. the count is dependent on difficulty, and decreases every time it is used

Important methods:
    - constructor - here, the field variables are initialized and GameBoard is given an actionListener.
    This listener calculates the row and column of a square corresponding to a click and sends this info to MineSweeper
    - reset() - resets game by creating new board
    - updateStatus() - reflects current state of game (win/lose/still playing)
    - paintComponent(Graphics g) - paints grid and images corresponding to squares
    - getCorrespondingImage(String value) - helper for repaint(), returns image corresponding to a Square's value.

RunMineSweeper
    - save - button that saves current state of game
    - load - button that loads previous game
    - easy - changes board to 12x12 and mines to 18
    - hard - changes board to 16x16 and mines to 30
    - timeText - a TextArea for the time. Uses TimerTask to increment the timer concurrently with the game
    - flags - displays how many flags are left. Since I can't update information realtime from GameBoard (where flags is modified),
    I am updating it every second along with the timer.

- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?

- Before I manually displayed lines and images for the grid, I used a gridLayout of JToggleButtons to display each Square.
Each button contained a map (using putClientProperty() and getClientProperty()) with the key "square" and value as the corresponding square.
Further, each button had its own listener that would make changes to the model and its corresponding square when it was clicked.
In the repaint method, I would redraw the gridlayout and change the background color of the buttons. However, this was an issue because the buttons
would change colors inconsistently or not at all. At one point, the debugging tool claimed that a button was greyed out (disabled) but the change did not reflect on the GUI
Eventually, I switched to the current method of displaying the grid, which is much smoother.

- while testing, I realized the status was not correctly updating when a game was loaded. I eventually realized the mistake was very obvious -
when I was loading the file, I only stored information when the square was enabled or visible, not when it was disabled or invisible.

- When implementing File I/O, my save and load functions used a FileChooser to give the user the flexibility to save or load a file to/from any location.
FileChooser worked for the save functionality, but it was infeasible to use for loading a file. Therefore, I switched to having a constant file and internally saving/loading



- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?

There aren't that many cases where a square is enabled but not visible. If I were to redo this, I would probably use only one variable because
having both is redundant.

========================
=: External Resources :=
========================

- Cite any external resources (images, tutorials, etc.) that you may have used 
  while implementing your game.

  The images of numbers, mines, and flags are from this Github page: https://github.com/pardahlman/minesweeper/tree/master/Images
