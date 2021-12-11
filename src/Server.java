import java.util.ArrayList;

public class Server {

	Board2 board;
	
	int height;
	int width;
	ArrayList<ArrayList<Integer>> rows = new ArrayList<ArrayList<Integer>>();;
	ArrayList<ArrayList<Integer>> columns = new ArrayList<ArrayList<Integer>>();;
	
	public void setHeightWidth(int h, int w) {
		height = h;
		width = w;
		board = new Board2(h, w);
	}
	
	public int getColumnNums() {
		return board.cNums[0][0];
	}
	
	public double[][] getRows() {
		return board.columns;
	}
	
	public boolean process(String str, Board2.RC rc) {
		
		// TODO: error checking (return false if error)
		
		ArrayList<Integer> row = new ArrayList<Integer>(); // TODO: rename this variable
		
		for(String row2 : str.trim().split("\n")) {
			for(String num : row2.split(" ")) {
				row.add(Integer.parseInt(num.trim())); // TODO: this will cause an error if not a num
			}
			switch(rc) {
			case COLUMN:
				columns.add(row);
			case ROW:
				rows.add(row);
			}
			row = new ArrayList<Integer>();
		}
		
		return true;
	}
	
	public void solveBoard() {
		for(int i = 0; i < height; i++) {
			if(board.isFull(Board2.RC.ROW, i)) {
				int start = 0;
				for(int num : board.getRowNums(i)) {
					for(int j = start; j < num + start; j++) {
						board.fillSquare(i, j);
					}
					start += num;
				}
			}
		}
		
		System.out.println(board.toString());
	}
	
	public int getBoardState() {
		if(rows.size() > 0) {
			if(columns.size() > 0) {
				if(board.isSolved()) {
					return 3;
				}
				return 2;
			}
			return 1;
		}
		
		return 0;
	}
	
	public String print() {
		return board.toString();
	}
}