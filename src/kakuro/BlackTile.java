/**
 * @author Bobby Palmer
 * @author Phil Smith
 */
package kakuro;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

public class BlackTile extends Tile {

	String bottomLeft, topRight;
	int bottomLeftNum, topRightNum;
	Rectangle displayed;

	public BlackTile(int size) {
		setSize(size);
		displayed = new Rectangle(size, size);
		displayed.setFill(Color.BLACK);
		displayed.setStroke(Color.WHITE);
		//setAlignment(Pos.CENTER);
		getChildren().addAll(displayed);
		setType("black");

	}

	public void setValues(String value1, String value2) {
		bottomLeft = value1;
		topRight = value2;
		bottomLeftNum = Integer.parseInt(value1);
		topRightNum = Integer.parseInt(value2);
		getChildren().clear();
		Line line = new Line(0, 0, getSize(), getSize());
		line.setStroke(Color.WHITE);

		getChildren().addAll(displayed, line, createText());
	}

	public int getTop() {
		return topRightNum;
	}

	public int getBottom() {
		return bottomLeftNum;
	}

	private Text createText() {
		Text text = new Text();
		text.setBoundsType(TextBoundsType.VISUAL);	
		text.setText("     " + topRight + "\n" + bottomLeft);

		text.setStyle(
				"-fx-font-family: \"Times New Roman\";" +
						"-fx-font-size: 24px;"	+
						"-fx-fill: rgb(255,255,255);"
				);

		return text;
	}

	@Override
	public String toString() {
		return "Black: " + topRight + " " + bottomLeft;
	}

}
