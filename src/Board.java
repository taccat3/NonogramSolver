import java.util.Arrays;

public class Board {
    State[][] answers;
    // [number of filled sections, sum of filled, sum defined space, 1 if done or 0 if
    // not]
    int[][] metaRows;
    int[][] metaColumns;
    Number[][] rows;
    Number[][] columns;

    int height;
    int width;

    public Board(int[][] r, int[][] c) {
        rows = intToNumber(r);
        columns = intToNumber(c);
    
        String str = "";
        for(Number num : columns[1]) {
            str += (num != null ? num.val : null) + ", ";
        }

        System.out.println("Column[1]: " + str);

        metaRows = getMeta(r);
        metaColumns = getMeta(c);

        height = rows.length;
        width = columns.length;

        answers = new State[height][width];

        for (int i = 0; i < height; i++) {

            for (int j = 0; j < width; j++) {
                answers[i][j] = State.EMPTY;
            }
        }
    }

    private int[][] getMeta(int[][] array) {
        int[][] meta = new int[array.length][4];

        for (int i = 0; i < array.length; i++) {
            meta[i][0] = 0;
            meta[i][1] = 0;
            for (int num : array[i]) {
                meta[i][0]++;
                meta[i][1] += num;
            }
            meta[i][2] = meta[i][0] + meta[i][1] - 1;
            meta[i][3] = 0;
        }

        return meta;
    }

    private Number[][] intToNumber(int[][] array) {

        int max = 0;
        for (int[] x : array) {
            if (x.length > max) {
                max = x.length;
            }
        }
        Number[][] newArray = new Number[array.length][max];

        for (int i = 0; i < array.length; i++) {
            // System.out.println(Arrays.toString(array[i]));
            for (int j = 0; j < array[i].length; j++) {
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

    public boolean isFilled(RC rc, int i, int j) {
        switch(rc) {
            case ROW:
                return answers[i][j] == State.FILLED;
            case COLUMN:
                return answers[j][i] == State.FILLED;
            default:
                return false;
        }
    }

    public boolean isX(RC rc, int i, int j) {
        switch(rc) {
            case ROW:
                return answers[i][j] == State.__X__;
            case COLUMN:
                return answers[j][i] == State.__X__;
            default:
                return false;
        }
    }

    public boolean isEmpty(RC rc, int i, int j) {
        switch(rc) {
            case ROW:
                return answers[i][j] == State.EMPTY;
            case COLUMN:
                return answers[j][i] == State.EMPTY;
            default:
                return false;
        }
    }

    // TODO: delete later?
    public boolean isFilled(int i, int j) {
        return answers[i][j] == State.FILLED;
    }

    public boolean isX(int i, int j) {
        return answers[i][j] == State.__X__;
    }

    public boolean isEmpty(int i, int j) {
        return answers[i][j] == State.EMPTY;
    }




    public String printBoard() {
        String str = "";

        // get the longest row for indent purposes
        int max = 0;
        for (Number[] row : rows) {
            if (max < row.length) {
                max = row.length;
            }
        }

        for (int i = 0; i < max; i++) {
            str += "   ";
        }
        str += " _";

        for (int i = 0; i < width; i++) {
            str += "__";
        }
        str += "\n" + getColumns(max) + " _";
        for (int i = 0; i < max; i++) {
            str += "___";
        }
        for (int i = 0; i < width; i++) {
            str += "__";
        }
        int rowCount = 0;
        for (State[] row : answers) {
            str += "\n| " + getRow(rowCount) + "| ";
            rowCount++;
            for (State square : row) {
                if(square == State.FILLED) {
                    str += "O ";
                } else if(square == State.EMPTY) {
                    str += "- ";
                } else {
                    str += "x ";
                }

                // str += (square == State.FILLED) ? "O " : "- ";
            }
            str += "|";
        }

        str += "\n _";
        for (int i = 0; i < max; i++) {
            str += "___";
        }
        for (int i = 0; i < width; i++) {
            str += "__";
        }

        str += "\n";

        return str;
    }

    private String getColumns(int indent) {
        String str = "";

        Number[][] columnReverse =  reverse(columns);

        for (int i = columns[0].length - 1; i >= 0; i--) { // height/2 is the greatest possible number of sections
                                                            // in a column
            for (int j = 0; j < indent; j++) {
                str += "   ";
            }
            str += "| ";
            for (Number[] column : columnReverse) {
                if (column[i] == null) {
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
        for (Number num : rows[i]) {
            if (num == null) {
                str += "  ";
            } else {
                str += num.val + " ";
            }
        }

        return str;
    }

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

    enum State {
        EMPTY,
        __X__,
        FILLED
    }

    enum RC {
        ROW,
        COLUMN
    }

}
