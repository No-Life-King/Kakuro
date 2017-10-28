/**
 * @author Bobby Palmer
 * @author Phil Smith
 */
package application;

import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public abstract class Tile extends StackPane {
	private String type;
	private int size;

	public void setType(String type) {
		this.type = type;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getSize() {
		return size;
	}


}
