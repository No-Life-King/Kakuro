/**
 * @author Bobby Palmer
 * @author Phil Smith
 */
package kakuro;

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

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			GameBoard gameBoard = new GameBoard();
			gameBoard.readBoard("board1.txt");
			Scene scene = new Scene(gameBoard.createContent());
			// scene.getStylesheets().add("application.css");
			primaryStage.setScene(scene);
			primaryStage.setTitle("Kakuro!");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
