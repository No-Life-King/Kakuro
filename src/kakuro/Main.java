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

	/**
	 * comment this whole file
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			GameBoard gameBoard = new GameBoard(primaryStage);
			Scene scene = new Scene(gameBoard.createContent());
			scene.getStylesheets().add(Main.class.getResource("Main.css").toExternalForm());
			primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Taidana Kakuro");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
