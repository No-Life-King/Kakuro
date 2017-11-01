/**
 * @author Bobby Palmer
 * @author Phil Smith
 */
package kakuro;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

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
