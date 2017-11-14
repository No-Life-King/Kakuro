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
	private String value = "";
	public String loadBoardValue = "";

	public WhiteTile(GameBoard gameBoard) {
		g = gameBoard;
		setSize(gameBoard.getTileSize());
		Rectangle displayed = new Rectangle(getSize(), getSize());
		displayed.setFill(Color.WHITE);
		displayed.setStroke(Color.BLACK);
		setType("white");

        combo.setVisible(false);

        label.setVisible(true);
        label.setFont(new Font("Helvetica", 22));
        label.setText(value);

        this.setOnMouseEntered(event -> {
        	combo.setVisible(true);
        	this.setComboLabels();
        	});

        combo.showingProperty().addListener(observable -> {
            if (!combo.isShowing()) {
                combo.setVisible(false);
            }
        });

        combo.valueProperty().addListener(new ChangeListener<String>() {
            @SuppressWarnings("rawtypes")
			@Override public void changed(ObservableValue ov, String t, String t1) {
            	
            	valueChange(combo.getValue());

            }
        });

        this.setOnMouseExited(event -> {
        	 if (!combo.isShowing()) {
                 combo.setVisible(false);
             }
        });

		getChildren().clear();
		getChildren().addAll(displayed, label, combo);
	}
	
	public void valueChange(String newValue) {
		if(value.equals("") && !newValue.equals("")) {
			g.setEntered(Integer.parseInt(newValue), getx(), gety());
    		g.winner();
		}
		
		if(!value.equals("") && newValue.equals("")) {
			g.deleteEntry(Integer.parseInt(value), getx(), gety());
		}
		
		if(!newValue.equals("") && !value.equals("") && !newValue.equals(value)) {
			g.deleteEntry(Integer.parseInt(value), getx(), gety());
			g.setEntered(Integer.parseInt(newValue), getx(), gety());
    		g.winner();
		}
		
		value = newValue;
    	label.setText(newValue);
    	combo.getSelectionModel().select(value);
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
		this.valueChange(v);
	}

	public int getValue() {
		if(value.equals("")) {
			return 0;
		} else {
			int numValue = Integer.parseInt(value);
			return numValue;
		}
	}

	public void setEmpty() {
		this.valueChange("");
	}

	@Override
	public String toString() {
		return "White" + "\t"  + this.getValue();
	}

}
