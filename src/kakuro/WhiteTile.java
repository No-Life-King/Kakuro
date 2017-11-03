/**
 * @author Bobby Palmer
 * @author Phil Smith
 */
package kakuro;

import java.util.HashSet;

import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class WhiteTile extends Tile {

	private int value;
	private TextField number = new TextField();
	private HashSet<Integer> validEntries;
	private Rectangle displayed;
	private String userInput = "";
	private GameBoard g;

	public WhiteTile(GameBoard gameBoard) {
		g = gameBoard;
		setSize(gameBoard.getSize());
		Rectangle displayed = new Rectangle(getSize(), getSize());
		displayed.setFill(Color.WHITE);
		displayed.setStroke(Color.BLACK);
		setType("white");

		number.setMaxWidth(30);
		number.setFont(Font.font(15));
		number.setStyle("-fx-border-color: white;"
					  + "-fx-background-color: white;");
		number.setOnKeyReleased(new EventHandler<KeyEvent>() {
		      public void handle(KeyEvent released) {
		    	  userInput += released.getText();
		    	  try {
		    		  value = Integer.parseInt(userInput);
		    		  if (gameBoard.validate(value, getx(), gety())) {
		    			  gameBoard.setEntered(value, getx(), gety());
		    			  number.setText(Integer.toString(value));

		    		  } else {
		    			  number.clear();
		    		  }
		    	  } catch(NumberFormatException nfe) {
		    		  number.clear();
		    	  }
		      }
		});


		displayed.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				validEntries = gameBoard.validValues(getx(), gety());

				gameBoard.getSidePanel().displayValues(validEntries);
			}
		});

		getChildren().clear();
		getChildren().addAll(displayed, number);
	}

	public void setValue(int value) {
		number.setText(Integer.toString(value));
		this.value = value;
		g.setEntered(value, getx(), gety());
	}

	@Override
	public String toString() {
		return "(" + getx() + ", " + gety() + ")";
	}

}
