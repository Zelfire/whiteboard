import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;

public class Whiteboard extends JFrame
{
	private Canvas canvas;
	
	public Whiteboard() {
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Add the canvas
		canvas = new Canvas();
		add(canvas, BorderLayout.CENTER);
		
		//Add the file menu
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);

		JMenuItem save = new JMenuItem("Save");
		save.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				//Prompt the user for a filename
				String filename = JOptionPane.showInputDialog("File name", null);
				try {
					canvas.save(new File(filename + ".xml"));
				}
				catch (FileNotFoundException e) {
					JOptionPane.showMessageDialog(null, "File not found!", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		fileMenu.add(save);
		
		JMenuItem open = new JMenuItem("Open");
		open.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				//Open file chooser
				JFileChooser chooser = new JFileChooser();
				chooser.setAcceptAllFileFilterUsed(false);
				chooser.addChoosableFileFilter(new FileFilter() {
					@Override
					public boolean accept(File f)
					{
						String filename = f.getName().toLowerCase();
						return filename.endsWith(".xml") || f.isDirectory();
					}
					@Override
					public String getDescription()
					{
						return ".xml";
					}
					
				});
				chooser.showOpenDialog(Whiteboard.this);
				File selectedFile = chooser.getSelectedFile();
				try {
					canvas.open(selectedFile);
				}
				catch(FileNotFoundException e) {
					JOptionPane.showMessageDialog(null, "File not found!", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		fileMenu.add(open);
		
		JMenuItem savePNG = new JMenuItem("Save as PNG");
		savePNG.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Prompt the user for a filename
				String filename = JOptionPane.showInputDialog("File name", null);
				try
				{
					canvas.saveAsPNG(new File(filename + ".png"));
				}
				catch (IOException e1)
				{
					JOptionPane.showMessageDialog(null, "An error occured while saving the file", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		fileMenu.add(savePNG);
		
		
		setJMenuBar(menuBar);
		
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
		lineButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				DShapeModel line = new DLineModel();
				addShape(line);
			}
		});
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
				canvas.moveToFront();
			}
		});
		JButton moveToBackBtn = new JButton("Move to Back");
		moveToBackBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				canvas.moveToBack();
			}
		});
		JButton removeShapeBtn = new JButton("Remove Shape");
		removeShapeBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				canvas.removeShape();
			}
		});
		Box moveBox = Box.createHorizontalBox();
		moveBox.add(moveToFrontBtn);
		moveBox.add(moveToBackBtn);
		moveBox.add(removeShapeBtn);
		
		JTable shapeInfo = new JTable(canvas.getTableModel());
		
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
