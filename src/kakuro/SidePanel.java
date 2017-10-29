package kakuro;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class SidePanel extends StackPane {
	Label validValues = new Label();
	
	public SidePanel() {
		Rectangle border;
		border = new Rectangle(240, 600);
		border.setFill(Color.BLACK);
		getChildren().addAll(border);

		getChildren().addAll(validValues);
	}
	
	public void setLabel(String values) {
		
		validValues.setText("hello");
	}
}
