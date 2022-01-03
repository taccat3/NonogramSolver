public class Done {
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

	private static boolean isStripDone(Board board, int i, Board.RC rc) throws IllegalSolutionException {

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
					currLeft = getNextSection(board, rc, i, curr); // at the first part of a section
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

	private static int getNumSections(Board board, Board.RC rc, int i) {
		switch(rc) {
			case ROW:
				return board.metaRows[i][0];
			case COLUMN:
				return board.metaColumns[i][0];
			default:
				return -1;
		}
	}

	private static int getNextSection(Board board, Board.RC rc, int i, int curr) {
		switch(rc) {
			case ROW:
				return board.rows[i][curr].val - 1;
			case COLUMN:
				return board.columns[i][curr].val - 1;
			default:
				return -1;
		}
	}

	private static boolean alreadyDone(Board board, int i, Board.RC rc) {

		int isComplete = 0;
		switch(rc) {
			case ROW:
				isComplete = board.metaRows[i][3];
				break;
			case COLUMN: 
				isComplete = board.metaColumns[i][3];
				break;
		}

		if (isComplete == 1) {
			return true;
		}

		return false;
	}

	private static int getLength(Board board, Board.RC rc) {
		switch(rc) {
			case ROW:
				return board.width;
			case COLUMN:
				return board.height;
			default:
				return -1;
		}
	}
}
