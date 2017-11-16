/**
 * @author Bobby Palmer
 * @author Phil Smith
 */
package kakuro;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

/**
 * Java-FX class Application is extended, enabling a start method
 * which initializes a stage (essentially blank window pane).
 */
public class Main extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Inherited from Application; initializes the stage, and the game-board
	 * in which the majority of our code is controlled and executed.
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			// Initializes game-board and sets as "scene"
			GameBoard gameBoard = new GameBoard(primaryStage);
			Scene scene = new Scene(gameBoard.createContent());
			
			// Adds CSS styling to the the entire application.
			scene.getStylesheets().add(Main.class.getResource("Main.css").toExternalForm());
			
			// Sets window characteristics.
			primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Taidana Kakuro");
			
			// Displays the application.
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
