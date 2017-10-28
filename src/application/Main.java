/**
 * @author Bobby Palmer
 * @author Phil Smith
 */
package application;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class Main extends Application {

	private Tile[][] tiles;
	private static final int tileSize = 75;

	private Parent createContent() {
		int boardSize = tiles.length;

		Pane root = new Pane();

		root.setPrefSize(tileSize*boardSize, tileSize*boardSize);

		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				Tile tile = tiles[i][j];
				tile.setTranslateX(j * tileSize);
				tile.setTranslateY(i * tileSize);

				root.getChildren().add(tile);
			}
		}

		return root;
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			this.savedBoardReader("Resources/board1.txt");
			primaryStage.setScene(new Scene(createContent()));
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void savedBoardReader(String filename) {
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
					rowTiles.add(new WhiteTile(tileSize));
				}

			}

			bufferedReader.close();
		} catch(FileNotFoundException ex) {
			System.out.println("Unable to find file.");
		} catch(IOException ex) {
			System.out.println("Unable to read file.");
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

}
