import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Whiteboard extends JFrame
{
	public Whiteboard() {
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Add the canvas
		Canvas canvas = new Canvas();
		add(canvas, BorderLayout.CENTER);
		
		//Add the controls
		JLabel addLabel = new JLabel("Add: ");
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
				rectangle.setColor(Color.RED);
				canvas.addShape(rectangle);
			}
		});
		JButton ovalButton = new JButton("Oval");
		ovalButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DShapeModel oval = new DOvalModel();
				//Temporary values for testing purposes
				oval.setX(200);
				oval.setY(200);
				oval.setWidth(200);
				oval.setHeight(200);
				oval.setColor(Color.GRAY);
				canvas.addShape(oval);
			}
		});
		JButton lineButton = new JButton("Line");
		JButton textButton = new JButton("Text");
		Box shapesBox = Box.createHorizontalBox();
		shapesBox.add(addLabel);
		shapesBox.add(rectButton);
		shapesBox.add(ovalButton);
		shapesBox.add(lineButton);
		shapesBox.add(textButton);
		
		JButton setColorBtn = new JButton("Set Color"); 
		setColorBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DShape selected = canvas.getSelected();
				if (canvas.getSelected() != null) {
					Color newColor = JColorChooser.showDialog(null, "Color", selected.getColor());
					selected.getModel().setColor(newColor);
				}
			
			}
		});
		
		JTextField textInput = new JTextField();
		textInput.setMaximumSize(new Dimension(100, textInput.getPreferredSize().height)); //Temporary size for now
		JComboBox<String> fonts = new JComboBox<>();
		fonts.setMaximumSize(new Dimension(100, textInput.getPreferredSize().height)); //Temporary size for now
		Box textBox = Box.createHorizontalBox();
		textBox.add(textInput);
		textBox.add(fonts);
		
		
		JButton moveToFrontBtn = new JButton("Move to Front");
		moveToFrontBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//canvas.moveToFront();
			}
		});
		JButton moveToBackBtn = new JButton("Move to Back");
		JButton removeShapeBtn = new JButton("Remove Shape");
		Box moveBox = Box.createHorizontalBox();
		moveBox.add(moveToFrontBtn);
		moveBox.add(moveToBackBtn);
		moveBox.add(removeShapeBtn);
		
		DefaultTableModel tmodel = new DefaultTableModel(){
			@Override 
			public boolean isCellEditable(int row, int column) { 
				return false; 
			} 
		}; 
		JTable shapeInfo = new JTable(tmodel);
		shapeInfo.setMinimumSize(new Dimension());
		//The addColumn calls should be creating column headings with the the given names however
		//it is not visible for some reason so I made a row with the header names instead, but this should not be necessary 
		tmodel.addColumn(""); 
		tmodel.addColumn("");
		tmodel.addColumn("");
		tmodel.addColumn("");
		Object[] firstRow = { "X", "Y", "WIDTH", "HEIGHT" };
		tmodel.addRow(firstRow);
		
		
		Box controlsBox = Box.createVerticalBox();
		controlsBox.add(Box.createVerticalStrut(20));
		controlsBox.add(shapesBox);
		controlsBox.add(Box.createVerticalStrut(20));
		controlsBox.add(setColorBtn);
		controlsBox.add(Box.createVerticalStrut(20));
		controlsBox.add(textBox);
		controlsBox.add(Box.createVerticalStrut(20));
		controlsBox.add(moveBox);
		controlsBox.add(Box.createVerticalStrut(20));
		controlsBox.add(shapeInfo);
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
