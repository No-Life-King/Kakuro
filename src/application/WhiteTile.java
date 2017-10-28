/**
 * @author Bobby Palmer
 * @author Phil Smith
 */
package application;

import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class WhiteTile extends Tile {

	int value;

	public WhiteTile(int size) {
		setSize(size);
		Rectangle border = new Rectangle(size, size);
		border.setFill(Color.WHITE);
		border.setStroke(Color.BLACK);

		//setAlignment(Pos.CENTER);
		getChildren().addAll(border);
		setType("white");
	}

	@Override
	public String toString() {
		return "White";
	}

}
