/* Name: Christina Carlson
 * Login: cs8bwaal
 * Date: March 3, 2016
 * File: Gui2048.java
 * Sources of help: docs.oracle.com, piazza.com, stackoverflow.com
 *
 * This program generates a user interface to play the game 2048.
 * The board updates with each key input, and the board displays
 * a game over if there are no possible moves. The user may
 * also save the board.

/** Gui2048.java */
/** PSA8 Release */

import javafx.application.*;
import javafx.scene.control.*;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.event.*;
import javafx.scene.input.*;
import javafx.scene.text.*;
import javafx.geometry.*;
import java.util.*;
import java.io.*;

public class Gui2048 extends Application
{
    private String outputBoard; // The filename for where to save the Board
    private Board board; // The 2048 Game Board

    private static final int TILE_WIDTH = 106;

    private static final int TEXT_SIZE_LOW = 55; // Low value tiles (2,4,8,etc)
    private static final int TEXT_SIZE_MID = 45; // Mid value tiles 
                                                 //(128, 256, 512)
    private static final int TEXT_SIZE_HIGH = 35; // High value tiles 
                                                  //(1024, 2048, Higher)

    // Fill colors for each of the Tile values
    private static final Color COLOR_EMPTY = Color.rgb(241, 232, 241);
    private static final Color COLOR_2 = Color.rgb(236, 213, 236);
    private static final Color COLOR_4 = Color.rgb(233, 193, 233);
    private static final Color COLOR_8 = Color.rgb(242, 177, 242);
    private static final Color COLOR_16 = Color.rgb(245, 149, 245);
    private static final Color COLOR_32 = Color.rgb(246, 124, 246);
    private static final Color COLOR_64 = Color.rgb(246, 101, 246);
    private static final Color COLOR_128 = Color.rgb(237, 92, 237);
    private static final Color COLOR_256 = Color.rgb(237, 78, 237);
    private static final Color COLOR_512 = Color.rgb(237, 63, 237);
    private static final Color COLOR_1024 = Color.rgb(217, 50, 217);
    private static final Color COLOR_2048 = Color.rgb(210, 45, 210);
    private static final Color COLOR_OTHER = Color.BLACK;
    private static final Color COLOR_GAME_OVER = Color.rgb(238, 218, 238, 0.73);

    private static final Color COLOR_VALUE_LIGHT = Color.rgb(249, 242, 249); 
                        // For tiles >= 8

    private static final Color COLOR_VALUE_DARK = Color.rgb(112, 101, 119); 
                       // For tiles < 8

    private GridPane pane;

    /** Add your own Instance Variables here */
    private int score;
    private Text scoreText = new Text();
    private Rectangle rect = new Rectangle();
    private Text tileVal = new Text();
    private ArrayList<Text> textList = new ArrayList<Text>();
    private ArrayList<Rectangle> rectList = new ArrayList<Rectangle>();
    private int numGameOver;

    @Override
    // Name: start
    // Purpose: activate the player window
    // Paramaters: Stage primaryStage, the active window
    // Return: void
    public void start(Stage primaryStage)
    {
        // Process Arguments and Initialize the Game Board
        processArgs(getParameters().getRaw().toArray(new String[0]));

        // Create the pane that will hold all of the visual objects
        pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
        pane.setStyle("-fx-background-color: rgb(185, 150, 185)");
        // Set the spacing between the Tiles
        pane.setHgap(15); 
        pane.setVgap(15);

        // Initialize the scene
       	int paneWidth = (int)((TILE_WIDTH + pane.getHgap()) * 
	 board.getGrid().length);
	int paneHeight = (int)((TILE_WIDTH + pane.getVgap()) * 
	 (board.getGrid().length) + 2*pane.getVgap() + TEXT_SIZE_MID);
	Scene scene = new Scene(pane,paneWidth,paneHeight);
	primaryStage.setTitle("Gui2048");
	primaryStage.setScene(scene);
	primaryStage.show();

	// Show the name of the game
	Text title = new Text();
	title.setText("2048");
	title.setFont(Font.font("Times New Roman",FontWeight.BOLD,
	 FontPosture.ITALIC,TEXT_SIZE_MID));
	title.setFill(COLOR_VALUE_LIGHT);

	// Show the score of the game
	scoreText.setText("Score: " + board.getScore());
       	scoreText.setFont(Font.font("Times New Roman",FontWeight.BOLD,
	 TEXT_SIZE_HIGH));
	scoreText.setFill(COLOR_VALUE_LIGHT);

	// Add the name and the score to the pane
	pane.add(title,0,0,2,1);
	pane.add(scoreText,board.getGrid().length - 2,0,2,1);
	GridPane.setHalignment(title,HPos.CENTER);
	GridPane.setHalignment(scoreText,HPos.CENTER);

	// Create the initial game board
	createBoard();
   
	// Change board based on key input
	scene.setOnKeyPressed(new myKeyHandler());
	     
    }
	// Inner class myHeyHandler used to take input from the user
        // and apply changes to the board
	private class myKeyHandler implements EventHandler<KeyEvent> {
	    public void handle(KeyEvent k) {
		// Make sure gameOver is only implemented once
                if(board.isGameOver() == true && numGameOver == 0) {
                    gameOver();
		    numGameOver++;
                    return;
                }		  
	        // Map key input to board movement
		// Add a new tile after each move
		// Save board when S is pressed	
		switch(k.getCode()){
		    case UP: 
			if(board.canMove(Direction.UP)) {
	                    board.move(Direction.UP);
			    System.out.println("Moving UP");
			    board.addRandomTile();
			}
			break;
		    case DOWN: 
			if(board.canMove(Direction.DOWN)) {
		            board.move(Direction.DOWN);
			    System.out.println("Moving DOWN");
			    board.addRandomTile();
			}
		        break;
		    case LEFT: 
			if(board.canMove(Direction.LEFT)) {
		            board.move(Direction.LEFT);
			    System.out.println("Moving LEFT");
			    board.addRandomTile();
			}
		        break;
		    case RIGHT: 
			if(board.canMove(Direction.RIGHT)) {
	                    board.move(Direction.RIGHT);
			    System.out.println("Moving RIGHT");
			    board.addRandomTile();
			}
		        break;
		    case S: 
			try {
			    board.saveBoard(outputBoard);
			    System.out.println("Saving Board to " + 
			     outputBoard);
			} catch (IOException e) {
		            System.out.println("saveBoard threw an Exception");
			}
			break;
		    case R:
			if(board.isGameOver() == false) {
			    board.rotate(true);
			}
			break;
		    default:break;
		}
		// Update score
		score = board.getScore();
		scoreText.setText("Score: " + score);
		// Modify tiles to show the new move
		for(int i = 0; i < board.getGrid().length; i++) {
		    for(int j = 0; j < board.getGrid().length; j++) {
			int objPos = (board.getGrid().length * i) + j; 
			modifyTile(rectList.get(objPos),textList.get(objPos),
			 board.getGrid()[j][i]);
			if(board.getGrid()[j][i] == 0) {
			    textList.get(objPos).setText("");
			} else {
			    textList.get(objPos).setText("" + 
			     board.getGrid()[j][i]);
			}
		    }
		}
	    }
	}           
    
    
    /** Add your own Instance Methods Here */
    
    // Name: createBoard
    // Purpose: creates the initial board and stores tiles and text
    // for later changes
    // Parameters: none
    // Return: void
    private void createBoard() {

	// Create a new board based on input size
	for(int i = 0; i < board.getGrid().length; i++) {
	    for(int j = 0; j < board.getGrid().length; j++) {

		// Add tile
		Rectangle square = new Rectangle();
		square.setWidth(TILE_WIDTH);
		square.setHeight(TILE_WIDTH);
		
		// Add tile text
		Text text = new Text();
		if(board.getGrid()[j][i] != 0) {
		    text.setText("" + board.getGrid()[j][i]);
		}
		
		// Modify tile appearance based on numerical value
		modifyTile(square,text,board.getGrid()[j][i]);

		// Store tiles and tile text
		rectList.add(square);
		textList.add(text);

		// Add tiles to pane
		pane.add(square, i, j+1);
		pane.add(text, i, j+1);
		GridPane.setHalignment(text, HPos.CENTER);
	    }
	}
    }

    // Name: modifyTile
    // Purpose: modifies a tile's appearance based on its numerical value
    // Parameters: Rectangle square, the tile to modify, Text text, the
    // text to modify, int value, the numerical value of the tile
    // Return: void
    private void modifyTile(Rectangle square, Text text, int value) {
	// Implement rules for tiles lower than 8
	if(value < 8) {
	    text.setFont(Font.font("Times New Roman", FontWeight.BOLD, 
	     TEXT_SIZE_LOW));
	    text.setFill(COLOR_VALUE_DARK);
	    if(value == 0) {
		square.setFill(COLOR_EMPTY);
	    }
	    if(value == 2) {
		square.setFill(COLOR_2);
	    }
	    if(value == 4) {
		square.setFill(COLOR_4);
	    }
	}
	// Implement rules for tiles higher than or equal to 8
	if(value >= 8) {
	    text.setFill(COLOR_VALUE_LIGHT);
	    // Implement rules for low valued tiles
	    if(value >= 8 && value <= 64) {
		text.setFont(Font.font("Times New Roman", FontWeight.BOLD, 
		 TEXT_SIZE_LOW));
		if(value == 8) {
		    square.setFill(COLOR_8);
		}
		if(value == 16) {
		    square.setFill(COLOR_16);
		}
		if(value == 32) {
		    square.setFill(COLOR_32);
		}
		if(value == 64) {
		    square.setFill(COLOR_64);
		}
	    }
	    // Implement rules for mid-valued tiles
	    if(value >= 128 && value <= 512) {
		text.setFont(Font.font("Times New Roman", FontWeight.BOLD, 
		 TEXT_SIZE_MID));
		if(value == 128) {
		    square.setFill(COLOR_128);
		}
		if(value == 256) {
		    square.setFill(COLOR_256);
		}
		if(value == 512) {
		    square.setFill(COLOR_512);
		}
	    }
	    // Implement rules for high-valued tiles
	    if(value >= 1024) {
		text.setFont(Font.font("Times New Roman", FontWeight.BOLD, 
		 TEXT_SIZE_HIGH));
		if(value == 1024) {
		    square.setFill(COLOR_1024);
		}
		if(value == 2048) {
		    square.setFill(COLOR_2048);
		}
		if(value > 2048) {
		    square.setFill(COLOR_OTHER);
		}
	    }
	}   
    }

    // Name: gameOver
    // Purpose: implements a Game Over overlay when there are
    // no valid moves
    // Parameters: none
    // Return: void
    private void gameOver() {

	// Create transparent overlay
	Rectangle block = new Rectangle();
	block.widthProperty().bind(pane.widthProperty());
	block.heightProperty().bind(pane.heightProperty());
	block.setFill(COLOR_GAME_OVER);

	// Create Game Over message
	Text text = new Text();
	text.setText("Game Over!");
	text.setFont(Font.font("Times New Roman",FontWeight.BOLD,50));
	text.setFill(COLOR_VALUE_DARK);
       
	GridPane.setHalignment(block,HPos.CENTER);
	GridPane.setHalignment(text,HPos.CENTER);

	// Add Game Over to pane
	pane.add(block,0,0,board.getGrid().length,board.getGrid().length + 1);
	pane.add(text,0,0,board.getGrid().length,board.getGrid().length + 1);
    }	    	

    /** DO NOT EDIT BELOW */

    // The method used to process the command line arguments
    private void processArgs(String[] args)
    {
        String inputBoard = null;   // The filename for where to load the Board
        int boardSize = 0;          // The Size of the Board

        // Arguments must come in pairs
        if((args.length % 2) != 0)
        {
            printUsage();
            System.exit(-1);
        }

        // Process all the arguments 
        for(int i = 0; i < args.length; i += 2)
        {
            if(args[i].equals("-i"))
            {   // We are processing the argument that specifies
                // the input file to be used to set the board
                inputBoard = args[i + 1];
            }
            else if(args[i].equals("-o"))
            {   // We are processing the argument that specifies
                // the output file to be used to save the board
                outputBoard = args[i + 1];
            }
            else if(args[i].equals("-s"))
            {   // We are processing the argument that specifies
                // the size of the Board
                boardSize = Integer.parseInt(args[i + 1]);
            }
            else
            {   // Incorrect Argument 
                printUsage();
                System.exit(-1);
            }
        }

        // Set the default output file if none specified
        if(outputBoard == null)
            outputBoard = "2048.board";
        // Set the default Board size if none specified or less than 2
        if(boardSize < 2)
            boardSize = 4;

        // Initialize the Game Board
        try{
            if(inputBoard != null)
                board = new Board(inputBoard, new Random());
            else
                board = new Board(boardSize, new Random());
        }
        catch (Exception e)
        {
            System.out.println(e.getClass().getName() + 
                               " was thrown while creating a " +
                               "Board from file " + inputBoard);
            System.out.println("Either your Board(String, Random) " +
                               "Constructor is broken or the file isn't " +
                               "formated correctly");
            System.exit(-1);
        }
    }

    // Print the Usage Message 
    private static void printUsage()
    {
        System.out.println("Gui2048");
        System.out.println("Usage:  Gui2048 [-i|o file ...]");
        System.out.println();
        System.out.println("  Command line arguments come in pairs of the "+ 
                           "form: <command> <argument>");
        System.out.println();
        System.out.println("  -i [file]  -> Specifies a 2048 board that " + 
                           "should be loaded");
        System.out.println();
        System.out.println("  -o [file]  -> Specifies a file that should be " + 
                           "used to save the 2048 board");
        System.out.println("                If none specified then the " + 
                           "default \"2048.board\" file will be used");  
        System.out.println("  -s [size]  -> Specifies the size of the 2048" + 
                           "board if an input file hasn't been"); 
        System.out.println("                specified.  If both -s and -i" + 
                           "are used, then the size of the board"); 
        System.out.println("                will be determined by the input" +
                           " file. The default size is 4.");
    }
}
