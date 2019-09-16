//------------------------------------------------------------------//
// Board.java                                                       //
//                                                                  //
// Class used to represent a 2048 game board                        //
//                                                                  //
// Author:  W16-CSE8B-TA group                                      //
// Date:    1/17/16                                                 //
//------------------------------------------------------------------//

/*
 * Name: Christina Carlson
 * Login: cs8bwaal
 * Date: February 4, 2016
 * File: Board.java
 * Sources of help: docs.oracle.com, piazza.com
 *
 * This program creates the grid on which 2048 is played. It can
 * save the grid to a file or rotate the grid. It can also add
 * a tile to a random, empty location on the grid.
 */

/**
 * Sample Board
 * <p/>
 * 0   1   2   3
 * 0   -   -   -   -
 * 1   -   -   -   -
 * 2   -   -   -   -
 * 3   -   -   -   -
 * <p/>
 * The sample board shows the index values for the columns and rows
 * Remember that you access a 2D array by first specifying the row
 * and then the column: grid[row][column]
 */

import java.util.*;
import java.io.*;

public class Board {
    public final int NUM_START_TILES = 2;
    public final int TWO_PROBABILITY = 90;
    public final int GRID_SIZE;


    private final Random random;
    private int[][] grid;
    private int score;

    // Constructs a fresh board with random tiles
    public Board(int boardSize, Random random) {

       // Initialize variables
        this.random = random;
        GRID_SIZE = boardSize;
        this.grid = new int[GRID_SIZE][GRID_SIZE];
        this.score = 0;
        int i = 0;
        // Add a given number of random tiles
        while(i < NUM_START_TILES){
            this.addRandomTile();
            i++;
        }
    }

    // Construct a board based off of an input file
    public Board(String inputBoard, Random random) throws IOException {
        // Initialize variables
        File inFile = new File(inputBoard);
        Scanner input = new Scanner(inFile);
        // Scan file for board values
        this.random = random;
        GRID_SIZE = input.nextInt();
        this.score = input.nextInt();
        this.grid = new int[GRID_SIZE][GRID_SIZE];
        for(int i = 0; i < GRID_SIZE; i++){
            for(int j = 0; j < GRID_SIZE; j++){
                this.grid[i][j] = input.nextInt();
            }
        }
        input.close();
    }

    // Name: saveBoard
    // Purpose: saves board to file
    // Paramaters: String outputBoard, file where board is saved
    // Return: void
    public void saveBoard(String outputBoard) throws IOException {
        // Create PrintWriter
        File outFile = new File(outputBoard);
        PrintWriter output = new PrintWriter(outFile);
        // Print board to file
        output.println(GRID_SIZE);
        output.println(this.getScore());
        for(int i = 0; i < GRID_SIZE; i++){
            for(int j = 0; j < GRID_SIZE; j++){
                output.print(this.grid[i][j] + " ");
            }
            output.println();
        }
        output.close();
    }

    // Name: addRandomTile
    // Purpose: adds a tile of value 2 or 4 to a random empty
    // space on the board
    // Paramaters: none
    // Return: void
    public void addRandomTile() {
        // Count empty tiles
        int count = 0;
        for(int i = 0; i < GRID_SIZE; i++){
            for(int j = 0; j < GRID_SIZE; j++){
                if(this.grid[i][j] == 0){
                    count += 1;
                }
            }
        }
        // Don't add any new tiles if there are no empty tiles
        if(count == 0){
            return;
        }
        int location = this.random.nextInt(count) + 1;
        int value = this.random.nextInt(100);
        // Search through empty tiles
        int empty = 0;
        for(int m = 0; m < GRID_SIZE; m++){
            for(int n = 0; n < GRID_SIZE; n++){
                if(this.grid[m][n] == 0){
                    empty += 1;
                // Add a tile at a random location
                // Set tile to 2 with 90% probability
                // Set tile to 4 with 10% probability
                    if(empty == location){
                        if(value < TWO_PROBABILITY){
                            this.grid[m][n] = 2;
                        } else {
                            this.grid[m][n] = 4;
                        }
                    }
                }
            }
        }
    }

    // Name: rotate
    // Purpose: Rotates the board by 90 degrees clockwise or
    // 90 degrees counter-clockwise.
    // Paramaters: boolean rotateClockwise, rotates the board clockwise
    // if true and counterclockwise if otherwise

 // Return: void
    public void rotate(boolean rotateClockwise) {
        // Create a temporary 2D array
        int[][] temp = new int[GRID_SIZE][GRID_SIZE];
        // Check if rotateClockwise is true
        // Save clockwise grid to temp
        if(rotateClockwise == true){
            for(int i = 0; i < GRID_SIZE; i++){
                for(int j = 0; j < GRID_SIZE; j++){
                    temp[i][j] = this.grid[GRID_SIZE - j - 1][i];
                }
            }
        // if rotateClockwise is not true
        // Save counterclockwise grid to temp
        } else {
            for(int a = 0; a < GRID_SIZE; a++){
                for(int b = 0; b < GRID_SIZE; b++){
                    temp[a][b] = this.grid[b][GRID_SIZE - a - 1];
                }
            }
        }
        // Copy temp to the grid that is being rotated
        for(int m = 0; m < GRID_SIZE; m++){
            for(int n = 0; n < GRID_SIZE; n++){
                this.grid[m][n] = temp[m][n];
            }
        }
    }

    //Complete this method ONLY if you want to attempt at getting the extra credit
    //Returns true if the file to be read is in the correct format, else return
    //false
    public static boolean isInputFileCorrectFormat(String inputFile) {
        //The try and catch block are used to handle any exceptions
        //Do not worry about the details, just write all your conditions inside the
        //try block
        try {
            //write your code to check for all conditions and return true if it satisfies
            //all conditions else return false
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Name: Move
    // Purpose: moves tiles in the specified direction
    // Matching tiles are merged and the value of the
    // new tile is added to the score
    // Paramaters: Direction direction, the direction of the move
    // Return: boolean, returns true if the move is successful
    // and false if not
    public boolean move(Direction direction) {
        //Checks if a move is possible
        if(this.canMove(direction) == true){
            //Completes the move based on which direction was specified
            if(direction == Direction.RIGHT){
                this.moveRight();
            }
            if(direction == Direction.LEFT){
                this.moveLeft();
            }
            if(direction == Direction.UP){
                this.moveUp();
            }
            if(direction == Direction.DOWN){
                this.moveDown();
            }
        return true;
        }
        return false;
    }

    //Name: moveRight
    //Purpose: moves tiles right, helper method for move
    //Paramters: none
    //Return: void
    private void moveRight() {
        //Cycles through each row from right to left
        //Finds non-zero numbers
        //Moves all non-zero numbers to the rightmost
        //edge of the board
        for(int i = 0; i < GRID_SIZE; i++){
            for(int j = GRID_SIZE - 1; j > 0; j--){
                if(this.grid[i][j] == 0 && this.grid[i][j-1] > 0){
                    this.grid[i][j] = this.grid[i][j-1];
                    this.grid[i][j-1] = 0;
                    j = GRID_SIZE;
                }
            }
        }
        //Cycles through the board from right to left
        for (int x = 0; x < GRID_SIZE; x++){
            for(int y = GRID_SIZE - 1; y > 0; y--){
                if(this.grid[x][y] == this.grid[x][y-1]){
                    //Creates a new tile with the value
                    //of the merged tiles
                  this.grid[x][y] += this.grid[x][y-1];
                    this.score += this.grid[x][y];
                    //Moves all tiles to the left of the
                    //merged tile over by one
                    for(int z = y - 1; z > 0; z--){
                        this.grid[x][z] = this.grid[x][z-1];
                    }
                    //Sets leftmost tile to 0
                    this.grid[x][0] = 0;
                }
            }
        }
    }

    //Name: moveLeft
    //Purpose: moves tiles left, helper method for move
    //Paramters: none
    //Return: void
    private void moveLeft(){
        //Rotates board 180 degrees
        //Implements move right
        //Returns board to original orientation
        this.rotate(true);
        this.rotate(true);
        this.moveRight();
        this.rotate(true);
        this.rotate(true);
    }

    //Name: moveUp
    //Purpose: moves tiles up, helper method for move
    //Paramters: none
    //Return: void
    private void moveUp(){
        //Rotates board clockwise
        //Implements move right
        //Returns board to original orientation
        this.rotate(true);
        this.moveRight();
        this.rotate(false);
    }

    //Name: moveDown
    //Purpose: moves tiles down, helper method for move
    //Paramters: none
    //Return: void
    private void moveDown(){
        //Rotates board counterclockwise
        //Implements move right
        //Returns board to original orientation
        this.rotate(false);
        this.moveRight();
        this.rotate(true);
    }

    //Name: isGameOver
    //Purpose: checks for a game over
    //Paramaters: none
    //Return: true if there is a game over, false if not
    public boolean isGameOver() {
        if(!this.canMove(Direction.RIGHT)){
            if(!this.canMove(Direction.LEFT)){
                if(!this.canMove(Direction.UP)){
                    if(!this.canMove(Direction.DOWN)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //Name: canMove
    //Purpose: checks if the tiles can move in a given direction
    //Paramaters: Direction direction, the direction of the move
    //Return: boolean, true if the move is possible, false if not
    public boolean canMove(Direction direction) {
        //Checks if a move is possible for all four directions
        if(direction == Direction.RIGHT){
            if(this.canMoveRight() == true){
                return true;
            }
        }
        if(direction == Direction.LEFT){
            if(this.canMoveLeft() == true){
                return true;
            }
        }
        if(direction == Direction.UP){
            if(this.canMoveUp() == true){
                return true;
            }
        }
        if(direction == Direction.DOWN){
            if(this.canMoveDown() == true){
                return true;
           }
        }
        return false;
    }

    //Name: canMoveRight
    //Purpose: checks if the tiles can move right, helper method for canMove
    //Parameters: none
    //Return: boolean, true if the tiles can move right, false if not
    private boolean canMoveRight() {
        //match is used to identify matching tiles
        //match is initialized at -1
        int match = -1;
        //Cycles through all tiles to find tiles that can merge
        for(int i = 0; i < GRID_SIZE; i++){
            for(int j = 0; j < GRID_SIZE; j++){
                if(this.grid[i][j] > 0 && j < GRID_SIZE - 1){
                    match = this.grid[i][j];
                    j++;
                    if(this.grid[i][j] == match){
                        return true;
                    } else {
                        j--;
                    }
                }
            }
            //match is reset
            match = -1;
        }
        //Cycles through all tiles to check
        //if there are empty tiles to the right
        //of non-empty tiles
        for(int x = 0; x < GRID_SIZE; x++){
            for(int y = 0; y < GRID_SIZE - 1; y++){
                if(this.grid[x][y] > 0 && this.grid[x][y+1] == 0){
                    return true;
                }
            }
        }
        return false;
    }

    //Name: canMoveLeft
    //Purpose: checks if the tiles can move left, helper method for canMove
    //Parameters: none
    //Return: boolean, true if the tiles can move left, false if not
    private boolean canMoveLeft(){
        //Rotates board 180 degrees
        //Checks if tiles can move right
        //Returns board to original orientation
        this.rotate(true);
        this.rotate(true);
        if(this.canMoveRight()){
            this.rotate(true);
            this.rotate(true);
            return true;
        } else {
            this.rotate(true);
            this.rotate(true);
            return false;
        }
    }

    //Name: canMoveUp
    //Purpose: checks if the tiles can move up, helper method for canMove
    //Parameters: none
    //Return: boolean, true if the tiles can move up, false if not
    private boolean canMoveUp(){
        //Rotates board clockwise
        //Checks if tiles can move right
        //Returns board to original orientation
        this.rotate(true);
        if(this.canMoveRight()){
            this.rotate(false);
            return true;
        } else {
            this.rotate(false);
            return false;
        }
    }

    //Name: canMoveDown
    //Purpose: checks if the tiles can move down, helper method for canMove
    //Parameters: none
    //Return: boolean, true if the tiles can move down, false if not
    private boolean canMoveDown(){
        //Rotates board counterclockwise
        //Checks if tiles can move right
        //Returns board to original orientation
        this.rotate(false);
        if(this.canMoveRight()){
            this.rotate(true);
            return true;
        } else {
            this.rotate(true);
            return false;
     }
    }

    //Name: undo
    //Purpose: reverts board to layout before most recent command
    //Paramaters: none
    //Return: none
    public void undo() throws IOException {
        //Creates save file for previous move
        Board previousBoard = new Board("previous.out", new Random());
        //Replaces current board with previous board
        for(int i = 0; i < GRID_SIZE; i++){
            for(int j = 0; j < GRID_SIZE; j++){
                this.grid[i][j] = previousBoard.grid[i][j];
            }
        }
    }

    // Return the reference to the 2048 Grid
    public int[][] getGrid() {
        return grid;
    }

    // Return the score
    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        StringBuilder outputString = new StringBuilder();
        outputString.append(String.format("Score: %d\n", score));
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int column = 0; column < GRID_SIZE; column++)
                outputString.append(grid[row][column] == 0 ? "    -" :
                        String.format("%5d", grid[row][column]));

            outputString.append("\n");
        }
        return outputString.toString();
    }
}
