package kakuro;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class GameBoard implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private transient Stage primaryStage;
	private Tile[][] tiles;
	private static final int tileSize = 60;
	private transient BorderPane root = new BorderPane();
	private transient GridPane appContent = new GridPane();
	private transient SidePanel sidePanel;
	private transient ArrayList<Row> rows = new ArrayList<Row>();
	private transient ArrayList<Column> columns = new ArrayList<Column>();
	private int boardSize;
	
	public GameBoard(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}
	
	public Parent createContent() {
		this.openDefault();
		int boardSize = tiles.length;
		this.boardSize = boardSize;

		root.setMaxSize(tileSize*(boardSize+4), tileSize*(boardSize+1));

		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				Tile tile = tiles[i][j];
				tile.setx(i);
				tile.sety(j);
				appContent.add(tile, j, i);
			}
		}

		sidePanel = new SidePanel(200, tileSize*boardSize+(boardSize*2));

		int x = 1;
		int y = 1;
		int upperBound = (boardSize-1);

		while (x <= upperBound && y <= upperBound) {

			Column column = new Column();
			while (x <= upperBound) {
				if (tiles[x][y].getType().equals("white")) {
					column.add(tiles[x][y]);
				} else {
					if (column.getSize() > 0) {
						columns.add(column);
						column.setSum(((BlackTile) tiles[x-column.getSize()-1][y]).getBottom());
						column = new Column();
					}
				}

				x++;
			}

			if (column.getSize() > 0){
				columns.add(column);
				column.setSum(((BlackTile) tiles[x-column.getSize()-1][y]).getBottom());
			}

			x = 1;
			y++;
		}


		for (Column col: columns) {
			col.calcValidValues();
			//System.out.println(col);
		}

		x = 1;
		y = 1;
		upperBound = (boardSize-1);

		while (x <= upperBound && y <= upperBound) {

			Row row = new Row();
			while (y <= upperBound) {
				if (tiles[x][y].getType().equals("white")) {
					row.add(tiles[x][y]);
				} else {
					if (row.getSize() > 0) {
						rows.add(row);
						row.setSum(((BlackTile) tiles[x][y-row.getSize()-1]).getTop());
						row = new Column();
					}
				}

				y++;
			}

			if (row.getSize() > 0){
				rows.add(row);
				row.setSum(((BlackTile) tiles[x][y-row.getSize()-1]).getTop());
			}

			y = 1;
			x++;
		}
		System.out.println();
		for (Row row: rows) {
			row.calcValidValues();
			//System.out.println(row);
		}

		root.setTop(this.generateMenu(root));
		root.setCenter(appContent);
		return root;
	}

	public MenuBar generateMenu(Pane root) {
		MenuBar menuBar = new MenuBar();

		Menu gameMenu = new Menu("Game");
		MenuItem newMenuItem = new MenuItem("New");
		MenuItem readMenuItem = new MenuItem("Read");
		MenuItem saveMenuItem = new MenuItem("Save");
		MenuItem cheat = new MenuItem("Cheat");
		MenuItem exitMenuItem = new MenuItem("Exit");
		
		readMenuItem.setOnAction(actionEvent -> open());
		saveMenuItem.setOnAction(actionEvent -> save());
		cheat.setOnAction(actionEvent -> cheat());
		exitMenuItem.setOnAction(actionEvent -> Platform.exit());
		
		gameMenu.getItems().addAll(newMenuItem, readMenuItem, saveMenuItem, cheat, new SeparatorMenuItem(), exitMenuItem);

		menuBar.getMenus().addAll(gameMenu);

		return menuBar;
	}

	public void cheat() {
		OUTER:
		for (int x=0; x<boardSize; x++) {
			for (int y=0; y<10; y++) {
				HashSet<Integer> tileValues = validValues(x, y);
				if (tileValues.size() == 1) {
					((WhiteTile) tiles[x][y]).setValue((int) tileValues.toArray()[0]);
					break OUTER;
				}
			}
		}
	}

	public int getSize() {
		return tileSize;
	}

	public SidePanel getSidePanel() {
		return sidePanel;
	}

	public HashSet<Integer> validValues(int x, int y) {
		HashSet<Integer> intersect = new HashSet<Integer>();
		HashSet<Integer> rowValues = new HashSet<Integer>();
		for (Row row: rows) {
			if (row.containsTile(x, y)) {
				rowValues = row.getValidValues();
				break;
			}
		}

		HashSet<Integer> colValues = new HashSet<Integer>();
		for (Column col: columns) {
			if (col.containsTile(x, y)) {
				colValues = col.getValidValues();
				break;
			}
		}

		for (int rowValue: rowValues) {
			for (int colValue: colValues) {
				if (rowValue == colValue) {
					intersect.add(colValue);
				}
			}
		}


		return intersect;
	}

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

					for (int j=0; j<columns; j++) {
						tiles[row][j] = rowTiles.get(j);
					}

					row += 1;
					rowTiles = new ArrayList<Tile>();
					continue;
				}

				String[] data = line.split("\t");
				if(data[0].equals("black")) {
					BlackTile tile = new BlackTile(tileSize);

					if(data[1].equals("full")) {
						tile.setValues(data[2], data[3]);
					}

					rowTiles.add(tile);
				}
				else if(data[0].equals("white")) {
					rowTiles.add(new WhiteTile(this));
				}

			}
			bufferedReader.close();
		} catch(FileNotFoundException ex) {
			System.out.println("Unable to find file.");
		} catch(IOException ex) {
			System.out.println("Unable to read file.");
		}
	}

	private Row getRow(int x, int y) {
		Row tileRow = new Row();

		for (Row row: rows) {
			if (row.containsTile(x, y)) {
				tileRow = row;
			}
		}

		return tileRow;
	}

	private Column getColumn(int x, int y) {
		Column tileCol = new Column();

		for (Column col: columns) {
			if (col.containsTile(x, y)) {
				tileCol = col;
			}
		}

		return tileCol;
	}

	public boolean validate(int value, int x, int y) {
		Row tileRow = getRow(x, y);
		Column tileCol = getColumn(x, y);

		if (tileRow.containsValue(value) && tileCol.containsValue(value) && !tileRow.hasEntry(value) && !tileCol.hasEntry(value)) {
			return true;
		}

		return false;
	}

	public void setEntered(int value, int x, int y) {
		Row tileRow = getRow(x, y);
		Column tileCol = getColumn(x, y);

		tileRow.addEntered(value);
		tileCol.addEntered(value);

		System.out.println(tileRow);
		System.out.println(tileCol);
	}
	
	public void save() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save Game");
		  
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("KAKURO files (*.kakuro)", "*.kakuro");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showSaveDialog(primaryStage);
        
        if(file != null){
        	try {
    			FileOutputStream f = new FileOutputStream(file);
    			ObjectOutputStream o = new ObjectOutputStream(f);

    			o.writeObject(this);

    			o.close();
    			f.close();

    		} catch (Exception e) {
    			e.printStackTrace();
    		}
        }
	}
	
	public void open() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Game");
		  
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(".TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(primaryStage);
        
        if(file != null){
        	this.readBoard(file);
        	this.createContent();
        }
	}
	
	private void openDefault() {
		File file = new File("board1.txt");
		this.readBoard(file);
	}

}
