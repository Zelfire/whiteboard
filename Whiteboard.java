import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Whiteboard extends JFrame
{
	public Whiteboard() {
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Add the canvas
		Canvas canvas = new Canvas();
		add(canvas, BorderLayout.CENTER);
		
		//Add the controls
		JButton rectButton = new JButton("Rect");
		rectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DShapeModel rectangle = new DRectModel();
				//Temporary values for testing purposes
				rectangle.setX(100);
				rectangle.setY(100);
				rectangle.setWidth(100);
				rectangle.setHeight(100);
				rectangle.setColor(Color.GRAY);
				canvas.addShape(rectangle);
			}
		});
		JButton ovalButton = new JButton("Oval");
		JButton lineButton = new JButton("Line");
		JButton textButton = new JButton("Text");
		Box shapesBox = Box.createHorizontalBox();
		shapesBox.add(rectButton);
		shapesBox.add(ovalButton);
		shapesBox.add(lineButton);
		shapesBox.add(textButton);
		
		JButton setColorBtn = new JButton("Set Color");
		
		JTextField textInput = new JTextField();
		textInput.setMaximumSize(new Dimension(100, textInput.getPreferredSize().height)); //Temporary size for now
		JComboBox<String> fonts = new JComboBox<>();
		fonts.setMaximumSize(new Dimension(100, textInput.getPreferredSize().height)); //Temporary size for now
		Box textBox = Box.createHorizontalBox();
		textBox.add(textInput);
		textBox.add(fonts);
		
		
		JButton moveToFrontBtn = new JButton("Move to Front");
		JButton moveToBackBtn = new JButton("Move to Back");
		JButton removeShapeBtn = new JButton("Remove Shape");
		Box moveBox = Box.createHorizontalBox();
		moveBox.add(moveToFrontBtn);
		moveBox.add(moveToBackBtn);
		moveBox.add(removeShapeBtn);
		
		Box controlsBox = Box.createVerticalBox();
		controlsBox.add(shapesBox);
		controlsBox.add(setColorBtn);
		controlsBox.add(textBox);
		controlsBox.add(moveBox);
		add(controlsBox, BorderLayout.WEST);
		
		//Align controls to the left
		for (Component comp : controlsBox.getComponents()) {
			((JComponent)comp).setAlignmentX(LEFT_ALIGNMENT);
		}
		
		pack();
		setVisible(true);
	}
	
	public static void main(String[] args) {
		Whiteboard frame = new Whiteboard();
	}
}
