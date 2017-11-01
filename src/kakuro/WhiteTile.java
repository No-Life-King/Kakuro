/**
 * @author Bobby Palmer
 * @author Phil Smith
 */
package kakuro;

import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class WhiteTile extends Tile {

	int value;
	TextField number = new TextField();
	int[] validEntries;
	Rectangle displayed;

	public WhiteTile(GameBoard gameBoard) {
		setSize(gameBoard.getSize());
		Rectangle displayed = new Rectangle(getSize(), getSize());
		displayed.setFill(Color.WHITE);
		displayed.setStroke(Color.BLACK);

		//setAlignment(Pos.CENTER);
		getChildren().addAll(displayed);
		setType("white");



		number.setMaxWidth(30);
		number.setFont(Font.font(15));
		number.setStyle("-fx-border-color: white;"
					  + "-fx-background-color: white;");
		number.setOnKeyReleased(new EventHandler<KeyEvent>() {
		      public void handle(KeyEvent released) {
		        String character = released.getText();


		      }
		});
		getChildren().add(number);
		this.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				int[] coords = {getx(), gety()};
				validEntries = gameBoard.validValues(coords);
				StringBuilder values = new StringBuilder();

				for (int value: validEntries) {
					values.append(value + "\n");
				}

				gameBoard.getSidePanel().setLabel(values.toString());
			}
		});
	}

	@Override
	public String toString() {
		return "White";
	}

}
