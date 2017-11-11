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
import javafx.application.Platform;
import javafx.event.EventHandler;
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
	 * comment me
	 * @param primaryStage
	 */
	public GameBoard(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.openDefault();
	}
	
	public GameBoard(Stage primaryStage, File file) {
		this.primaryStage = primaryStage;
		this.readBoard(file);
	}

	/**
	 * comment me
	 * @return
	 */
	public Parent createContent() {
		int boardSize = tiles.length;
		this.boardSize = boardSize;

		root.setMaxSize(tileSize*(boardSize), tileSize*(boardSize));

		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				Tile tile = tiles[i][j];
				tile.setx(i);
				tile.sety(j);
				appContent.add(tile, j, i);
			}
		}

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

		root.setTop(this.generateMenu());
		root.setCenter(appContent);
		return root;
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

		// trigger method calls on action
		newMenuItem.setOnAction(actionEvent -> clearBoard());
		readMenuItem.setOnAction(actionEvent -> open());
		saveMenuItem.setOnAction(actionEvent -> save());
		cheat.setOnAction(actionEvent -> cheat());

		// if the exit button is clicked, exit the process immediately
		exitMenuItem.setOnAction(actionEvent -> Platform.exit());

		// add all the menu buttons to the menu
		gameMenu.getItems().addAll(newMenuItem, readMenuItem, saveMenuItem, cheat, new SeparatorMenuItem(), exitMenuItem);

		// add the menu to the menu bar
		menuBar.getMenus().addAll(gameMenu);

		return menuBar;
	}

	/**
	 * Fills out the first puzzle box for which there is only one possible solution.
	 * May be triggered by pressing the spacebar or the cheat button in the menu.
	 */
	public void cheat() {
		OUTER:
		for (int x=0; x<boardSize; x++) {
			for (int y=0; y<10; y++) {

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
	 * comment me
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

					rowTiles.add(tile);
				}
				else if(data[0].equals("White")) {
					WhiteTile tile = new WhiteTile(this);
					
					int value = Integer.parseInt(data[1]);
					if(value != 0) {
						tile.setValue(value);
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
	 * Comment pl0x
	 */
	public void save() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save Game");

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("KAKURO files (*.kakuro)", "*.kakuro");
        fileChooser.getExtensionFilters().add(extFilter);

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
	 * comment me
	 */
	public void open() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Game");

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("KAKURO files (*.kakuro)", "*.kakuro");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(primaryStage);

        if(file != null){
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
    			primaryStage.setTitle("Kakuro!");
    			primaryStage.show();
    		} catch(Exception e) {
    			e.printStackTrace();
    		}
        }
	}

	/**
	 * comment me
	 */
	private void openDefault() {
		File file = new File("Resources/default.kakuro");
		this.readBoard(file);
	}
	
	public void clearBoard() {
		
	}

}
