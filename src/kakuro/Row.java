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
		int addends = numTiles - numEnteredValues;

		if (addends > 0) {

			if (numTiles == numEnteredValues) {
				validValues.clear();
			} else if (addends == 1) {
				validValues.clear();
				validValues.add(this.sum - sumEnteredValues());
			} else if (addends == 9) {
				validValues.clear();
				validValues.add(1);
				validValues.add(2);
				validValues.add(3);
				validValues.add(4);
				validValues.add(5);
				validValues.add(6);
				validValues.add(7);
				validValues.add(8);
				validValues.add(9);
			} else {
				validValues.clear();

				Integer[] values = new Integer[addends];
				int sum = 0;
				int pivot = addends-1;

				for (int x=0; x<addends; x++) {
					values[x] = x+1;

				}

				do  {
					sum = 0;

					for (int x=0; x<addends; x++) {
						sum += values[x];
					}
					if (sum == this.sum - sumEnteredValues() && !arrayContainsEntry(values) && !arrayHasDuplicates(values)) {
						for (int value: values) {
							validValues.add(value);
						}

					}

					if (values[pivot] < 9) {
						values[pivot] += 1;
					} else {
						while (values[pivot] == 9) {
							values[pivot] = 1;
							pivot--;

							if (pivot == -1) {
								pivot++;
								break;
							}
						}
						values[pivot] += 1;
						pivot = addends-1;
					}

				} while (sum < addends*9);
			}
		}
	}

	private boolean arrayHasDuplicates(Integer[] values) {
		HashSet<Integer> set = new HashSet<Integer>();

		for (int value: values) {
			set.add(value);
		}

		if (set.size() == values.length) {
			return false;
		}

		return true;
	}

	/*
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

			// print all permutations if necessary
			for (Integer[] values: sums) {
				for (int value: values) {
					System.out.print(value + "\n");
				}
				System.out.println();
			}

			for (Integer[] addends: sums) {
				for (int validValue: addends) {
					validValues.add(validValue);
				}
			}
		}

	}
	*/

	private ArrayList<Integer[]> getValidSums(int addends) {
		ArrayList<Integer[]> sums = new ArrayList<Integer[]>();
		int addendCap = 9;
		int maxSum = 0;
		int goal = this.sum - sumEnteredValues();

		for (int x = 0; x < addends; x++) {
			maxSum += addendCap-x;
		}

		while (maxSum >= goal) {
			ArrayList<Integer[]> sumsWithThisMax = getSums(goal, addends, addendCap);

			for (Integer[] array: sumsWithThisMax) {
				if (!arrayContainsEntry(array)) {
					sums.add(array.clone());
				}
			}

			maxSum = 0;
			addendCap--;
			for (int x = 0; x < addends; x++) {
				maxSum += addendCap-x;
			}
		}

		return sums;
	}

	private ArrayList<Integer[]> getSums(int goal, int addends, int addendCap) {
		int pivot = addends-1;
		Integer[] values = new Integer[addends];
		ArrayList<Integer[]> sums = new ArrayList<Integer[]>();
		int sum = addends*(addends+1)/2;
		int max = addendCap;

		for (int x=0; x<addends; x++) {
			values[x] = x+1;
		}

		while (sum < goal) {
			if (values[pivot] == max) {
				pivot--;
				max--;
			}

			values[pivot] += 1;
			sum++;
		}

		sums.add(values.clone());

		int gap = findGap(values);

		if (gap < addends) {
			while (values[gap+1] - values[gap] > 2) {
				values[gap] += 1;
				values[gap+1] -= 1;
				sums.add(values.clone());
			}
		}

		return sums;
	}

	private int findGap(Integer[] values) {
		int x=0;
		while (x < values.length-1) {
			if (values[x+1] - values[x] > 2) {
				return x;
			}
			x++;
		}

		x++;
		return x;
	}

	private boolean arrayContainsEntry(Integer[] array) {

		for (int i: array) {
			if (enteredValues.contains(i)) {
				return true;
			}
		}

		return false;
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

	public void deleteEntered(int value) {
		enteredValues.remove((Integer) value);
		this.calcValidValues();

	}

}
