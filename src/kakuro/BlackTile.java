package kakuro;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * This class represents a black Kakuro puzzle square that may contain one sum,
 * two sums, or may be blank.
 * @author Bobby Palmer
 * @author Phil Smith
 *
 */
public class BlackTile extends Tile {

	private String bottomLeft, topRight;
	private int bottomLeftNum, topRightNum;
	private Rectangle displayed;

	/**
	 * This constructor creates a new black tile with the width and height of 'size'.
	 * @param size The size in pixels of the tile.
	 */
	public BlackTile(int size) {
		setSize(size);

		// draw a rectangle
		displayed = new Rectangle(size, size);

		// paint it black
		displayed.setFill(Color.BLACK);

		// add it to the game board
		getChildren().addAll(displayed);

		setType("black");
	}

	/**
	 * Set any values of the black tile. If the value is 0, it will not be displayed.
	 * @param value1 The value that should be set in the bottom left-hand corner of the
	 * 					black tile. The column below this tile must sum to the value
	 * 					that this is set to.
	 * @param value2 The value that should be set in the top right-hand corner of the
	 * 					black tile. The row right of this must sum to the value that
	 * 					this is set to.
	 */
	public void setValues(String value1, String value2) {
		bottomLeft = value1;
		topRight = value2;
		bottomLeftNum = Integer.parseInt(value1);
		topRightNum = Integer.parseInt(value2);

		// create the text that should be displayed on this tile
		this.createText();
	}

	/**
	 * Retrieve the row sum value.
	 * @return The value in the top right corner of this tile.
	 */
	public int getTop() {
		return topRightNum;
	}

	/**
	 * Retrieve the column sum value.
	 * @return The value in the bottom let corner of this tile.
	 */
	public int getBottom() {
		return bottomLeftNum;
	}

	/**
	 * comment
	 */
	private void createText() {
		if(topRightNum != 0 || bottomLeftNum != 0) {
			Line line = new Line(0, 0, getSize(), getSize());
			line.setStroke(Color.WHITE);
			getChildren().addAll(line);
		}

		if(topRightNum != 0) {
			Text tR = new Text(topRight);
			tR.setTranslateX(14.00);
			tR.setTranslateY(-14.00);

			tR.setStyle(
					"-fx-font-family: \"Times New Roman\";" +
							"-fx-font-size: 24px;"	+
							"-fx-fill: rgb(255,255,255);"
					);

			getChildren().addAll(tR);
		}

		if(bottomLeftNum != 0) {
			Text bL = new Text(bottomLeft);
			bL.setTranslateX(-14.00);
			bL.setTranslateY(14.00);

			bL.setStyle(
					"-fx-font-family: \"Times New Roman\";" +
							"-fx-font-size: 24px;"	+
							"-fx-fill: rgb(255,255,255);"
					);

			getChildren().addAll(bL);
		}
	}

	/**
	 * Print the tile type along with its values.
	 */
	@Override
	public String toString() {
		return "Black" + "\t" + bottomLeft + "\t" + topRight;
	}

}
