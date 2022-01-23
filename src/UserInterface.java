import java.util.LinkedList;
import java.util.List;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Color;
import javax.swing.*;
import BreezySwing.*;

public class UserInterface extends GBFrame implements KeyListener {

	private LinkedList<JComponent> sizeGUI;
	private LinkedList<JComponent> rcGUI;
	private LinkedList<JComponent> boardGUI;

	private int height;
	private int width;
	private static JFrame frm;

	public UserInterface() {
		initSizeGUI();

		height = 0;
		width = 0;
	}

	public static void main(String[] args) {
		frm = new UserInterface();
		frm.setTitle("Nonogram Solver");
		frm.setSize(500, 200);
		frm.setVisible(true);
	}

	public void buttonClicked(JButton btn) {
		if (btn == sizeGUI.get(0)) {
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

		} else if (btn == rcGUI.get(0)) {
			hide(rcGUI);

			BoardSolver board = init();
			initBoardGUI(board);

			long time = System.currentTimeMillis();

			while(System.currentTimeMillis() - time < 2000) {
				continue;
			}

			time = System.currentTimeMillis();

			int iterations = 0;
			final int MAX = 10; // max number of iterations
			final int STEPS = 6; // number of strategies one can solve with
			String text = "starter text";

			do {
				if(System.currentTimeMillis() - time > 1000) {
					text = board.solve(iterations % STEPS);
					((JTextArea) boardGUI.get(0)).setText(text);
					iterations++;
					System.out.println(iterations);
					// System.out.println("iterations < MAX: " + (iterations < MAX));
					// System.out.println("!text.equals(ERROR): " + !text.equals("ERROR"));
					// System.out.println("text.charAt(text.length()-1) != 'a': " + (text.charAt(text.length()-1) != 'a'));
					time = System.currentTimeMillis();
				}
			} while(iterations < MAX && !text.equals("ERROR") && text.length() > 1 && text.charAt(text.length()-1) != 'a');
		}
	}

	private BoardSolver init() {
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

		return new BoardSolver(rows5_5, columns5_5);
	}

	private void hide(List<JComponent> thing) {
		for (JComponent part : thing) {
			part.setVisible(false);
		}
	}

	private void show(List<JComponent> thing) {
		for (JComponent part : thing) {
			part.setVisible(true);
		}
	}

	private void initSizeGUI() {
		sizeGUI = new LinkedList<JComponent>();

		sizeGUI.add(addButton("Set Size", 3, 2, 1, 1));
		sizeGUI.add(addIntegerField(0, 1, 2, 1, 1)); // Height Field
		sizeGUI.add(addIntegerField(0, 2, 2, 1, 1)); // Width Field
		sizeGUI.add(addLabel("Height: ", 1, 1, 1, 1));
		sizeGUI.add(addLabel("Width: ", 2, 1, 1, 1));
	}

	private void initRCGUI() {
		rcGUI = new LinkedList<JComponent>();

		rcGUI.add(addButton("Done", 4, 1, 1, 1));
	}

	private void initBoardGUI(BoardSolver board) {
		boardGUI = new LinkedList<JComponent>();

		JTextArea output = addTextArea("", 1, 1, 5, 5);
		boardGUI.add(output);
		output.setEditable(false);
		output.setText(board.printBoard());
		frm.setSize(600, 500);
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
