public abstract class Solving {

	protected static int getLength(Board board, Board.RC rc) {
		switch(rc) {
			case ROW:
				return board.width;
			case COLUMN:
				return board.height;
			default:
				return -1;
		}
	}

	protected static int getNumStrips(Board board, Board.RC rc) {
		switch(rc) {
			case ROW:
				return board.height;
			case COLUMN:
				return board.width;
			default:
				return -1;
		}
	}

    protected static Number getSection(Board board, Board.RC rc, int i, int curr) {
		switch(rc) {
			case ROW:
				return board.rows[i][curr];
			case COLUMN:
				return board.columns[i][curr];
			default:
				return null;
		}
	}

    protected static boolean alreadyDone(Board board, int i, Board.RC rc) {

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

    protected static int getNumSections(Board board, Board.RC rc, int i) {
		switch(rc) {
			case ROW:
				return board.metaRows[i][0];
			case COLUMN:
				return board.metaColumns[i][0];
			default:
				return -1;
		}
	}

}
