package kakuro;

import java.util.HashSet;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class SidePanel extends StackPane {
	Rectangle displayed;
	Label validValues = new Label();

	public SidePanel(int width, int height) {
		Rectangle displayed = new Rectangle(width, height);
		displayed.setFill(Color.WHITE);
		this.getChildren().addAll(displayed);
	}

	public void displayValues(HashSet<Integer> values) {
		String displayString = "";

		for(int i : values) {
			displayString += Integer.toString(i) + "\n";
		}

		validValues.setText(displayString);

		validValues.setStyle(
				"-fx-font-family: \"Times New Roman\";" +
						"-fx-font-size: 40px;"	+
						"-fx-fill: rgb(0,0,0);"
				);

		if(!displayString.equals("")) {
			this.getChildren().removeAll(validValues);
			this.getChildren().addAll(validValues);
		}
	}
}
