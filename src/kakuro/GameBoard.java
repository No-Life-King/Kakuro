package kakuro;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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

public class GameBoard {

	private Tile[][] tiles;
	private static final int tileSize = 60;
	private BorderPane root = new BorderPane();
	private GridPane appContent = new GridPane();
	private SidePanel sidePanel;
	private ArrayList<Row> rows = new ArrayList<Row>();
	private ArrayList<Column> columns = new ArrayList<Column>();

	public Parent createContent() {
		int boardSize = tiles.length;

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

		for (Row row: rows) {
			row.calcValidValues();
		}

		root.setTop(this.generateMenu(root));
		root.setCenter(appContent);
		root.setRight(sidePanel);
		return root;
	}

	public MenuBar generateMenu(Pane root) {
		MenuBar menuBar = new MenuBar();

		Menu fileMenu = new Menu("File");
		MenuItem newMenuItem = new MenuItem("Open");
		MenuItem saveMenuItem = new MenuItem("Save");
		MenuItem exitMenuItem = new MenuItem("Exit");
		exitMenuItem.setOnAction(actionEvent -> Platform.exit());

		fileMenu.getItems().addAll(newMenuItem, saveMenuItem,
				new SeparatorMenuItem(), exitMenuItem);

		menuBar.getMenus().addAll(fileMenu);

		return menuBar;
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
			}
		}

		for (Column col: columns) {
			if (col.containsTile(x, y)) {
				HashSet<Integer> colValues = col.getValidValues();
				for (int num: colValues) {
					if (rowValues.contains(num)) {
						intersect.add(num);
					}
				}
			}
		}

		return intersect;
	}

	public void readBoard(String filename) {
		String line;
		ArrayList<Tile> rowTiles = new ArrayList<Tile>();

		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
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

	public void saveFile() {

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

	}
}
