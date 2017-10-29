/**
 * @author Bobby Palmer
 * @author Phil Smith
 */
package kakuro;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class WhiteTile extends Tile {

	int value;
	TextField number = new TextField();

	public WhiteTile(GameBoard gameBoard) {
		setSize(gameBoard.getSize());
		Rectangle border = new Rectangle(getSize(), getSize());
		border.setFill(Color.WHITE);
		border.setStroke(Color.BLACK);

		//setAlignment(Pos.CENTER);
		getChildren().addAll(border);
		setType("white");

		number.setMaxWidth(30);
		number.setFont(Font.font(15));
		number.setStyle("-fx-border-color: white;"
					  + "-fx-background-color: white;");
		number.setOnKeyReleased(new EventHandler<KeyEvent>() {
		      public void handle(KeyEvent released) {
		        String character = released.getText();
		        if (!gameBoard.isValidNumber(character)) {
		        	number.setText("");
		        }

		      }
		});
		getChildren().add(number);
	}

	@Override
	public String toString() {
		return "White";
	}

}
