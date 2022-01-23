import java.util.ArrayList;

public class Done extends Solving{

	// fills in Done Sections
	
	public static Board fillDone(Board board) throws IllegalSolutionException {
		for (int i = 0; i < board.height; i++) {
			if (isStripDone(board, i, Board.RC.ROW)) {
				for (int j = 0; j < board.width; j++) {
					if (board.isEmpty(Board.RC.ROW, i, j)) {
						board.setX(i, j);
					}
				}
				for (Number num : board.rows[i]) {
					if (num != null) {
						num.completed = true;
					}

				}
				board.metaRows[i][3] = 1;
			}
		}
		for (int i = 0; i < board.width; i++) {
			if (isStripDone(board, i, Board.RC.COLUMN)) {
				for (int j = 0; j < board.height; j++) {
					if (board.isEmpty(Board.RC.COLUMN, i, j)) {
						board.setX(j, i);
					}
				}
				for (Number num : board.columns[i]) {
					if (num != null) {
						num.completed = true;
					}
				}
				board.metaColumns[i][3] = 1;
			}
		}

		return board;
	}

	public static boolean isStripDone(Board board, int i, Board.RC rc) throws IllegalSolutionException {

		if(alreadyDone(board, i, rc)) {
			return true;
		}

		boolean onNum = false;
		boolean space = false;
		boolean done = false;
		int curr = 0;
		int currLeft = 0;
		int numSections = getNumSections(board, rc, i);

		for (int j = 0; j < getLength(board, rc); j++) {
			boolean isFilled = board.isFilled(rc, i, j);

			if (done && isFilled) { // finished but found extra section
				return false;
			} else if (!space && isFilled) { // found part of a section
				onNum = true;
				if (currLeft == 0) {
					currLeft = getSection(board, rc, i, curr).val - 1; // at the first part of a section // TODO: possible error?
				} else {
					currLeft--; // in middle/end of section
				}
			} else if (onNum && board.isEmpty(rc, i, j)) { // on section but empty
				return false;
			} else if (space && !isFilled) { // on space
				space = false;
			} else if (onNum && !board.isX(rc, i, j)) { // if marked with an X incorrectly
				return false;
				// throw new IllegalSolutionException("Incorrectly marked with X at: (" + j + ",
				// " + i + ")");
			} else if (space && isFilled) { // if spacing was skipped
				throw new IllegalSolutionException("RC: " + rc + " \tSpacing was skipped at:\ti: " + i + "\tj: " + j);
			}

			// move onto next section if current is finished
			

			if (onNum && currLeft == 0) {
				onNum = false;
				space = (curr == numSections);
				curr++;
			}

			if (curr >= numSections) { // found all the sections
				done = true;
			}
		}

		return done;
	}

	// checks if board is solved

	public static boolean isSolved(Board board) {
		return (isSolvedRC(board, Board.RC.ROW) && isSolvedRC(board, Board.RC.COLUMN));
	}

	private static boolean isSolvedRC(Board board, Board.RC rc) {

		// check the rows are valid
		for (int i = 0; i < getNumStrips(board, rc); i++) { // strip index
			ArrayList<Integer> nums = new ArrayList<Integer>();
			int num = 0;
			boolean onPart = false;
			for (int j = 0; j < getLength(board, rc); j++) { // square index
				boolean isFilled = board.isFilled(rc, i, j);
				if (onPart) {
					if (isFilled) {
						num++;
						if (j == board.width - 1) {
							nums.add(num);
						}
					} else if (!isFilled) {
						nums.add(num);
						num = 0;
						onPart = false;
					}
				} else {
					if (isFilled) {
						num++;
						onPart = true;
					}
				}
			}

			if (nums.size() == 0) {
				nums.add(0);
			}

			// check if nums is the same as strip
			for (int n = 0; n < getStripSectionsNum(board, rc, i); n++) {
				Number stripNum = getStripNum(board, rc, i, n);
				if (stripNum.val != nums.get(n)) {
					return false;
				}
			}
		}

		return true;
	}

	private static int getStripSectionsNum(Board board, Board.RC rc, int i) {
		switch(rc) {
			case ROW:
				return board.metaRows[i][0];
			case COLUMN:
				return board.metaColumns[i][0];
			default:
				return -1;
		}
	}

	private static Number getStripNum(Board board, Board.RC rc, int i, int n) {
		switch(rc) {
			case ROW:
				return board.rows[i][n];
			case COLUMN:
				return board.columns[i][n];
			default:
				return null;
		}
	}
}
