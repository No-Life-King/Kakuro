package kakuro;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * This is the main game board object that contains information about the tiles,
 * rows and columns, and GUI features. It contains methods that correspond to Kakuro game play.
 * @author Phil Smith
 * @author Bobby Palmer
 *
 */
public class GameBoard {

	private Stage primaryStage;
	private Tile[][] tiles;
	private static final int tileSize = 60;
	private BorderPane root = new BorderPane();
	private GridPane appContent = new GridPane();
	private ArrayList<Row> rows = new ArrayList<Row>();
	private ArrayList<Column> columns = new ArrayList<Column>();
	private int whiteTiles = 0, enteredValues = 0, boardSize;

	/**
	 * Constructor generates blank 10x10 board as default application behavior upon opening.
	 * @param primaryStage
	 */
	public GameBoard(Stage primaryStage) {
		this.primaryStage = primaryStage;

		LandingPage lp = new LandingPage(this);
		appContent = lp.getLandingPage();
	}

	/**
	 * Overloaded constructor, allows a game-board of any size to be loaded from any valid
	 * board data file (*.KAKURO).
	 * @param primaryStage
	 * @param file
	 */
	public GameBoard(Stage primaryStage, File file) {
		this.primaryStage = primaryStage;
		this.readBoard(file);
		int boardSize = tiles.length;
		this.boardSize = boardSize;

		root.setMaxSize(tileSize*(boardSize), tileSize*(boardSize));

		this.buildRowsColumns();

		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				Tile tile = tiles[i][j];
				if(tile.getType().equals("white")) {
					WhiteTile t = (WhiteTile) tiles[i][j];
					if(!t.loadBoardValue.equals("")) {
						t.valueChange(t.loadBoardValue);
					}
				}
				appContent.add(tile, j, i);
			}
		}
		appContent.setPadding(new Insets(5, 5, 5, 5));
	}

	/**
	 * This constructor randomly generates a new size x size board.
	 * @param primaryStage The primary stage to which the board should be drawn.
	 * @param size The width and length of the puzzle in terms of puzzle squares.
	 */
	public GameBoard(Stage primaryStage, int size) {
		this.boardSize = size;
		this.primaryStage = primaryStage;
		this.tiles = new Tile[size][size];

		root.setMaxSize(tileSize*(boardSize), tileSize*(boardSize));

		// generate the coordinates for randomly placed black tiles, keeping a few rules in mind
		// the number to generate starts at (n-1)^2 - (n-2)^2 where n is the board size in tiles
		ArrayList<int[]> randomBlackTiles = generateRandomBlackTiles((int) (Math.pow(boardSize - 1, 2) - Math.pow(boardSize - 2, 2)));

		// place the randomly generated black tiles on the board
		for (int[] blackTile: randomBlackTiles) {
			BlackTile tile = new BlackTile(tileSize);
			tiles[blackTile[0]][blackTile[1]] = tile;
			appContent.add(tile, blackTile[1], blackTile[0]);
		}

		LinkedList<Integer> availableValues = new LinkedList<Integer>();

		// add 1-9 to the available values
		for (int x=1; x<=9; x++) {
			availableValues.add(x);
		}

		// place the rest of the tiles on the game board
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {

				// row 1 and column 1 should all be black tiles
				if (i == 0 || j == 0) {
					BlackTile tile = new BlackTile(tileSize);
					tiles[i][j] = tile;
					appContent.add(tile, j, i);
					tile.setx(i);
					tile.sety(j);
				}

				// if a space isn't set yet, put a white tile there
				if (tiles[i][j] == null) {
					WhiteTile tile = new WhiteTile(this);
					tiles[i][j] = tile;
					tile.setx(i);
					tile.sety(j);

					// eliminate values that are already used from the available values
					int k = i-1;
					while (tiles[k][j].getType().equals("white")) {
						WhiteTile above = (WhiteTile) tiles[k][j];
						if (availableValues.indexOf(Integer.parseInt(above.loadBoardValue)) != -1) {
							availableValues.remove(availableValues.indexOf(Integer.parseInt(above.loadBoardValue)));
						}
						k--;
					}

					// if all values are assigned, the max row size has been reached and a black tile should be placed
					if (availableValues.size() > 0) {
						int randIndex = (int) (Math.random() * availableValues.size());
						tile.loadBoardValue = Integer.toString(availableValues.remove(randIndex));
						appContent.add(tile, j, i);
					} else {
						BlackTile blackie = new BlackTile(tileSize);
						tiles[i][j] = blackie;
						blackie.setx(i);
						blackie.sety(j);
						for (int x=1; x<=9; x++) {
							availableValues.add(x);
						}
						appContent.add(blackie, j, i);
					}

				} else {

					// if a black tile is reached, the row is finished so all the values become available again
					availableValues.clear();
					for (int x=1; x<=9; x++) {
						availableValues.add(x);
					}
				}
			}
		}

		// set the row and column sums on the black tiles
		for (int i = 0; i < boardSize; i++) {

			for (int j = 0; j < boardSize; j++) {
				Tile tile = tiles[i][j];

				if (tile.getType().equals("black")) {
					int k=j+1;
					int l=i+1;
					int rowSum = 0;
					int columnSum = 0;

					// get the row sum
					while (k<boardSize && tiles[i][k].getType().equals("white")) {
						WhiteTile whitey = (WhiteTile) tiles[i][k];
						rowSum += Integer.parseInt(whitey.loadBoardValue);
						k++;
					}

					// get the column sum
					while (l<boardSize && tiles[l][j].getType().equals("white")) {
						WhiteTile whitey = (WhiteTile) tiles[l][j];
						columnSum += Integer.parseInt(whitey.loadBoardValue);
						l++;
					}

					BlackTile blackie = (BlackTile) tile;
					if (rowSum == 0) {
						blackie.setTop(0);
					} else {
						blackie.setTop(rowSum);
					}

					if (columnSum == 0) {
						blackie.setBottom(0);
					} else {
						blackie.setBottom(columnSum);
					}
				}
			}
		}

		// build the row and column objects and calculate all the valid values
		this.buildRowsColumns();
		appContent.setPadding(new Insets(5, 5, 5, 5));
	}

	/**
	 * Randomly generates black tiles to be placed on a random board.
	 * @param i The number of black tiles to generate.
	 * @return An ArrayList of black tile coordinates.
	 */
	private ArrayList<int[]> generateRandomBlackTiles(int i) {
		ArrayList<int[]> coords = new ArrayList<int[]>();

		while (coords.size() < i) {
			int[] tileCoords = new int[2];

			// generate a black tile for each row
			for (int a=1; a<boardSize; a++) {
				int b = 0;
				while (b == 0) {
					b = (int) (Math.random() * boardSize);
				}

				tileCoords[0] = a;
				tileCoords[1] = b;

				boolean valid = validateGeneratedTile(tileCoords, coords);

				if (valid) {
					coords.add(tileCoords.clone());
				}
			}

			// generate a black tile for each column
			for (int c=1; c<boardSize; c++) {
				int d = 0;
				while (d == 0) {
					d = (int) (Math.random() * boardSize);
				}

				tileCoords[0] = d;
				tileCoords[1] = c;

				boolean valid = validateGeneratedTile(tileCoords, coords);
				if (valid) {
					coords.add(tileCoords.clone());
				}
			}
		}

		return coords;
	}

	/**
	 * Checks to see if a randomly generated black tile has been placed in an appropriate position.
	 * @param tileCoords The coordinates of the black tile.
	 * @param coords The rest of the black tiles that have already been generated for a random board.
	 * @return True if ok. Otherwise, false.
	 */
	private boolean validateGeneratedTile(int[] tileCoords, ArrayList<int[]> coords) {
		if (coords.size() == 0) {
			return true;
		}

		boolean valid = true;

		// compare against all placed black tiles
		for (int y=0; y<coords.size(); y++) {
			int xCoord = coords.get(y)[0];
			int yCoord = coords.get(y)[1];

			// if this position has already been taken, don't add it to the list again
			if (tileCoords[0] == xCoord && tileCoords[1] == yCoord) {
				valid = false;
				break;

			// if this black tile is only 1 white space away from another black tile, don't add it to the board
			} else if ((tileCoords[0] == coords.get(y)[0] && Math.abs(tileCoords[1] - coords.get(y)[1]) == 2)
					|| (tileCoords[1] == coords.get(y)[1] && Math.abs(tileCoords[0] - coords.get(y)[0]) == 2)) {
				valid = false;
				break;
			}
		}

		return valid;
	}

	/**
	 * Collects all the GUI elements and adds them to a single pane which is returned
	 * to the application master pane in Main class.
	 * @return root: The pane on which all the game-board objects are instantiated.
	 */
	public Parent createContent() {
		root.setTop(this.generateMenu());
		root.setCenter(appContent);
		return root;
	}

	/**
	 * Builds all of the row and column objects for any given game board and calculates all of the
	 * possible starting values for that board.
	 */
	private void buildRowsColumns() {
		int x = 1;
		int y = 1;
		int upperBound = (boardSize-1);

		// start at (1, 1) and create column objects out of the contiguous white tiles
		while (x <= upperBound && y <= upperBound) {

			Column column = new Column();
			while (x <= upperBound) {

				// add the white tile to the column
				if (tiles[x][y].getType().equals("white")) {
					column.add(tiles[x][y]);
				} else {

					// If we run into a black tile, finish off the column.
					// do not include column objects that contain no white tiles -
					// this may occur when there are adjacent black tiles
					if (column.getSize() > 0) {
						columns.add(column);

						// set the sum of the column to the number in the bottom left-hand
						// corner of black tile that is on top of the column
						column.setSum(((BlackTile) tiles[x-column.getSize()-1][y]).getBottom());
						column = new Column();
					}
				}

				x++;
			}

			// finish off the last column
			if (column.getSize() > 0){
				columns.add(column);
				column.setSum(((BlackTile) tiles[x-column.getSize()-1][y]).getBottom());
			}

			x = 1;
			y++;
		}

		// calculate the valid values for every column upon board initialization
		for (Column col: columns) {
			col.calcValidValues();
		}

		x = 1;
		y = 1;

		// start at (1, 1) and create row objects out of the contiguous white tiles
		while (x <= upperBound && y <= upperBound) {

			Row row = new Row();
			while (y <= upperBound) {

				// add the white tile to the row
				if (tiles[x][y].getType().equals("white")) {
					row.add(tiles[x][y]);
					whiteTiles++;
				} else {
					if (row.getSize() > 0) {

						// If we run into a black tile, finish off the row.
						// do not include row objects that contain no white tiles -
						// this may occur when there are adjacent black tiles
						rows.add(row);
						row.setSum(((BlackTile) tiles[x][y-row.getSize()-1]).getTop());
						row = new Column();
					}
				}

				y++;
			}

			// finish off the last row
			if (row.getSize() > 0){
				rows.add(row);
				row.setSum(((BlackTile) tiles[x][y-row.getSize()-1]).getTop());
			}

			y = 1;
			x++;
		}

		// calculate the valid values for every row upon board initialization
		for (Row row: rows) {
			row.calcValidValues();
		}
	}

	/**
	 * Populates and draws the menu bar.
	 * @return The menu bar.
	 */
	public MenuBar generateMenu() {

		// create the menu bar object
		MenuBar menuBar = new MenuBar();

		// add a game menu to it
		Menu gameMenu = new Menu("Game");

		// create the necessary menu items
		MenuItem newMenuItem = new MenuItem("New");
		MenuItem readMenuItem = new MenuItem("Read");
		MenuItem saveMenuItem = new MenuItem("Save");
		MenuItem cheat = new MenuItem("Cheat");
		MenuItem exitMenuItem = new MenuItem("Exit");

		// add a menu for randomly creating a new board of size
		// 6x6, 8x8, or 10x10
		Menu boardMenu = new Menu("New Board");

		MenuItem six = new MenuItem("6x6");
		MenuItem eight = new MenuItem("8x8");
		MenuItem ten = new MenuItem("10x10");

		// trigger method calls on action
		newMenuItem.setOnAction(actionEvent -> clearBoard());
		readMenuItem.setOnAction(actionEvent -> open());
		saveMenuItem.setOnAction(actionEvent -> save());
		cheat.setOnAction(actionEvent -> cheat());

		// generate the new board
		six.setOnAction(actionEvent -> generateRandomBoard(6));
		eight.setOnAction(actionEvent -> generateRandomBoard(8));
		ten.setOnAction(actionEvent -> generateRandomBoard(10));

		// if the exit button is clicked, exit the process immediately
		exitMenuItem.setOnAction(actionEvent -> Platform.exit());

		// add all the menu buttons to the menu
		gameMenu.getItems().addAll(newMenuItem, readMenuItem, saveMenuItem, cheat, new SeparatorMenuItem(), exitMenuItem);

		boardMenu.getItems().addAll(six, eight, ten);

		// add the menu to the menu bar
		menuBar.getMenus().addAll(gameMenu, boardMenu);

		return menuBar;
	}

	/**
	 * Fills out the first puzzle box for which there is only one possible solution.
	 * May be triggered by pressing the space-bar or the cheat button in the menu.
	 */
	public void cheat() {
		OUTER:
		for (int x= 0; x < boardSize; x++) {
			for (int y = 0; y < 10; y++) {

				// checks if there is only one solution for this tile
				HashSet<Integer> tileValues = validValues(x, y);
				if (tileValues.size() == 1) {

					// if there is, set the tile to that value
					((WhiteTile) tiles[x][y]).setValue((int) tileValues.toArray()[0]);

					// break both loops and stop cheating
					break OUTER;
				}
			}
		}
	}

	/**
	 * Gets the integer size of the tiles. Can be used by the tiles to draw themselves.
	 * @return The size (in pixels) of each tile.
	 */
	public int getTileSize() {
		return tileSize;
	}

	/**
	 * Get the valid values for a single tile based on its x and y coordinates. The valid values
	 * are the intersection of the sets of possible row values and column values.
	 * @param x The tile's x position.
	 * @param y The tile's y position.
	 * @return A set of valid values for the tile.
	 */
	public HashSet<Integer> validValues(int x, int y) {
		HashSet<Integer> intersect = new HashSet<Integer>();


		HashSet<Integer> rowValues = new HashSet<Integer>();
		// get the row that contains the tile
		for (Row row: rows) {
			if (row.containsTile(x, y)) {

				// get the row's valid values
				rowValues = row.getValidValues();
				break;
			}
		}

		HashSet<Integer> colValues = new HashSet<Integer>();
		// get the column that contains the tile
		for (Column col: columns) {
			if (col.containsTile(x, y)) {

				// get the column's valid values
				colValues = col.getValidValues();
				break;
			}
		}

		// calculate the intersection of the row and column values
		for (int rowValue: rowValues) {
			for (int colValue: colValues) {
				if (rowValue == colValue) {
					intersect.add(colValue);
				}
			}
		}

		return intersect;
	}

	/**
	 * Reads the data from valid (*.KAKURO) data-files and generates the corresponding
	 * tile objects for display on the game-board.
	 * @param file
	 */
	public void readBoard(File file) {
		String line;
		ArrayList<Tile> rowTiles = new ArrayList<Tile>();

		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
			int row = 0;

			while((line = bufferedReader.readLine()) != null) {
				if (line.startsWith("row")) {
					int columns = rowTiles.size();
					if (row == 0) {
						tiles = new Tile[columns][columns];
					}

					for (int j = 0; j < columns; j++) {
						tiles[row][j] = rowTiles.get(j);
					}

					row += 1;
					rowTiles = new ArrayList<Tile>();
					continue;
				}

				String[] data = line.split("\t");
				if(data[0].equals("Black")) {
					BlackTile tile = new BlackTile(tileSize);

					tile.setValues(data[1], data[2]);

					tile.setx(row);
					tile.sety(rowTiles.size());

					rowTiles.add(tile);
				}
				else if(data[0].equals("White")) {
					WhiteTile tile = new WhiteTile(this);

					tile.setx(row);
					tile.sety(rowTiles.size());

					if(!data[1].equals("0")) {
						tile.loadBoardValue = data[1];
					}

					rowTiles.add(tile);
				}

			}
			bufferedReader.close();
		} catch(FileNotFoundException ex) {
			System.out.println("Unable to find file.");
		} catch(IOException ex) {
			System.out.println("Unable to read file.");
		}

	}

	/**
	 * Gets the row that contains the tile with the coordinates (x, y).
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @return The corresponding row.
	 */
	private Row getRow(int x, int y) {
		Row tileRow = new Row();

		for (Row row: rows) {
			if (row.containsTile(x, y)) {
				tileRow = row;
			}
		}

		return tileRow;
	}

	/**
	 * Gets the column that contains the tile with the coordinates (x, y).
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @return The corresponding column.
	 */
	private Column getColumn(int x, int y) {
		Column tileCol = new Column();

		for (Column col: columns) {
			if (col.containsTile(x, y)) {
				tileCol = col;
			}
		}

		return tileCol;
	}

	/**
	 * Once a value has been entered by the user, set the value as entered throughout
	 * the rest of the program.
	 * @param value The value that the user has entered.
	 * @param x The x coordinate of the tile upon which the value has been entered.
	 * @param y The y coordinate of the tile upon which the value has been entered.
	 */
	public void setEntered(int value, int x, int y) {
		Row tileRow = getRow(x, y);
		Column tileCol = getColumn(x, y);

		// increment entered values to check the winning condition
		enteredValues++;

		// set the value as entered within the row and column
		tileRow.addEntered(value);
		tileCol.addEntered(value);
	}

	/**
	 * Once a value has been deleted by the user, set the value as entered throughout
	 * the rest of the program.
	 * @param value The value that the user has deleted.
	 * @param x The x coordinate of the tile upon which the value has been deleted.
	 * @param y The y coordinate of the tile upon which the value has been deleted.
	 */
	public void deleteEntry(int value, int x, int y) {
		Row tileRow = getRow(x, y);
		Column tileCol = getColumn(x, y);

		// decrement the number of entered values
		enteredValues--;

		// set the value as deleted within the row and column
		tileRow.deleteEntered(value);
		tileCol.deleteEntered(value);
	}

	/**
	 * A check that can be quickly performed after each value is entered to see if the
	 * user has won this instance of Kakuro.
	 * @return True if the user has won - otherwise false.
	 */
	public boolean winner() {

		// if the number of entered values is equal to the number of white tiles,
		// then the user must have won
		if (whiteTiles == enteredValues) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Victory is Yours");
			alert.setHeaderText("Congratulations, you won the game!");
			alert.setContentText("Return to game.");

			alert.showAndWait();
			return true;
		}

		return false;
	}

	/**
	 * Displays a file-chooser which allows a user to save the current game-board in its present state
	 * to a valid (*.KAKURO) data file on the user's system.
	 */
	public void save() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save Game");

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("KAKURO files (*.kakuro)", "*.kakuro");
        fileChooser.getExtensionFilters().add(extFilter);

        fileChooser.setInitialDirectory(new File("Resources"));

        File file = fileChooser.showSaveDialog(primaryStage);

        if(file != null){
        	try {
        		BufferedWriter bw = new BufferedWriter(new FileWriter(file));

        		for(int i = 0; i < tiles.length; i++) {
        			if(i != 0) {
        				bw.write("row" + "\n");
        			}
        			for(int j = 0; j < tiles[i].length; j++) {
        				bw.write(tiles[i][j].toString() + "\n");
        			}
        		}

        		bw.write("row");
        		bw.close();
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
        }
	}

	/**
	 * Displays a file-chooser from which only valid (*.KAKURO) files may be
	 * selected to be opened; generates an entirely new Game-board object using
	 * the valid file constructor, and displays the new Game-board object on the screen.
	 */
	public void open() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Game");

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("KAKURO files (*.kakuro)", "*.kakuro");
        fileChooser.getExtensionFilters().add(extFilter);

        fileChooser.setInitialDirectory(new File("Resources"));

        File file = fileChooser.showOpenDialog(primaryStage);

        openBoard(file);
	}

	/**
	 * Randomly generate a new board of that has size x size puzzle squares.
	 * @param size The width and height of the puzzle in terms of puzzle squares.
	 */
	public void generateRandomBoard(int size) {
		try {
			GameBoard gameBoard = new GameBoard(primaryStage, size);
			Scene scene = new Scene(gameBoard.createContent());
			scene.getStylesheets().add(Main.class.getResource("Main.css").toExternalForm());
			scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
				 public void handle(final KeyEvent keyEvent) {
				   if (keyEvent.getCode() == KeyCode.SPACE) {
				    gameBoard.cheat();
				    keyEvent.consume();
				   }
				 }
				});
			primaryStage.setScene(scene);
			primaryStage.setTitle("Taidana Kakuro");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Load a game board from the specified file.
	 * @param file The file from which to load the board.
	 */
	private void openBoard(File file) {
		if (file != null){
        	try {
    			GameBoard gameBoard = new GameBoard(primaryStage, file);
    			Scene scene = new Scene(gameBoard.createContent());
    			scene.getStylesheets().add(Main.class.getResource("Main.css").toExternalForm());
    			scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
    				 public void handle(final KeyEvent keyEvent) {
    				   if (keyEvent.getCode() == KeyCode.SPACE) {
    				    gameBoard.cheat();
    				    keyEvent.consume();
    				   }
    				 }
    				});
    			primaryStage.setScene(scene);
    			primaryStage.setTitle("Taidana Kakuro");
    			primaryStage.show();
    		} catch(Exception e) {
    			e.printStackTrace();
    		}
        }
	}

	/**
	 * Causes all the entered WhiteTile values to be cleared, and their values in the game-logic
	 * row/column objects to be deleted.
	 */
	public void clearBoard() {

		// if "new" is clicked while the landing page is up, load the default game board
		if (tiles != null) {

			// set all the white tiles to empty, rolling back all changes
			for(int i = 0; i < tiles.length; i++) {
				for(int j = 0; j < tiles[i].length; j++) {
					if(tiles[i][j].getType().equals("white")) {
						WhiteTile tile = (WhiteTile) tiles[i][j];
						tile.setEmpty();
					}
				}
			}

			// set the total number of entered values equal to zero so
			// that the winning condition triggers at the right time
			enteredValues = 0;
		} else {
			openBoard(new File("Resources/g2.kakuro"));
		}
	}

}
