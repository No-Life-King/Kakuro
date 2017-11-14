package kakuro;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * This class groups all of the white tiles from a single row (or column) so that
 * tiles from the same row or column are cognizant of changes in each other and
 * the valid values for that row or column may be recalculated on the fly.
 * @author Phil Smith
 * @author Bobby Palmer
 */
public class Row {

	private ArrayList<Tile> tiles = new ArrayList<Tile>();
	private HashSet<Integer> validValues = new HashSet<Integer>();
	private ArrayList<Integer> enteredValues = new ArrayList<Integer>();
	private int sum;

	/**
	 * Adds a tile to this row during board creation.
	 * @param tile The tile to be added.
	 */
	public void add(Tile tile) {
		tiles.add(tile);
	}

	/**
	 * Gets the number of tiles in this row.
	 * @return The number of tiles this row contains.
	 */
	public int getSize() {
		return tiles.size();
	}

	/**
	 * Sets the sum that is present in the black tile directly above or to the
	 * left of this row or column.
	 * @param sum The target number that the values in this row must sum to.
	 */
	public void setSum(int sum) {
		this.sum = sum;
	}

	/**
	 * Gets the valid values that may be entered into this row.
	 * @return A hash set of the valid values.
	 */
	public HashSet<Integer> getValidValues() {
		return validValues;
	}

	/**
	 * Set a value as entered within this row or column.
	 * @param value The value that has been selected by the user for a white tile.
	 */
	public void addEntered(int value) {
		enteredValues.add(value);

		// recalculate the values to get a new set of valid values for the row
		calcValidValues();
	}

	/**
	 * Get the sum of the values that have been entered into this row or column
	 * so that the remaining sum may be calculated.
	 * @return The sum of the entered values.
	 */
	private int sumEnteredValues() {
		int sum = 0;

		// add 'em all up
		for (int value: enteredValues) {
			sum += value;
		}

		return sum;
	}

	/**
	 * Calculate all of the valid values for this row or column as an
	 * arbitrary number of addends that sum to the sum value of this
	 * row or column and exclude values and tiles that have already
	 * been entered.
	 */
	public void calcValidValues() {

		int numTiles = tiles.size();
		int numEnteredValues = enteredValues.size();
		int addends = numTiles - numEnteredValues;

		// skip this during board creation until all of the rows and
		// columns have been populated
		if (addends >= 0) {

			// if the row is full, return an empty set
			if (numTiles == numEnteredValues) {
				validValues.clear();
			} else if (addends == 1) {
				validValues.clear();

				// if one tile is left, its value must be the remaining sum
				validValues.add(this.sum - sumEnteredValues());
			} else if (addends == 9) {

				// if the row is the maximum size of 9, the set is simply
				// always {1, 2, 3, 4, 5, 6, 7, 8, 9}
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

				// clear all the values and recalculate valid values
				validValues.clear();

				Integer[] values = new Integer[addends];
				int sum = 0;
				int pivot = addends-1;

				// start the possible solution off as {1, 2, 3, 4, ...}
				for (int x=0; x<addends; x++) {
					values[x] = x+1;
				}

				// generate all the permutations between the minimum possible sum and
				// maximum possible sum and see if they add up to the target sum
				do  {
					sum = 0;

					// sum the values in the potential generated solution array
					for (int x=0; x<addends; x++) {
						sum += values[x];
					}

					// the solution is only valid if it has no duplicates and doesn't
					// contain a value that has already been entered
					if (sum == this.sum - sumEnteredValues() && !arrayContainsEntry(values) && !arrayHasDuplicates(values)) {
						for (int value: values) {
							validValues.add(value);
						}

					}

					// make the array increment like a number system
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

	/**
	 * Check if the specified array contains duplicate values.
	 * @param values The array of values.
	 * @return True if there are two or more occurrences of a value in the array -
	 * otherwise false.
	 */
	private boolean arrayHasDuplicates(Integer[] values) {
		HashSet<Integer> set = new HashSet<Integer>();

		for (int value: values) {
			set.add(value);
		}

		// if all the values in the array are unique, the set size will equal
		// the array size
		if (set.size() == values.length) {
			return false;
		}

		return true;
	}

	/**
	 * Check to see if the potential solution set contains a value that has
	 * already been entered.
	 * @param array An array of potential valid values.
	 * @return True if a value of the array has already been entered in this
	 * row or column - otherwise false.
	 */
	private boolean arrayContainsEntry(Integer[] array) {

		for (int i: array) {
			if (enteredValues.contains(i)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Checks to see if this row or column contains the tile at position (x, y).
	 * @param x The vertical distance, in squares, from the top-left square.
	 * @param y The horizontal distance, in squares, from the top-left square.
	 * 			It's a weird coordinate scheme, I know.
	 * @return True if this row or column is keeping track of the specified tile.
	 * 	       Otherwise, false.
	 */
	public boolean containsTile(int x, int y) {

		for (Tile rowTile: tiles) {
			if (x == rowTile.getx() && y == rowTile.gety()) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Sets a previously entered value as un-entered and recalculates the valid
	 * values for the row.
	 * @param value The value to remove from this row.
	 */
	public void deleteEntered(int value) {
		enteredValues.remove((Integer) value);
		calcValidValues();
	}

	/**
	 * Returns a string representation of this row or column. Prints the sum,
	 * the tiles, and the valid values of the entire row. Used for debugging.
	 */
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
