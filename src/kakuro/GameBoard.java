package kakuro;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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
	BorderPane root = new BorderPane();
	GridPane appContent = new GridPane();
	SidePanel sidePanel = new SidePanel(200, tileSize*10);

	public Parent createContent() {
		int boardSize = tiles.length;

		root.setPrefSize(tileSize*(boardSize+4), tileSize*(boardSize+1));

		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				Tile tile = tiles[i][j];
				tile.setx(i);
				tile.sety(j);
				appContent.add(tile, j, i);
			}
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

	public int[] validValues(int[] coords) {
		int i = coords[0];
		int j = coords[1];
		int k = 0;
		while(tiles[i][j].getType().equals("white")) {
			i--; k++;
		}
		
		int l = coords[0];
		while(tiles[l][j].getType().equals("white")) {
			l++; k++;
		}
		
		BlackTile blackTile = (BlackTile) tiles[i][j];
		int num1 = blackTile.getTop();
		
		int[] values = new int[k];
		for (int x = k-1; x >= 0; x--) {
			values[x] = k;
		}
		
		return values;
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
}
