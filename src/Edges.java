import Board.RC;

public class Edges {
    
    Board board;

	public static Board fillInEdges(Board board) {
		board = fillInFirstRow(board);
		board = fillInFirstColumn(board);
		board = fillInLastRow(board);
		return fillInLastColumn(board);
	}

	private static Board fillInFirstRow(Board board) {
		for (int i = 0; i < board.width; i++) {
			if (board.isFilled(0, i)) {
				int firstInColumn = board.columns[i][0].val;
				for (int j = 0; j < firstInColumn; j++) {
					board.fill(j, i);
				}
				if (firstInColumn < board.height) {
					board.setX(firstInColumn, i);
				}
				board.columns[i][0].completed = true;
			}
		}

        return board;
	}

	private static Board fillInFirstColumn(Board board) {
		for (int i = 0; i < board.height; i++) {
			if (board.isFilled(i, 0)) {
				for (int j = 0; j < board.rows[i][0].val; j++) {
					board.fill(i, j);
				}
				if (board.rows[i][0].val < board.width) {
					board.setX(i, board.rows[i][0].val);
				}
				board.rows[i][0].completed = true;
			}
		}

        return board;
	}

	private static Board fillInLastRow(Board board) {
		// i is the column index
		for (int i = 0; i < board.width; i++) {

			// if the square is filled
			if (board.isFilled(board.height - 1, i)) {

				// get the last section in the column
				int indexOfLastNumInColumn = 0;
				for (int k = board.columns[i].length - 1; k > 0; k--) {
					if(board.columns[i][k] != null) {
						indexOfLastNumInColumn = k;
						break;
					}
				}

				for (int j = board.height - board.columns[i][indexOfLastNumInColumn].val; j < board.height; j++) {
					board.fill(j, i);
				}
				if (board.columns[i][0].val < board.height) {
					board.setX(board.height - board.columns[i][indexOfLastNumInColumn].val - 1, i);
				}
				board.columns[i][indexOfLastNumInColumn].completed = true;
			}
		}

        return board;
	}

	private static Board fillInLastColumn(Board board) {
		// check each index in the last column (0-[height-1])
		for (int i = 0; i < board.height; i++) {

			// check if index is filled, i is row, board.width-1 = last column
			if (board.isFilled(i, board.width - 1)) {
				int indexOfLastNumInRow = 0;

				// get the last section num in the row that will be filled in
				for (int j = board.rows[i].length - 1; j >= 0; j--) {
					if (board.rows[i][j] != null) {
						indexOfLastNumInRow = j;
						break;
					}
				}

				for(int j = board.width - 1; j >= board.width - board.rows[i][indexOfLastNumInRow].val; j--) {
					if(j == board.width - board.rows[i][indexOfLastNumInRow].val && j > 0 && board.isEmpty(Board.RC.ROW, i, j)) {
						board.setX(i, j);
						break;
					}
					board.fill(i, j);
				}
				// for (int j = board.width - board.rows[i][indexOfLastNumInRow].val; j < board.width; j++) {
				// 	board.fill(i, j);
				// }
				// if (board.columns[i][0].val < board.width) {
				// 	board.setX(i, board.width - board.rows[i][indexOfLastNumInRow].val - 1);
				// }
				board.rows[i][indexOfLastNumInRow].completed = true;
			}
		}

        return board;
	}

}
