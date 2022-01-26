import java.util.LinkedList;
import java.util.List;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;

import org.w3c.dom.css.RGBColor;

import BreezySwing.*;

public class UserInterface extends GBFrame implements KeyListener {

	private static LinkedList<JComponent> sizeGUI;
	private static LinkedList<JComponent> rcGUI;
	private static LinkedList<JComponent> boardGUI;

	private int height;
	private int width;
	private static JFrame frm;
	private static BoardSolver board;
	private static boolean ready;

	public UserInterface() {
		initSizeGUI();

		height = 0;
		width = 0;
		ready = false;
	}

	public static void main(String[] args) {
		frm = new UserInterface();
		frm.setTitle("Nonogram Solver");
		frm.setSize(500, 200);
		frm.setVisible(true);

		while(!ready) {
			System.out.println(ready);
			continue;
		}

		solve();
	}

	private static void solve() {
		long time = System.currentTimeMillis();

		int iterations = 0;
		final int MAX = 10; // max number of iterations
		final int STEPS = 6; // number of strategies one can solve with
		String text = "starter text";
	
		do {
			if(System.currentTimeMillis() - time > 2000) {
				text = board.solve(iterations % STEPS);
				((JTextArea) boardGUI.get(0)).setText(text);
				iterations++;
				System.out.println(iterations);
				time = System.currentTimeMillis();
				System.out.println("update");
			}
		} while(iterations < MAX && !text.equals("ERROR") && notSolved(text));

		System.out.println("DONE");
	}

	private static boolean notSolved(String str) {
		final String END_TAG = "\nSOLVED";
		return str.length() > END_TAG.length() && !str.substring(str.length()-END_TAG.length()).equals(END_TAG);
	}

	public void buttonClicked(JButton btn) {
		if (btn == sizeGUI.get(0)) {
			sizeGUIActions();

		} else if (btn == rcGUI.get(0)) {

			Parser parser = new Parser();

			// TODO: error checking
			int[][] rows = parser.parse(((JTextArea)(rcGUI.get(2))).getText());
			int[][] columns = parser.parse(((JTextArea)(rcGUI.get(4))).getText());
			
			hide(rcGUI); // TODO: put rcGUI on a panel and then make the panel invisible

			board = initBoardSolver(rows, columns);
			initBoardGUI(board);

			ready = true;
		}
	}

	private BoardSolver initBoardSolver(int[][] rows, int[][] columns) {
		int[][] columns5_1 = { { 2, 2 },
				{ 5 },
				{ 0 },
				{ 0 },
				{ 0 } };
		int[][] rows5_1 = { { 2 },
				{ 2 },
				{ 1 },
				{ 2 },
				{ 2 } };
		int[][] columns5_2 = { { 2, 2 },
				{ 5 },
				{ 0 },
				{ 0 },
				{ 0 } };
		int[][] rows5_2 = { { 2 },
				{ 2 },
				{ 1 },
				{ 2 },
				{ 2 } };

		int[][] columns5_3 = { { 0 },
				{ 0 },
				{ 0 },
				{ 5 },
				{ 2, 2 } };
		int[][] rows5_3 = { { 2 },
				{ 2 },
				{ 1 },
				{ 2 },
				{ 2 } };

		int[][] rows5_4 = { { 0 },
				{ 0 },
				{ 0 },
				{ 5 },
				{ 2, 2 } };
		int[][] columns5_4 = { { 2 },
				{ 2 },
				{ 1 },
				{ 2 },
				{ 2 } };

		int[][] rows5_5 = { { 3 },
				{ 2 },
				{ 2 },
				{ 2, 1 },
				{ 3 } };

		int[][] columns5_5 = { { 2 },
				{ 1, 2 },
				{ 1, 1 },
				{ 3 },
				{ 3 } };

		int[][] rows10_1 = {{1}, {1}, {1}, {1}, {1}, {1}, {1}, {1}, {1}, {1}};
		int[][] columns10_1 = {{10}, {0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}};

		return new BoardSolver(rows, columns);
	}

	private void hide(List<JComponent> thing) {
		for (JComponent part : thing) {
			part.setVisible(false);
		}
	}

	private void initSizeGUI() {
		sizeGUI = new LinkedList<JComponent>();

		// TODO: delete HEIGHT and WIDTH --> set to 0
		final int HEIGHT = 10;
		final int WIDTH = 10;
		sizeGUI.add(addButton("Set Size", 3, 2, 1, 1));
		sizeGUI.add(addIntegerField(HEIGHT, 1, 2, 1, 1)); // Height Field
		sizeGUI.add(addIntegerField(WIDTH, 2, 2, 1, 1)); // Width Field
		sizeGUI.add(addLabel("Height: ", 1, 1, 1, 1));
		sizeGUI.add(addLabel("Width: ", 2, 1, 1, 1));
	}

	private void initRCGUI() {
		rcGUI = new LinkedList<JComponent>();

		rcGUI.add(addButton("Done", 7, 2, 1, 1));
		// row stuff
		rcGUI.add(addLabel("Enter the Rows (use spaces per section and new lines per row)", 1, 1, 1, 1));
		rcGUI.add(addTextArea("", 2, 1, 2, 2));
		// columns stuff
		rcGUI.add(addLabel("Enter the Columns (use spaces per section and new lines per column)", 4, 1, 1, 1));
		rcGUI.add(addTextArea("", 5, 1, 2, 2));

		frm.setSize(500, 400);
	}

	private void initBoardGUI(BoardSolver board) {
		boardGUI = new LinkedList<JComponent>();

		JTextArea output = addTextArea("", 1, 1, 5, 5);
		boardGUI.add(output);
		output.setEditable(false);
		output.setText(board.printBoard());
		frm.setSize(width * 50, height * 50);
	}

	private void sizeGUIActions() {
		IntegerField fldHeight = (IntegerField) (sizeGUI.get(1));
		IntegerField fldWidth = (IntegerField) (sizeGUI.get(2));
		if (fldHeight.isValidNumber() && fldWidth.isValidNumber()) {
			height = fldHeight.getNumber();
			width = fldWidth.getNumber();

			hide(sizeGUI);
			initRCGUI();
		} else {
			messageBox("Invalid Input try again");
			if (!fldHeight.isValidNumber()) {
				fldHeight.select(0, fldHeight.getText().length());
				fldHeight.requestFocus();
			}
			if (!fldWidth.isValidNumber()) {
				fldWidth.select(0, fldWidth.getText().length());
				fldWidth.requestFocus();
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}
}
