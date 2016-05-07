import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Whiteboard extends JFrame
{
	private ShapeTableModel tmodel;
	private Canvas canvas;
	
	public Whiteboard() {
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Add the canvas
		canvas = new Canvas();
		add(canvas, BorderLayout.CENTER);
		
		//Add the controls
		JLabel addLabel = new JLabel("Add: ");
		JButton rectButton = new JButton("Rect");
		rectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DShapeModel rectangle = new DRectModel();
				addShape(rectangle);
			}
		});
		JButton ovalButton = new JButton("Oval");
		ovalButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DShapeModel oval = new DOvalModel();
				addShape(oval);
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
				moveToFront();
			}
		});
		JButton moveToBackBtn = new JButton("Move to Back");
		moveToBackBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				moveToBack();
			}
		});
		JButton removeShapeBtn = new JButton("Remove Shape");
		removeShapeBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				removeShape();
			}
		});
		Box moveBox = Box.createHorizontalBox();
		moveBox.add(moveToFrontBtn);
		moveBox.add(moveToBackBtn);
		moveBox.add(removeShapeBtn);
		
		tmodel = new ShapeTableModel();
		tmodel.setCanvas(canvas);
		JTable shapeInfo = new JTable(tmodel);
		
		JScrollPane tableScroller = new JScrollPane(shapeInfo);
		tableScroller.setPreferredSize(new Dimension());
		
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
		controlsBox.add(tableScroller);
		add(controlsBox, BorderLayout.WEST);
		
		//Align controls to the left
		for (Component comp : controlsBox.getComponents()) {
			((JComponent)comp).setAlignmentX(LEFT_ALIGNMENT);
		}
		
		pack();
		setVisible(true);
	}


	private void addShape(DShapeModel model)
	{
		model.setX(10);
		model.setY(10);
		model.setWidth(20);
		model.setHeight(20);
		model.setColor(Color.GRAY);
		canvas.addShape(model);
		model.addModelListener(tmodel);
		tmodel.fireTableDataChanged();
	}


	private void removeShape()
	{
		canvas.removeShape();
		tmodel.fireTableDataChanged();
	}
	

	private void moveToBack()
	{
		canvas.moveToBack();
		tmodel.fireTableDataChanged();
	}

	private void moveToFront()
	{
		canvas.moveToFront();
		tmodel.fireTableDataChanged();
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				new Whiteboard();
			}
		});
	}
}
