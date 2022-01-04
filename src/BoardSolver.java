import java.util.ArrayList;
import java.util.Arrays;

// TODO: include threads or something? cuz this is the slowest thing ever (the for loops :/ )

// TODO: remove the checking if a row is filled to x it all out from the solve() function and put it within each method

public class BoardSolver {

	Board board;

	final int SECONDS = 1;

	private static Number[][] reverse(Number[][] original) {
		Number[][] newArray = new Number[original.length][original[0].length];
		for(int i = 0; i < original.length; i++) {
			int start = 0;
			for(int last = original[0].length - 1; last >= 0; last--) {
				newArray[i][last] = original[i][start];
				start++;
			}
		}

		return newArray;
	}

	public BoardSolver(int[][] r, int[][] c) {
		board = new Board(r, c);
	}

	public void solve() {
		System.out.println("\n\n\n\n\n\n\nSTART");

		try {
			System.out.println("fill in full strips");
			fillInFullParts();
			System.out.println(board.printBoard());

			// fillInEdges();
			System.out.println("fill in edges");
			board = Edges.fillInEdges(board);
			System.out.println(board.printBoard());

			
			System.out.println("fill in overlaps");
			// checkOverlaps(); // TODO: add in skip Xs
			board = Overlaps.checkOverlaps(board);
			System.out.println(board.printBoard());

			// fillDone();
			board = Done.fillDone(board);
			System.out.println("add x in Done Strips");
			System.out.println(board.printBoard());

			// // checkOverlaps();
			// System.out.println("fill in overlaps 2");
			// System.out.println(board.printBoard());

			if (!Done.isSolved(board)) {
				System.out.println("...");
			} else {
				System.out.println("SOLVED");
			}

			System.out.println(board.printBoard());

		} catch (IllegalSolutionException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	// method should check if a section can be filled in due to being limited by an x on 1 side
	private void fillInXLimited() {

	}

	private void checkOverlapsColumns2() {
		for (int i = 0; i < board.width; i++) {
			Number[] column = new Number[board.height];
			int index = 0;

			// fill in the row from left to right
			for (Number num : board.columns[i]) {
				if (index != 0) { // add spaces if not at the beginning
					index++;
				}
				if (num != null) { // add the numbers
					for (int count = index; count < index + num.val; count++) {
						column[count] = num;
					}
					index += num.val;
				}
			}

			index = board.height - 1;

			// check the row from right to left
			for (int j = board.columns[i].length - 1; j >= 0; j--) {
				if (index != board.height - 1) { // add spaces if not at the beginning (technically the end)
					index--;
				}
				if (board.columns[i][j] != null) {
					for (int count = index; count > index - board.columns[i][j].val; count--) {
						if (column[count] == board.columns[i][j]) { // is the exact same number
							board.fill(count, i);
						}
					}
					index -= board.columns[i][j].val;
				}
			}
		}
	}

	public void fillInFullParts() {
		int curr = 0;
		for (int j = 0; j < board.rows.length; j++) {
			if (isFullToStart(j, RC.ROW)) {
				int columnNum = 0;
				for (Number num : board.rows[j]) {
					for (int i = 0; i < num.val; i++) {
						board.fill(curr, columnNum++);
					}
					num.completed = true;
					if (columnNum < board.width) {
						board.setX(curr, columnNum++);
					}
				}
			}
			curr++;
		}

		curr = 0;

		for (int j = 0; j < board.columns.length; j++) {
			if (isFullToStart(j, RC.COLUMN)) {
				int rowNum = 0;
				for (Number num : board.columns[j]) {
					if (num == null) {
						break;
					}
					for (int i = 0; i < num.val; i++) {
						board.fill(rowNum++, curr);
					}
					num.completed = true;
					if (rowNum < board.height) {
						board.setX(rowNum++, curr);
					}
				}
			}
			curr++;
		}
	}

	private boolean isFullToStart(int i, RC rc) {
		switch (rc) {
			case ROW:
				return board.metaRows[i][2] == board.width;
			case COLUMN:
				return board.metaColumns[i][2] == board.height;
		}
		return false;
	}

	enum RC {
		ROW,
		COLUMN
	}
}
