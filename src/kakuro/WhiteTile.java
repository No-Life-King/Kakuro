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

/**
 * WhiteTile inherits from the abstract Tile class, which
 * inherits FX-node properties from Stack-Pane. Provides all the
 * functionality for the input of values onto boards, and modification/
 * retrieval of valid values from game-board value generator.
 */
public class WhiteTile extends Tile {

	private GameBoard g;
	private Label label = new Label();
	private ComboBox<String> combo = new ComboBox<>();
	private String value = "";
	public String loadBoardValue = "";

	/**
	 * Constructor sets up all the basic GUI elements and places them on
	 * the game-board. Event handlers are also attached here, mainly
	 * through lambda functions.
	 * @param gameBoard: Reference to g/b for valid value retrieval.
	 */
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
	
	/**
	 * Every-time the value entry combo-box is changed the change is checked
	 * and whenever necessary modifies the the valid value calculation in g/b.
	 * @param newValue
	 */
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
	}

	/**
	 * Method retrieves the valid values from the g/b and adds them to our display
	 * mechanism, a combo-box.
	 */
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

	/**
	 * Allows a value to be set from other parts of the program, and displayed.
	 * @param value: A String.
	 */
	public void setValue(int value) {
		String v = ((Integer) value).toString();
		this.valueChange(v);
	}

	/**
	 * Allows other parts of the program to retrieve the tiles numerical value.
	 * @return Integer representation of the value.
	 */
	public int getValue() {
		if(value.equals("")) {
			return 0;
		} else {
			int numValue = Integer.parseInt(value);
			return numValue;
		}
	}

	/**
	 * Causes the value to be set to empty.
	 */
	public void setEmpty() {
		this.valueChange("");
	}

	@Override
	public String toString() {
		return "White" + "\t"  + this.getValue();
	}

}
