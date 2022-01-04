import java.util.Arrays;

public class Overlaps extends Solving {
    
	public static Board checkOverlaps(Board board) throws IllegalSolutionException {
		// board = checkOverlapsStrips(board, Board.RC.ROW);
        // return checkOverlapsStrips(board, Board.RC.COLUMN);
        return checkOverlapsStrips(board, Board.RC.ROW);

	}

	private static Board checkOverlapsStrips(Board board, Board.RC rc) throws IllegalSolutionException {
		for (int i = 0; i < board.height; i++) {
			if(alreadyDone(board, i, rc)) {
				continue;
			}

			Number[] strip = new Number[getLength(board, rc)];
			int index = 0;

			// fill in the row from left to right
			for (int j = 0; j < getNumSections(board, rc, i); j++) {
				Number num = getSection(board, rc, i, j);

				// add spaces if NOT at the beginning
				if (index != 0) { 
					index++;
				}

				// try to add the number
				if (num != null) { // check if number exists
					int numNeeded = num.val;
					for (int count = index; count < index + num.val; count++) {
						// check if they fit
						if (board.isX(i, count)) {
							// remove filled in spots for the num in "row"
							for (int countReverse = count; countReverse >= index; countReverse--) {
								strip[countReverse] = null;
							}

							if (numNeeded > 0 && count >= strip.length - 1) {
								throw new IllegalSolutionException("Row cannot be filled");
							}

							// restart
							index = count;
							numNeeded = num.val;
							continue;
						} else {
							numNeeded--;
							strip[count] = num;
							if (numNeeded == 0) {
								index = count + 1;
								break;
							}
						}
					}
				}
			}

            // fill in row from right to left
            Number[] strip2 = checkRightToLeft(board, rc, i); // got locations of overlap in strip

            System.out.println("i=" + i);
            System.out.println(" strip: " + Arrays.toString(strip));
            System.out.println("strip2: " + Arrays.toString(strip2) + "\n");

            for(int n = 0; n < strip.length; n++) {
                if(strip[n] == strip2[n] && strip[n] != null) {
                    System.out.println("n=" + n);
                    board.fill(rc, i, n);
                }
            }

			index = board.width - 1;
		}

        return board;
	}

    private static Number[] checkRightToLeft(Board board, Board.RC rc, int i) throws IllegalSolutionException {
        
        int length = getLength(board, rc);
        Number[] strip = new Number[length];
        int index = length;

        for (int j = getNumSections(board, rc, i) - 1; j >= 0; j--) {
            if (index != getLength(board, rc) - 1) { // add spaces if not at the beginning (technically the end)
                index--;
            }

            // get the section number
            Number num = getSection(board, rc, i, j); 

            if (num != null) {
                int numNeeded = num.val;

                for (int count = index; count > index - num.val; count--) {
                    if(i==0)
                        System.out.println("count: " + count);


                    // check if they fit
                    if (board.isX(rc, i, count)) {
                        // remove filled in spots for the num in "row"
                        for (int countReverse = count; countReverse <= index; countReverse++) {
                            strip[countReverse] = null;
                        }

                        if (numNeeded > 0 && count <= 0) {
                            throw new IllegalSolutionException("Row cannot be filled");
                        }

                        // restart
                        index = count;
                        numNeeded = num.val;
                        continue;
                    } else {
                        numNeeded--;
							strip[count] = num;
							if (numNeeded == 0) {
								index = count - 1;
								break;
							}
                    }
                }
            }
        }

        return strip;
    }

}
