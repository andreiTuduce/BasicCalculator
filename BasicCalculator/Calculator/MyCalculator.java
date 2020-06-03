
import java.awt.*;
import java.awt.event.*;

/*********************************************/

public class MyCalculator extends Frame {

	public boolean setClear = true;
	double number, memValue;
	char op;

	String digitButtonText[] = { "7", "8", "9", "4", "5", "6", "1", "2", "3", "0", "+/-", "." };
	String operatorButtonText[] = { "+", "-", "*", "/", "%", "=", "C" };

	MyDigitButton digitButton[] = new MyDigitButton[digitButtonText.length];
	MyOperatorButton operatorButton[] = new MyOperatorButton[operatorButtonText.length];

	Label displayLabel = new Label("0", Label.RIGHT);

	final int FRAME_WIDTH = 325, FRAME_HEIGHT = 325;
	final int HEIGHT = 30, WIDTH = 30, H_SPACE = 10, V_SPACE = 10;
	final int TOPX = 30, TOPY = 50;

	MyCalculator(String frameText) {
		super(frameText);

		int tempX = TOPX, y = TOPY;
		displayLabel.setBounds(tempX, y, 240, HEIGHT);
		displayLabel.setBackground(Color.DARK_GRAY);
		displayLabel.setForeground(Color.WHITE);
		add(displayLabel);

//set the Number buttons
		int digitX = TOPX + WIDTH + H_SPACE;
		int digitY = TOPY + 2 * (HEIGHT + V_SPACE);
		tempX = digitX;
		y = digitY;
		for (int i = 0; i < digitButton.length; i++) {
			digitButton[i] = new MyDigitButton(tempX, y, WIDTH, HEIGHT, digitButtonText[i], this);
			digitButton[i].setForeground(Color.BLACK);
			tempX += WIDTH + H_SPACE;
			if ((i + 1) % 3 == 0) {
				tempX = digitX;
				y += HEIGHT + V_SPACE;
			}
		}

//set the Operator buttons 
		int opsX = digitX + 2 * (WIDTH + H_SPACE) + H_SPACE;
		int opsY = digitY;
		tempX = opsX;
		y = opsY;
		for (int i = 0; i < operatorButton.length; i++) {
			tempX += WIDTH + H_SPACE;
			operatorButton[i] = new MyOperatorButton(tempX, y, WIDTH, HEIGHT, operatorButtonText[i], this);
			operatorButton[i].setForeground(Color.BLACK);
			if ((i + 1) % 2 == 0) {
				tempX = opsX;
				y += HEIGHT + V_SPACE;
			}
		}

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent ev) {
				System.exit(0);
			}
		});

		setLayout(null);
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setVisible(true);
	}

	static String getFormattedText(double temp) {
		String resText = "" + temp;
		if (resText.lastIndexOf(".0") > 0)
			resText = resText.substring(0, resText.length() - 2);
		return resText;
	}

	public static void main(String[] args) {
		new MyCalculator("Calculator - JavaTpoint");
	}
}

/*******************************************/

class MyDigitButton extends Button implements ActionListener {
	MyCalculator cl;

	MyDigitButton(int x, int y, int width, int height, String cap, MyCalculator clc) {
		super(cap);
		setBounds(x, y, width, height);
		this.cl = clc;
		this.cl.add(this);
		addActionListener(this);
	}

	static boolean isInString(String s, char ch) {
		for (int i = 0; i < s.length(); i++)
			if (s.charAt(i) == ch)
				return true;
		return false;
	}

	public void actionPerformed(ActionEvent ev) {
		String tempText = ((MyDigitButton) ev.getSource()).getLabel();

		if (tempText.equals(".")) {
			if (cl.setClear) {
				cl.displayLabel.setText("0.");
				cl.setClear = false;
			} else if (!isInString(cl.displayLabel.getText(), '.'))
				cl.displayLabel.setText(cl.displayLabel.getText() + ".");
			return;
		}

		if (tempText.equals("+/-")) {
			int value = Integer.parseInt(cl.displayLabel.getText());
			value *= -1;
			cl.displayLabel.setText("" + value);
			return;
		}

		int index = 0;
		try {
			index = Integer.parseInt(tempText);
		} catch (NumberFormatException e) {
			return;
		}

		if (index == 0 && cl.displayLabel.getText().equals("0"))
			return;

		if (cl.setClear) {
			cl.displayLabel.setText("" + index);
			cl.setClear = false;
		} else
			cl.displayLabel.setText(cl.displayLabel.getText() + index);
	}
}

/********************************************/

class MyOperatorButton extends Button implements ActionListener {
	MyCalculator cl;

	MyOperatorButton(int x, int y, int width, int height, String cap, MyCalculator clc) {
		super(cap);
		setBounds(x, y, width, height);
		this.cl = clc;
		this.cl.add(this);
		addActionListener(this);
	}

	public void actionPerformed(ActionEvent ev) {
		String opText = ((MyOperatorButton) ev.getSource()).getLabel();

		cl.setClear = true;
		double temp = Double.parseDouble(cl.displayLabel.getText());
		boolean isDivisionByZero = false;

		if(opText.equals("C"))
		{
			temp = 0;
			cl.displayLabel.setText(MyCalculator.getFormattedText(temp));
			return;
		}
		
		if (!opText.equals("=")) {
			cl.number = temp;
			cl.op = opText.charAt(0);
			return;
		}
		switch (cl.op) {
		case '+':
			temp += cl.number;
			break;
		case '-':
			temp = cl.number - temp;
			break;
		case '*':
			temp *= cl.number;
			break;
		case '%':
			if (temp != 0)
				temp = cl.number / temp;
			else
				isDivisionByZero = true;
			break;
		case '/':
			if (temp != 0)
				temp = cl.number / temp;
			else
				isDivisionByZero = true;
			break;
		}

		if(isDivisionByZero)
			cl.displayLabel.setText(0 + "");
		else
			cl.displayLabel.setText(MyCalculator.getFormattedText(temp));
	}
}
