package kakuro;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class SidePanel extends StackPane {
	Label validValues = new Label();

	public SidePanel() {
		Rectangle border;
		border = new Rectangle(240, 600);
		border.setFill(Color.WHITE);
		getChildren().addAll(border);
		validValues.setFont(Font.font(22));
		setLabel("");
		getChildren().add(validValues);
	}

	public void setLabel(String values) {
		validValues.setText(values);
	}
}
