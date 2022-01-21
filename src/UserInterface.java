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

	public UserInterface() {
		initSizeGUI();
		

		height = 0;
		width = 0;
	}

	public static void main(String[] args) {
		JFrame frm = new UserInterface();
		frm.setTitle("Nonogram Solver");
		frm.setSize(500, 200);
		frm.setVisible(true);
	}

	public void buttonClicked(JButton btn) {
		if (btn == sizeGUI.get(0)) {
			IntegerField fldHeight = (IntegerField)(sizeGUI.get(1));
			IntegerField fldWidth = (IntegerField)(sizeGUI.get(2));
			if(fldHeight.isValidNumber() && fldWidth.isValidNumber()) {
				height = fldHeight.getNumber();
				width = fldWidth.getNumber();
				hide(sizeGUI);
				initRCGUI();
			} else {
				messageBox("Invalid Input try again");
				if(!fldHeight.isValidNumber()) {
					fldHeight.select(0, fldHeight.getText().length());
					fldHeight.requestFocus();
				}
				if(!fldWidth.isValidNumber()) {
					fldWidth.select(0, fldWidth.getText().length());
					fldWidth.requestFocus();
				}
			}
			
		} else if (btn == rcGUI.get(0)) {
			hide(rcGUI);
			initBoardGUI();
		}
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

	private void initBoardGUI() {
		boardGUI = new LinkedList<JComponent>();

		boardGUI.add(addTextArea("", 1, 1, 5, 5));
		((JTextArea)boardGUI.get(0)).setEditable(false);
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
