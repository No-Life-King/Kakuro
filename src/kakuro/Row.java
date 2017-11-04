package kakuro;

import java.util.ArrayList;
import java.util.HashSet;

public class Row {
	private ArrayList<Tile> tiles = new ArrayList<Tile>();
	private HashSet<Integer> validValues = new HashSet<Integer>();
	private ArrayList<Integer> enteredValues = new ArrayList<Integer>();
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
		calcValidValues();
	}

	public boolean hasEntry(int value) {
		if (enteredValues.contains(value)) {
			return true;
		}

		return false;
	}

	private int sumEnteredValues() {
		int sum = 0;
		for (int value: enteredValues) {
			sum += value;
		}

		return sum;
	}

	public void calcValidValues() {
		int numTiles = tiles.size();
		int numEnteredValues = enteredValues.size();

		if (numTiles == numEnteredValues) {
			validValues.clear();
		} else if ((numTiles - numEnteredValues) == 1) {
			validValues.clear();
			validValues.add(this.sum - sumEnteredValues());
		} else {
			ArrayList<Integer[]> sums = getValidSums(numTiles-numEnteredValues);
			validValues.clear();
			
			for (Integer[] addends: sums) {
				for (int validValue: addends) {
					validValues.add(validValue);
				}
			}
		}

	}

	private ArrayList<Integer[]> getValidSums(int addends) {
		ArrayList<Integer[]> sums = new ArrayList<Integer[]>();
		int max = 9;
		int sum = 0;
		int pivot = tiles.size()-enteredValues.size()-1;
		Integer[] values = new Integer[addends];
		
		for (int x = 0; x < addends; x++) {
			values[x] = x+1;
		}

		while (true) {
			for (int value: values) {
				sum += value;
			}

			if (sum < this.sum - sumEnteredValues()) {
				values[pivot] += 1;
			} else {
				break;
			}

			if (values[pivot] == max) {
				pivot--;
				max--;
			}

			sum = 0;
		}

		sums.add(values.clone());

		if (pivot > 0) {
			while ((values[pivot] - values[pivot-1]) > 2) {
				values[pivot] -= 1;
				values[pivot-1] += 1;

				boolean valid = true;
				for (int value: values) {
					if (enteredValues.contains(value)) {
						valid = false;
					}
				}
				
				if (valid) {
					sums.add(values.clone());
				}
			}
		}
		
		for (Integer[] valueArray: sums) {
			for (int value: valueArray) {
				System.out.print(value + ", ");
			}
			System.out.println();
		}
		
		if (pivot == 0) {
			while ((values[pivot+1] - values[0]) > 2) {
				values[pivot+1] -= 1;
				values[0] += 1;

				boolean valid = true;
				for (int value: values) {
					if (enteredValues.contains(value)) {
						valid = false;
					}
				}
				
				if (valid) {
					sums.add(values.clone());
				}
			}
		}
		

		return sums;
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
