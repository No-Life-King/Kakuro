/**
 * @author Bobby Palmer
 * @author Phil Smith
 */
package kakuro;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			GameBoard gameBoard = new GameBoard(primaryStage);
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
