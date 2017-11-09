/**
 * @author Bobby Palmer
 * @author Phil Smith
 */
package kakuro;

import java.util.HashSet;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class WhiteTile extends Tile {

	private GameBoard g;
	private Label label = new Label();
	private ComboBox<String> combo = new ComboBox<>();
	private String value;

	public WhiteTile(GameBoard gameBoard) {
		g = gameBoard;
		setSize(gameBoard.getSize());
		Rectangle displayed = new Rectangle(getSize(), getSize());
		displayed.setFill(Color.WHITE);
		displayed.setStroke(Color.BLACK);
		setType("white");

		combo.getSelectionModel().select(0);
        combo.setVisible(false);

        label.setVisible(true);
        label.setFont(new Font("Times New Roman", 24));
        value = combo.getValue();
        label.setText(value);

        this.setOnMouseEntered(event -> {
        	combo.setVisible(true);
        	this.setComboLabels();
        	value = combo.getValue();
            label.setText(value);
        	});

        combo.showingProperty().addListener(observable -> {
            if (!combo.isShowing()) {
                combo.setVisible(false);
            }
            value = combo.getValue();
            label.setText(value);
        });

        combo.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String t1) {
            	if (t1 != "") {
            		g.setEntered(Integer.parseInt(t1), getx(), gety());
            		g.winner();
            	} else {
            		g.deleteEntry(Integer.parseInt(value), getx(), gety());
            	}
            }
        });


        this.setOnMouseExited(event -> {
        	 if (!combo.isShowing()) {
                 combo.setVisible(false);
             }
        	 value = combo.getValue();
             label.setText(value);
        });


		getChildren().clear();
		getChildren().addAll(displayed, label, combo);
	}

	private void setComboLabels() {
		HashSet<Integer> validValues = g.validValues(getx(), gety());
		String[] validEntries = new String[validValues.size()+1];
		validEntries[0] = "";

		int counter = 1;
		for(int i : validValues) {
			validEntries[counter] = "" + i;
			counter += 1;
		}

		combo.getItems().setAll(validEntries);
	}

	public void setValue(int value) {
		String v = ((Integer) value).toString();
		label.setText(v);
		this.value = v;
		combo.setValue(v);
	}

	public int getValue() {
		value = combo.getValue();
		try {
			int numValue = Integer.parseInt(value);
			return numValue;
		} catch(Exception e) {
			return 0;
		}
	}

	@Override
	public String toString() {
		return "(" + getx() + ", " + gety() + ")";
	}

}
