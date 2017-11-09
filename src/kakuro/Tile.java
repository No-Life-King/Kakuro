/**
 * @author Bobby Palmer
 * @author Phil Smith
 */
package kakuro;

import javafx.scene.layout.StackPane;

public abstract class Tile extends StackPane {

	private String type;
	private int size;
	private int x;
	private int y;

	public void setType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getSize() {
		return size;
	}

	public void setx(int x) {
		this.x = x;
	}

	public void sety(int y) {
		this.y = y;
	}

	public int getx() {
		return x;
	}

	public int gety() {
		return y;
	}

}
