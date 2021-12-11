import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import BreezySwing.GBFrame;
import BreezySwing.IntegerField;

public class UserInterface extends GBFrame implements KeyListener {

	private LinkedList<JComponent> sizeGUI;
	private LinkedList<JComponent> rcGUI;

	private JTable board;

	private int height;
	private int width;

	Server server = new Server();

	public UserInterface() {
		sizeGUI = new LinkedList<JComponent>();
		rcGUI = new LinkedList<JComponent>();

		sizeGUI.add(addButton("Set Size", 3, 2, 1, 1));
		sizeGUI.add(addLabel("Height: ", 1, 1, 1, 1));
		sizeGUI.add(addIntegerField(0, 1, 2, 1, 1));
		sizeGUI.add(addLabel("Width: ", 2, 1, 1, 1));
		sizeGUI.add(addIntegerField(0, 2, 2, 1, 1));

		rcGUI.add(addButton("Done", 4, 1, 1, 1));
		// server.process("1\n1 2\n1 2 3\n1 2 3 4");
	}

	public static void main(String[] args) {
		
		int[][] columns5_1 = {{2, 2},
						{5},
						{0},
						{0},
						{0}};
		int[][] rows5_1 = {{2},
						   {2},
						   {1},
						   {2},
						   {2}};
		int[][] columns5_2 = {{2, 2},
							{5},
							{0},
							{0},
							{0}};
		int[][] rows5_2 = {{2},
						{2},
						{1},
						{2},
						{2}};

		int[][] columns5_3 = {{0},
						   {0},
						   {0},
						   {5},
						   {2, 2}};
		int[][] rows5_3 = {{2},
					    {2},
					    {1},
					    {2},
					    {2}};
		
		int[][] rows5_4 = {{0},
							{0},
							{0},
							{5},
							{2, 2}};
		int[][] columns5_4 = {{2},
			    		{2},
			    		{1},
			    		{2},
			    		{2}};

		int[][] rows5_5 = {{3},
							{2},
							{2},
							{2,1},
							{3}};

		int[][] columns5_5 = {{2},
							{2,1},
							{1,1},
							{3},
							{3}};



		
		
		BoardSolver board2 = new BoardSolver(rows5_5, columns5_5);
		
		board2.solve();
		
//		JFrame frm = new UserInterface();
//		frm.setTitle("Nonogram Solver");
//		frm.setSize(500, 200);
//		frm.setVisible(true);	
	}

	public void buttonClicked(JButton btn) {
		if (btn == sizeGUI.get(0)) {
			IntegerField fldHeight = (IntegerField) sizeGUI.get(2);
			IntegerField fldWidth = (IntegerField) sizeGUI.get(4);
			if (fldHeight.isValidNumber() && fldWidth.isValidNumber()) {
				height = fldHeight.getNumber();
				width = fldWidth.getNumber();
				if (height > 0 || width > 0) {
					server.setHeightWidth(height, width);
					hide(sizeGUI);
					rcGUI.add(addTextArea("", 2, 1, 2, 2));
					rcGUI.add(addLabel("Enter Rows, use spaces between numbers and enters between rows", 1, 1, 1, 1));

					((JTextArea) rcGUI.get(1)).setRows(height);
				}
			}
		} else if (btn == rcGUI.get(0)) {
			String input = ((JTextArea) rcGUI.get(1)).getText();
			switch (server.getBoardState()) {
				case 0:
					if (server.process(input, Board2.RC.ROW)) {
						((JLabel) rcGUI.get(2))
								.setText("Enter Columns, use spaces between numbers and enters between columns");
						((JTextArea) rcGUI.get(1)).setText("");
					}
					break;
				case 1:
					if (server.process(input, Board2.RC.COLUMN)) {
						hide(rcGUI);

						board = new JTable(new DefaultTableModel(server.getRows(), server.getColumnNums()));
						System.out.println(server.print());
					}
					break;
				case 2:
					System.out.println("Not solved but HOW????");
					break;
				case 3:
					System.out.println("HOW????");
					break;
			}

		}
	}

	private void hide(List<JComponent> thing) {
		for (JComponent part : thing) {
			part.setVisible(false);
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
