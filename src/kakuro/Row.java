package kakuro;

import java.util.ArrayList;
import java.util.HashSet;

public class Row {
	private ArrayList<Tile> tiles = new ArrayList<Tile>();
	private HashSet<Integer> validValues = new HashSet<Integer>();
	private HashSet<Integer> enteredValues = new HashSet<Integer>();
	private int sum;

	public void add(Tile tile) {
		tiles.add(tile);
	}

	public int getSize() {
		return tiles.size();
	}

	public void setSum(int sum) {
		this.sum = sum;
	}

	public HashSet<Integer> getValidValues() {
		return validValues;
	}

	public boolean containsValue(int value) {
		for (int valid: validValues) {
			if (value == valid) {
				return true;
			}
		}

		return false;
	}

	public void addEntered(int value) {
		enteredValues.add(value);
		validValues.remove(value);
	}

	public boolean hasEntry(int value) {
		if (enteredValues.contains(value)) {
			return true;
		}

		return false;
	}

	public void calcValidValues() {
		int max = 9;
		int sum = 0;
		int pivot = tiles.size()-1;
		int[] row = new int[tiles.size()];

		for (int x = 0; x <= pivot; x++) {
			row[x] = x+1;
		}

		while (true) {
			for (int value: row) {
				sum += value;
			}

			if (sum < this.sum) {
				row[pivot] += 1;
			} else {
				break;
			}

			if (row[pivot] == max) {
				pivot--;
				max--;
			}

			sum = 0;
		}

		for (int value: row) {
			validValues.add(value);
		}

		if (pivot > 0) {
			while ((row[pivot] - row[pivot-1]) > 2) {
				row[pivot] -= 1;
				row[pivot-1] += 1;

				for (int value: row) {
					validValues.add(value);
				}
			}
		}

		if (pivot == 0) {
			while ((row[pivot+1] - row[0]) > 2) {
				row[pivot+1] -= 1;
				row[0] += 1;

				for (int value: row) {
					validValues.add(value);
				}
			}
		}

	}

	public boolean containsTile(int x, int y) {

		for (Tile rowTile: tiles) {
			if (x == rowTile.getx() && y == rowTile.gety()) {
				return true;
			}
		}

		return false;
	}

	@Override
	public String toString() {
		String row = sum + ": ";

		for (Tile tile: tiles) {
			row += tile.toString() + " ";
		}

		row += ": ";

		for (int value: validValues) {
			row += value + ", ";
		}

		return row;
	}

}
