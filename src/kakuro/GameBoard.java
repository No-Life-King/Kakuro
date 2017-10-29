package kakuro;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javafx.scene.Parent;
import javafx.scene.layout.Pane;

public class GameBoard {
	private Tile[][] tiles;
	private static final int tileSize = 60;
	SidePanel sidePanel;

	public Parent createContent() {
		int boardSize = tiles.length;

		Pane root = new Pane();

		root.setPrefSize(tileSize*(boardSize+3), tileSize*boardSize);

		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				Tile tile = tiles[i][j];
				tile.setTranslateX(j * tileSize);
				tile.setTranslateY(i * tileSize);
				tile.setx(i);
				tile.sety(j);
				root.getChildren().add(tile);
			}
		}
		sidePanel = new SidePanel();
		sidePanel.setTranslateX(boardSize*tileSize);
		root.getChildren().add(sidePanel);
		return root;
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
}
