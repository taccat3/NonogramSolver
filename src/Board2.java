
public class Board2 {

	public int height;
	public int width;
	
	public int rNums[][];
	public int cNums[][];
	
	public double rows[][]; // rows first
	public double columns[][]; // columns first
	
	public Board2(int h, int w) {
		rows = new double[h][w];
		columns = new double[w][h];
		
		height = h;
		width = w;
	}
	
	public void setNumbers(int[][] r, int[][] c) {
		rNums = r;
		cNums = c;
	}
	
	public double[] getRow(int index) {
		return rows[index];
	}
	
	public double[] getColumn(int index) {
		return columns[index];
	}
	
	public int[] getRowNums(int i) {
		return rNums[i];
	}
	
	public int[] getColumnNums(int i) {
		return cNums[i];
	}
	
	public double getSquare(int r, int c) {
		return rows[r][c];
	}
	
	public void setSquare(int r, int c, double val) {
		rows[r][c] = val;
		columns[c][r] = val;
	}
	
	public void fillSquare(int r, int c) {
		rows[r][c] = 10;
		columns[c][r] = 10;
	}
	
	public boolean isFull(RC rc, int i) {
		return (sum(rc, i) + spacing(rc, i) ==length(rc));
	}
	
	/**
	 * The probability that each square in a rc is filled 
	 * @param rc Row or Column
	 * @param i which row/column (index)
	 * @return value between 0-1 (0 being not filled, 1 being filled)
	 */
	public double probability(RC rc, int i) {
		return (sum(rc, i) + spacing(rc, i)) / length(rc);
	}
	
	/**
	 * the length of a row/column 
	 * @param rc Row or Column
	 * @return returns the height if given column, and the width if given row (negative if error)
	 */
	public int length(RC rc) {
		switch(rc) {
		case COLUMN:
			return height;
		case ROW:
			return width;
		}
		
		return -1;
	}

	/**
	 * Calcuates the spacing between sections of a given row/column
	 * @param rc Row or Column
	 * @param i which row/column (index)
	 * @return int representing the number of spaces there must be between sections (negative if error)
	 */
	public int spacing(RC rc, int i) {
		switch(rc) {
		case ROW:
			return rNums[i].length - 1;
		case COLUMN:
			return cNums[i].length - 1;
		}
		return -1;
	}
	
	/**
	 * Finds the total number of filled squares in a given row/column
	 * @param rc Row or Column
	 * @param i which row/column (index)
	 * @return the total number of filled squares in the given row/column
	 */
	public int sum(RC rc, int i) {
		int sum = 0;
		switch(rc) {
		case ROW:
			for(int n : rNums[i]) {
				sum += n;
			}
		case COLUMN:
			for(int n : cNums[i]) {
				sum += n;
			}
		}
		return sum;
	}
	
	public boolean isSolved() {
		return false;
	}
	
	public String toString() {
		String str = "";
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				str += columns[i][j] + "\t";
			}
			str += "\n";
		}
		return str;
	}
	
	/**
	 * Represents either a row or a column
	 */
	enum RC {
		ROW,
		COLUMN
	}
}
