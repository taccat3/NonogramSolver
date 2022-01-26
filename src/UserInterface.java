import java.util.LinkedList;
import java.util.List;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;

import BreezySwing.*;

// TODO: error checking for too big section values

public class UserInterface extends GBFrame implements KeyListener {

	private static LinkedList<JComponent> sizeGUI;
	private static LinkedList<JComponent> rcGUI;
	private static LinkedList<JComponent> boardGUI;


	private int height;
	private int width;
	private static JFrame frm;
	private static BoardSolver board;
	private static boolean ready;
	
	private static final Color COLOR = Color.getHSBColor((float).203, (float)0.23, (float)0.79);

	public UserInterface() {
		// TODO: uncomment
		splashScreen("<html><br><center>&#9;Please Note that any incorrect input will likely result in a convoluted error.&#9;<br><br>Please plan accordingly :)</center><br></html>");
		splashScreen("<html><br><center>&#9;Also Note that this probably won't work. :'(&#9;<br><br></center><html>");
		initSizeGUI();

		height = 0;
		width = 0;
		ready = false;

	}

	private void splashScreen(String message) {
		JFrame splash = new JFrame();
		splash.add(addLabel(message, 1, 1, 1, 1));

		splash.getContentPane().setBackground(COLOR);
		splash.setUndecorated(true);

		splash.pack();
		splash.setLocationRelativeTo(null);
		splash.setVisible(true);

		long time = System.currentTimeMillis();
		while(System.currentTimeMillis() - time < 3000) {
			continue;
		}

		splash.setVisible(false);
	}

	public static void main(String[] args) {
		frm = new UserInterface();
		frm.setTitle("Nonogram Solver");
		frm.pack();
		frm.setLocationRelativeTo(null);
		frm.getContentPane().setBackground(COLOR);
		frm.setVisible(true);

		while (!ready) {
			System.out.println(ready);
			continue;
		}

		solve();
	}

	private JPanel panel(String str, int num, int length) {
		JPanel panel = new JPanel();

		panel.setBackground(COLOR);

		// panel.setBorder (new TitledBorder(new EtchedBorder(), "Display Area"));

		// create the middle panel components
		JTextArea info = new JTextArea(num, 1);
		JTextArea input = new JTextArea(num, length);

		info.setBackground(COLOR);
		info.setText(getInputLabels(str, num));
		info.setEditable(false); // set textArea non-editable

		input.setBackground(COLOR.darker());
		input.setBounds(new Rectangle(input.getX(), input.getY(), num, length));

		JScrollPane scroll = new JScrollPane(input); // was info
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setVisible(false);

		//Add TextArea in to middle panel
		panel.add(info);
		panel.add(input);
		panel.add(scroll);

		return panel;
	}

	private static String getInputLabels(String str, int num) {
		String label = "";
		
		for(int i = 1; i <= num; i++) {
			label += str + " " + i + ":\n";
		}

		return label;
	}

	private static void solve() {
		long time = System.currentTimeMillis();

		int iterations = 0;
		final int MAX = 10; // max number of iterations
		final int STEPS = 6; // number of strategies one can solve with
		String text = "starter text";

		do {
			if (System.currentTimeMillis() - time > 2000) {
				text = board.solve(iterations % STEPS);
				((JTextArea) boardGUI.get(0)).setText(text);
				iterations++;
				System.out.println(iterations);
				time = System.currentTimeMillis();
				System.out.println("update");
			}
		} while (iterations < MAX && !text.equals("ERROR") && notSolved(text));

		System.out.println("DONE");
	}

	private static boolean notSolved(String str) {
		final String END_TAG = "\nSOLVED";
		return str.length() > END_TAG.length() && !str.substring(str.length() - END_TAG.length()).equals(END_TAG);
	}

	public void buttonClicked(JButton btn) {
		if (btn == sizeGUI.get(0)) {
			sizeGUIActions();

		} else if (btn == rcGUI.get(0)) {

			Parser parser = new Parser();

			// TODO: error checking
			int[][] rows = null;
			int[][] columns = null;

			try {
				rows = parser.parse(((JTextArea) (rcGUI.get(1)).getComponents()[1]).getText(), height);
				columns = parser.parse(((JTextArea) (rcGUI.get(2)).getComponents()[1]).getText(), width);
			} catch (IllegalArgumentException e) {
				messageBox("need to enter " + height + " rows and " + width
						+ "columns\nEnter 0s if there is none in a section");
				return;
			}

			hide(rcGUI);

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

		int[][] rows10_1 = { { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 } };
		int[][] columns10_1 = { { 10 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 } };

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
		// rcGUI.add(addLabel("Enter the Rows (use spaces per section and new lines per row)", 1, 1, 1, 1));
		// rcGUI.add(addTextArea("", 2, 1, 2, 2));
		// // columns stuff
		// rcGUI.add(addLabel("Enter the Columns (use spaces per section and new lines per column)", 4, 1, 1, 1));
		// rcGUI.add(addTextArea("", 5, 1, 2, 2));

		rcGUI.add(panel("Row", height, width));
		rcGUI.add(panel("Column", width, height));

		frm.add(rcGUI.get(1));
		frm.add(rcGUI.get(2));

		frm.pack();

		// frm.setSize(frm.getHeight(), frm.getWidth());
	}

	private void initBoardGUI(BoardSolver board) {
		boardGUI = new LinkedList<JComponent>();

		JTextArea output = addTextArea("", 1, 1, 5, 5);
		boardGUI.add(output);
		output.setEditable(false);
		output.setText(board.printBoard());
		frm.setSize(width * 50, height * 50);

		frm.pack();
		frm.setSize(frm.getHeight() + 10, frm.getWidth() + 10);
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
