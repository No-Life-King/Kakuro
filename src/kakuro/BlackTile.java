/**
 * @author Bobby Palmer
 * @author Phil Smith
 */
package kakuro;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class BlackTile extends Tile {

	String bottomLeft, topRight;
	int bottomLeftNum, topRightNum;
	Rectangle displayed;

	public BlackTile(int size) {
		setSize(size);
		displayed = new Rectangle(size, size);
		displayed.setFill(Color.BLACK);
		getChildren().addAll(displayed);
		setType("black");

	}

	public void setValues(String value1, String value2) {
		bottomLeft = value1;
		topRight = value2;
		bottomLeftNum = Integer.parseInt(value1);
		topRightNum = Integer.parseInt(value2);
		this.createText();
	}

	public int getTop() {
		return topRightNum;
	}

	public int getBottom() {
		return bottomLeftNum;
	}

	private void createText() {
		Line line = new Line(0, 0, getSize(), getSize());
		line.setStroke(Color.WHITE);
		getChildren().addAll(line);
		
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

	@Override
	public String toString() {
		return "Black: " + topRight + " " + bottomLeft;
	}

}
