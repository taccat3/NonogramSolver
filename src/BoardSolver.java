import java.util.ArrayList;
import java.util.Arrays;

public class BoardSolver {
	
	Board board;
	
	public static class Number {
		int val;
		boolean completed;
		
		public Number(int v) {
			val = v;
			completed = false;
		}
		
		public String toString() {
			return ("val: " + val + "\tcompleted? " + completed);
		}
	}

	public static class Board {
		State[][] answers;
		// [number of filled sections, sum of filled, sum defined space]
		int[][] metaRows; 
		int[][] metaColumns;
		Number[][] rows;
		Number[][] columns;
		
		int height;
		int width;
		
		public Board(int[][] r, int[][] c) {
			rows = intToNumber(r);
			columns = intToNumber(c);
			
			metaRows = getMeta(r);
			metaColumns = getMeta(c);
			
			height = rows.length;
			width = columns.length;
			
			answers = new State[height][width];
			
			for(int i = 0; i < height; i++) {
				for(int j = 0; j < width; j++) {
					answers[i][j] = State.EMPTY;
				}
			}	
		}

		private int[][] getMeta(int[][] array) {
			int[][] meta = new int[array.length][3];
			
			for(int i = 0; i < array.length; i++) {
				meta[i][0] = 0;
				meta[i][1] = 0;
				for(int num : array[i]) {
					meta[i][0] += 1;
					meta[i][1] += num;
				}
				meta[i][2] = meta[i][0] + meta[i][1] - 1;

			}
			
			return meta;
		}

		private Number[][] intToNumber(int[][] array) {

			int max = 0;
			for(int[] x : array) {
				if(x.length > max) {
					max = x.length;
				}
			}
			Number[][] newArray = new Number[array.length][max];

			for(int i = 0; i < array.length; i++) {
				// System.out.println(Arrays.toString(array[i]));
				for(int j = 0; j < array[i].length; j++) {
					newArray[i][j] = new Number(array[i][j]);
				}
			}
			
			return newArray;
		}
		
		public void fill(int i, int j) {
			answers[i][j] = State.FILLED;
		}
		
		public void setX(int i, int j) {
			answers[i][j] = State.__X__;
		}
		
		public void empty(int i, int j) {
			answers[i][j] = State.EMPTY;
		}
		
		public boolean isFilled(int i, int j) {
			return answers[i][j] == State.FILLED;
		}
		
		public boolean isX(int i, int j) {
			return answers[i][j] == State.__X__;
		}
		
		public boolean isEmpty(int i, int j) {
			return answers[i][j] == State.EMPTY;
		}
		
		public String toString() {
			String str = "";
			for(State[] row : answers) {
				str += Arrays.toString(row) + "\n";
			}
			return str;
		}
		
		public String printBoard() {
			String str = "";
			
			// get the longest row for indent purposes
			int max = 0;
			for(Number[] row : rows) {
				if(max < row.length) {
					max = row.length;
				}
			}
			
			for(int i = 0; i < max; i++) {
				str += "   ";
			}
			str += " _";
			
			for(int i = 0; i < width; i++) {
				str += "__";
			}
			str += "\n" + getColumns(max) + " _";
			for(int i = 0; i < max; i++) {
				str += "___";
			}
			for(int i = 0; i < width; i++) {
				str += "__";
			}
			int rowCount = 0;
			for(State[] row : answers) {
				str +=  "\n| " + getRow(rowCount) + "| ";
				rowCount++;
				for(State square : row) {
					str += (square == State.FILLED) ? "O " : "- ";
				}
				str+= "|";
			}
			
			str += "\n _";
			for(int i = 0; i < max; i++) {
				str += "___";
			}
			for(int i = 0; i < width; i++) {
				str += "__";
			}
			return str;
		}
		
		private String getColumns(int indent) {
			String str = "";
			for(int i = columns[0].length - 1; i >= 0; i--) { // height/2 is the greatest possible number of sections in a column
				for(int j = 0; j < indent; j++) {
					str += "   ";
				}
				str += "| ";
				for(Number[] column : columns) {
					if(column[i] == null) {
						str += "  ";
					} else {
						str += column[i].val + " ";
					}
				}
				str += "|\n";
			}
			
			return str;
		}
		
		private String getRow(int i) {
			String str = "";
			for(Number num : rows[i]) {
				if(num == null) {
					str += "  ";
				} else {
					str += num.val + " ";
				}
			}

			return str;
		}
	}
	
	public BoardSolver(int[][] r, int[][] c) {
		board = new Board(r, c);
	}
	
	public void solve() {
		System.out.println("\n\n\n\n\n\n\nSTART");
		long start = System.currentTimeMillis();
		long curr = System.currentTimeMillis();
		String str = "";
		int repeatCount = 0;
		fillInFullParts();
			String str1 = board.toString();
			if(!str.equals(str1)) {
				System.out.println(str1);
			}
			str = str1;

		while((curr - start) < 15 * 1000) {
			fillInEdges();
			String str2 = board.toString();
			if(!str.equals(str2)) {
				System.out.println(str2);
			} else {
				repeatCount++;
			}
			
			checkOverlaps();
			String str3 = board.toString();
			if(!str2.equals(str3)) {
				System.out.println(str3);
			} else {
				repeatCount++;
			}

			str = str3;

			// System.out.println(board.printBoard());
			System.out.println(repeatCount);

			if(!isSolved()) {
				System.out.println("INVALID BOARD");
			}  else {
				System.out.println("SOLVED");
			}

			if(repeatCount > 50) {
				System.out.println("BREAK OUT OF LOOP");
				break;
			}
		}
	}
	
	
	public void checkOverlaps() {
		checkOverlapsRows();
		checkOverlapsColumns();
	}
	
	public void checkOverlapsRows() {
		for(int i = 0; i < board.height; i++) {
			Number[] row = new Number[board.width];
			int index = 0;

			// fill in the row from left to right
			for(Number num : board.rows[i]) {
				if(index != 0) { // add spaces if not at the beginning
					index++;
				} 
				if (num != null) { // add the numbers
					for(int count = index; count < index + num.val; count++) {
						row[count] = num;
					}
					index += num.val;
				}	
			}

			index = board.width - 1;

			// check the row from right to left
			for(int j = board.rows[i].length - 1; j >= 0; j--) {
				if(index != board.width - 1) { // add spaces if not at the beginning (technically the end) 
					index--;
				} 
				if(board.rows[i][j] != null) {
					for(int count = index; count > index - board.rows[i][j].val; count--) {
						if (row[count] == board.rows[i][j]) { // is the exact same number
							board.fill(i, count);
						}
					}
					index -= board.rows[i][j].val;
				}	
			}
		}	
	}
	
	public void checkOverlapsColumns() {
		for(int i = 0; i < board.width; i++) {
			Number[] column = new Number[board.height];
			int index = 0;

			// fill in the row from left to right
			for(Number num : board.columns[i]) {
				if(index != 0) { // add spaces if not at the beginning
					index++;
				} 
				if (num != null) { // add the numbers
					for(int count = index; count < index + num.val; count++) {
						column[count] = num;
					}
					index += num.val;
				}	
			}

			index = board.height - 1;

			// check the row from right to left
			for(int j = board.columns[i].length - 1; j >= 0; j--) {
				if(index != board.height - 1) { // add spaces if not at the beginning (technically the end) 
					index--;
				} 
				if(board.columns[i][j] != null) {
					for(int count = index; count > index - board.columns[i][j].val; count--) {
						if (column[count] == board.columns[i][j]) { // is the exact same number
							board.fill(count, i);
						}
					}
					index -= board.columns[i][j].val;
				}	
			}
		}
	}
	
	
	private void fillInEdges() {
		fillInFirstRow();
		fillInFirstColumn();
		fillInLastRow();
		fillInLastColumn();
	}
	
	private void fillInFirstRow() {
		for(int i = 0; i < board.width; i++) {
			if(board.isFilled(0, i)) {
				int firstInColumn = board.columns[i][0].val;
				for(int j = 0; j < firstInColumn; j++) {
					board.fill(j, i);
				}
				if(firstInColumn < board.height) {
					board.setX(firstInColumn, i);
				}
				board.columns[i][0].completed = true;
			}
		}
	}
	
	private void fillInFirstColumn() {
		for(int i = 0; i < board.height; i++) {
			if(board.isFilled(i, 0)) {
				for(int j = 0; j < board.rows[i][0].val; j++) {
					board.fill(i, j);
				}
				if(board.rows[i][0].val < board.width) {
					board.setX(i, board.rows[i][0].val);
				}
				board.rows[i][0].completed = true;
			}
		}
	}
	
	private void fillInLastRow() {
		for(int i = 0; i < board.width; i++) {
			if(board.isFilled(board.height-1, i)) {
				if(board.columns[i][board.columns[i].length-1] == null) {
					break;
				}
				int indexOfLastNumInColumn = board.columns[i].length-1;
				for(int j = board.height - board.columns[i][indexOfLastNumInColumn].val; j < board.height; j++) {
					board.fill(j, i);
				}
				if(board.columns[i][0].val < board.height) {
					board.setX(board.height - board.columns[i][indexOfLastNumInColumn].val-1, i);
				}
				board.columns[i][indexOfLastNumInColumn].completed = true;
			}
		}
	}
	
	private void fillInLastColumn() {
		for(int i = 0; i < board.height; i++) {
			if(board.isFilled(i, board.width-1)) {
				int indexOfLastNumInRow = 0;
				for(int j = board.rows[i].length-1; j >= 0; j--) {
					if(board.rows[i][j] != null) {
						indexOfLastNumInRow = j;
						break;
					}
				}
				for(int j = board.width - board.rows[i][indexOfLastNumInRow].val; j < board.width; j++) {
					board.fill(i, j);
				}
				if(board.columns[i][0].val < board.width) {
					board.setX(i, board.width - board.rows[i][indexOfLastNumInRow].val-1);
				}
				board.columns[i][indexOfLastNumInRow].completed = true;
			}
		}
	}
	
	public void fillInFullParts() {
		int curr = 0;
		for(int j = 0; j < board.rows.length; j++) {
			if(isFullToStart(j, RC.ROW)) {
				int columnNum = 0;
				for(Number num : board.rows[j]) {
					for(int i = 0; i < num.val; i++) {
						board.fill(curr, columnNum++);
					}
					num.completed = true;
					if(columnNum < board.width) {
						board.setX(curr, columnNum++);
					}
				}
			}
			curr++;
		}
		
		curr = 0;

		for(int j = 0; j < board.columns.length; j++) {
			if(isFullToStart(j, RC.COLUMN)) {
				int rowNum = 0;
				for(Number num : board.columns[j]) {
					if(num == null) {
						break;
					}
					for(int i = 0; i < num.val; i++) {
						board.fill(rowNum++, curr);
					}
					num.completed = true;
					if(rowNum < board.height) {
						board.setX(rowNum++, curr);
					}
				}
			}
			curr++;
		}
	}
	
	private boolean isFullToStart(int i, RC rc) {
		switch(rc) {
		case ROW:
			return board.metaRows[i][2] == board.width;
		case COLUMN:
			return board.metaColumns[i][2] == board.height;
		}
		return false;
	}
	
	public boolean isSolved() {
		return (isSolvedRows() && isValidColumns());
	}
	
	private boolean isSolvedRows() {
		
		// check the rows are valid
		for(int i = 0; i < board.height; i++) { // row index
			ArrayList<Integer> nums = new ArrayList<Integer>();
			int num = 0;
			boolean onPart = false;
			for(int j = 0; j < board.width; j++) { // column index
				boolean isFilled = board.isFilled(i, j);
				if(onPart) {
					if(isFilled) {
						num++;
						if(j == board.width - 1) {
							nums.add(num);
						}
					} else if (!isFilled){
						nums.add(num);
						num = 0;
						onPart = false;
					}
				} else {
					if(isFilled) {
						num++;
						onPart = true;
					}
				}
			}
			
			if(nums.size() == 0) {
				nums.add(0);
			}
			
			// check if nums is the same as rows
			for(int n = 0; n < board.rows[i].length; n++) {
				if(board.rows[i][n].val != nums.get(n)) {
					return false;
				}
			}
		}
		
		
		return true;
	}
	
	private boolean isValidColumns() {
		
		// check the rows are valid
		for(int i = 0; i < board.width; i++) { // row index
			ArrayList<Integer> nums = new ArrayList<Integer>();
			int num = 0;
			boolean onPart = false;
			for(int j = 0; j < board.height; j++) { // column index
				boolean isFilled = board.isFilled(j,i);
				if(onPart) {
					if(isFilled) {
						num++;
						if(j == board.height - 1) {
							nums.add(num);
						}
					} else if (!isFilled){
						nums.add(num);
						num = 0;
						onPart = false;
					}
				} else {
					if(isFilled) {
						num++;
						onPart = true;
					}
				}
			}
			
			if(nums.size() == 0) {
				nums.add(0);
			}
			
			// check if nums is the same as rows
			for(int n = 0; n < board.columns[i].length; n++) {
				if(board.columns[i][n] == null) {
					break;
				}
				if(board.columns[i][n].val != nums.get(n)) {
					return false;
				}
			}
		}
		return true;
	}
	
	private int getLength(RC rc) {
		switch(rc) {
		case COLUMN:
			return board.height;
		case ROW:
			return board.width;
		}
		
		return -1;
	}
	
	enum RC {
		ROW,
		COLUMN
	}
	
	enum State {
		EMPTY,
		__X__,
		FILLED
	}
	
}
