import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;

import java.net.*;
import java.util.ArrayList;

public class Whiteboard extends JFrame implements ModelListener
{
	private static final String ADD_COMMAND = "add";
	private static final String REMOVE_COMMAND = "remove";
	private static final String MOVE_FRONT_COMMAND = "front";
	private static final String MOVE_BACK_COMMAND = "back";
	private static final String CHANGE_COMMAND = "change";
	
	public static int serialID = 1;
	
	public static final int DEFAULT_PORT = 21413;
	public static final String DEFAULT_IP = "127.0.0.1:" + DEFAULT_PORT;
	

	private JTextField status;
	public static final String CLIENT_MODE = "Client Mode";
	public static final String SERVER_MODE = "Server Mode";
	private ArrayList<ObjectOutputStream> clientStreams = new ArrayList<>();
	private boolean addingShape;
	
	private Canvas canvas;
	
	JTextField textInput = new JTextField();
	JComboBox<String> fonts;
	
	public Whiteboard() {
		setLayout(new BorderLayout());
		this.setTitle("WhiteBoard");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Add the canvas
		canvas = new Canvas();
		add(canvas, BorderLayout.CENTER);
		
		//Add the file menu and its options
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		
		JMenuItem saveAs = new JMenuItem("Save as");
		saveAs.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				//Prompt the user for a filename
				JFileChooser saver = new JFileChooser();
				saver.setAcceptAllFileFilterUsed(false);
				saver.setDialogTitle("Save as...");
				saver.addChoosableFileFilter(new FileFilter() {
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
				saver.showSaveDialog(Whiteboard.this);
				File file = saver.getSelectedFile();
				if (file != null) {
					String filename = file.getPath();

					if (!filename.endsWith(".xml"))  //Prevents program from naming files such as output.xml.xml
					{
						filename += ".xml";
					}
					
					try {
						canvas.save(new File(filename));
					} catch (FileNotFoundException e) {
						JOptionPane.showMessageDialog(null, "File not found!", "Error", JOptionPane.ERROR_MESSAGE);
					} 
				}
				if (canvas.getCurrentFile() != null)
					setTitle( canvas.getCurrentFile().getName() + " - WhiteBoard");
			}
		});
		
		JMenuItem save = new JMenuItem("Save");
		save.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (canvas.getCurrentFile() != null) {
					try {
						canvas.save(canvas.getCurrentFile());
					} catch (FileNotFoundException exe) {
						JOptionPane.showMessageDialog(null, "File not found!", "Error", JOptionPane.ERROR_MESSAGE);
					} 
				}
				else { //if this is a new project use the saveAs operation 
					ActionListener saveAslistener = saveAs.getActionListeners()[0];
					saveAslistener.actionPerformed(e);
				}
				if (canvas.getCurrentFile() != null)
					setTitle( canvas.getCurrentFile().getName() + " - WhiteBoard");
			}
		});
		
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
				if (selectedFile != null) {
					try {
						canvas.open(selectedFile);
						listenToShapes();
					}
					catch(FileNotFoundException e) {
						JOptionPane.showMessageDialog(null, "File not found!", "Error", JOptionPane.ERROR_MESSAGE);
					}	

					setTitle( canvas.getCurrentFile().getName() + " - WhiteBoard");
				}
			}
		});
		
		
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
		
		fileMenu.add(save);
		fileMenu.add(saveAs);
		fileMenu.add(open);
		fileMenu.add(savePNG);
		
		
		setJMenuBar(menuBar);
		
		// Add network buttons
		status = new JTextField("N/A");
		status.setEditable(false);
		JButton startServer = new JButton("Server Start");
		startServer.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (status.getText().equals(CLIENT_MODE))
				{
					JOptionPane.showMessageDialog(null, "Can't change from client mode to server mode. Restart the program.", "Error", JOptionPane.ERROR_MESSAGE);
				}
				else
				{
					String port = JOptionPane.showInputDialog("Enter port number", DEFAULT_PORT);
					try {
						int portNum = Integer.parseInt(port);
						if (portNum > 65535 || portNum < 0) {
							throw new IllegalArgumentException();
						}
						status.setText(SERVER_MODE);
						ServerAccepter server = new ServerAccepter(Integer.parseInt(port));
						server.start();
					}
					catch(IllegalArgumentException e1) {
						JOptionPane.showMessageDialog(null, "Enter an integer between 0 and 65535", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		JButton startClient = new JButton("Client Start");
		startClient.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (status.getText().equals(SERVER_MODE))
				{
					JOptionPane.showMessageDialog(null, "Can't change from server mode to client mode. Restart the program.", "Error", JOptionPane.ERROR_MESSAGE);
				}
				else
				{
					String ipAddress = JOptionPane.showInputDialog("Enter IP Address", DEFAULT_IP);
					try 
					{
						String[] parts = ipAddress.split(":");
						String host = parts[0];
						int port = Integer.parseInt(parts[1]);
						if (port > 65535 || port < 0) {
							throw new IllegalArgumentException();
						}
						status.setText(CLIENT_MODE);
						canvas.clear();
						ClientHandler client = new ClientHandler(host, port);
						client.start();
						canvas.removeListeners();
					}
					catch (Exception e1) //ArrayIndexOutOfBounds and IllegalArgumentException
					{
						JOptionPane.showMessageDialog(null, "Please enter a valid IP address", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		Box networkBox = Box.createHorizontalBox();
		networkBox.add(startServer);
		networkBox.add(startClient);
		networkBox.add(status);
		add(networkBox, BorderLayout.NORTH);
		
		
		//Add the shape buttons
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
		textButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                DTextModel text = new DTextModel();
                addShape(text);
            }
        });
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
		
		//Set up the text controls
		textInput.setEnabled(false);
		textInput.setMaximumSize(new Dimension(150, textInput.getPreferredSize().height));
		textInput.getDocument().addDocumentListener(new DocumentListener() {
		    public void changedUpdate(DocumentEvent e) {
		        setTextinShape();
		    }
		    public void removeUpdate(DocumentEvent e) {
		        setTextinShape();
		    }
		    public void insertUpdate(DocumentEvent e) {
		        setTextinShape();
		    }
		    public void setTextinShape() {
		    	if (!addingShape)
		    		canvas.updateTextShape(textInput.getText());
		    }
		});
		
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		String[] fontList = ge.getAvailableFontFamilyNames();
		fonts = new JComboBox<>(fontList);
		fonts.setEnabled(false);
		fonts.setMaximumSize(new Dimension(150, textInput.getPreferredSize().height));
		fonts.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = (String) fonts.getSelectedItem();
				if (!addingShape) 
					canvas.setFont(name);
			}
		});
		
		
		Box textBox = Box.createHorizontalBox();
		textBox.add(textInput);
		textBox.add(fonts);
		
		//Add shape operations
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
		
		//Add table containing shape info
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

	/**
	 * Adds a shape to the canvas of the whiteboard
	 * @param model the model to add
	 */
	private void addShape(DShapeModel model)
	{
		if (status.getText().equals(CLIENT_MODE)) {
			return;
		}
		model.setID(serialID++);
		model.setX(10);
		model.setY(10);
		model.setWidth(20);
		model.setHeight(20);
		model.setColor(Color.GRAY);
		model.addModelListener(this);
		addingShape = true;
		canvas.addShape(model);
		addingShape = false;
		updateClients(ADD_COMMAND, model);
	}
	
	/**
	 * Removes the selected shape from the canvas
	 */
	private void removeShape() 
	{
		if (status.getText().equals(CLIENT_MODE)) {
			return;
		}
		DShapeModel removed = canvas.removeShape();
		if (removed != null)
			updateClients(REMOVE_COMMAND, removed);
	}
	
	/**
	 * Moves the selected shape to the front of other shaped
	 */
	private void moveToFront() 
	{
		if (status.getText().equals(CLIENT_MODE)) {
			return;
		}
		DShapeModel moved = canvas.moveToFront();
		if (moved != null)
			updateClients(MOVE_FRONT_COMMAND, moved);
	}
	
	/**
	 * Moves the selected shape to back of the other shapes
	 */
	private void moveToBack()
	{
		if (status.getText().equals(CLIENT_MODE)) {
			return;
		}
		DShapeModel moved = canvas.moveToBack();
		if (moved != null)
			updateClients(MOVE_BACK_COMMAND, moved);
	}
	
	@Override
	/**
	 * Notifies the clients when a shape model has changed
	 * @param model the model that has changed
	 */
	public void modelChanged(DShapeModel model)
	{
		if (SERVER_MODE.equals(status.getText()))
			updateClients(CHANGE_COMMAND, model);	
	}
	
	
	@Override
	/**
	 * Updates the text controls when a shape has been selected
	 * @param model the model of the shape that has been selected
	 */
	public void modelSelected(DShapeModel model) {
		if (model instanceof DTextModel) {
			DTextModel textModel =  (DTextModel) model;
			enableTextControls();
			updateTextControls(textModel.getText(), textModel.getFontName());
		}
		else 
			disableTextControls();
	}
	
	/**
	 * Sends clients a model and a given command of what to do with the model
	 * @param command the command to give the clients
	 * @param model the model to send to the clients
	 */
	private void updateClients(String command, DShapeModel model) {
		for (int i = 0; i < clientStreams.size(); i++) {
			try
			{
				clientStreams.get(i).writeObject(command); //Can write directly, because Strings are serializable
				OutputStream memStream = new ByteArrayOutputStream();
				XMLEncoder in = new XMLEncoder(memStream);
				in.writeObject(model);
				in.close();
				String xmlString = memStream.toString();
				clientStreams.get(i).writeObject(xmlString);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Enables the text controls 
	 */
	public void enableTextControls() {
        textInput.setEnabled(true);
        fonts.setEnabled(true);
    }
	
	/**
	 * Disables the text controls
	 */
    public void disableTextControls() {
        textInput.setEnabled(false);
        textInput.setText("");
        fonts.setEnabled(false);
    }
    
    /**
     * Updates text controls when text shape has been selected
     * @param theText the text of the text shape
     * @param theFont the font of the text shape
     */
    public void updateTextControls(String theText, String theFont) {
    	fonts.setSelectedItem(theFont);
    	textInput.setText(theText);
    }
    
    /**
     * Gets the status of this whiteboard (client, server, or n/a)
     * @return the status of this whiteboard
     */
    public String getStatus() {
    	return status.getText();
    }
    
    /**
     * A class to accept clients
     */
    class ServerAccepter extends Thread {
    	private int port;
        ServerAccepter(int port) {
            this.port = port;
        }
        
        @Override
        public void run() {
        	try {
        		ServerSocket serverSocket = new ServerSocket(port);
        		while (true) {
        			Socket toClient = serverSocket.accept();
        			ObjectOutputStream out = new ObjectOutputStream(toClient.getOutputStream());
        			clientStreams.add(out);
        			
        			//Populates the client canvas with the shapes currently on the server canvas
        			for (DShape shape : canvas.getShapes()) {
        				DShapeModel model = shape.getModel();
        				updateClients(ADD_COMMAND, model);
        			}
        		}
        	}
        	catch(IOException e) {
        		JOptionPane.showMessageDialog(null, "An error occurred", "Error", JOptionPane.ERROR_MESSAGE);
        	}
        }
    }
    
    /**
     * A class that handles the client side of a tcp connection
     */
    class ClientHandler extends Thread{
    	private String name;
    	private int port;
    	
    	ClientHandler(String name, int port) {
    		this.name = name;
    		this.port = port;
    	}
    	
    	@Override
    	public void run() {
    		try 
    		{
                Socket toServer = new Socket(name, port);
                ObjectInputStream in = new ObjectInputStream(toServer.getInputStream());
                while (true) {
                	String command = (String) in.readObject();
                	String xmlShapeModel = (String) in.readObject();
                	XMLDecoder decoder = new XMLDecoder(new ByteArrayInputStream(xmlShapeModel.getBytes()));
                	DShapeModel aModel = (DShapeModel) decoder.readObject();
                	decoder.close();
                	
                	if (command.equals(ADD_COMMAND)) {
                		canvas.addShape(aModel);
                		canvas.setSelected(null);
                	}
                	else if (command.equals(REMOVE_COMMAND)) {
                		int modelID = aModel.getID();
                		canvas.removeShape(modelID);
                	}
                	else if (command.equals(MOVE_FRONT_COMMAND)) {
                		int modelID = aModel.getID();
                		canvas.moveToFront(modelID);
                	}
                	else if (command.equals(MOVE_BACK_COMMAND)){
                		int modelID = aModel.getID();
                		canvas.moveToBack(modelID);
                	}
                	else {
                		int modelID = aModel.getID();
                		DShapeModel clientModel = canvas.getShape(modelID).getModel();
                		clientModel.mimic(aModel);
                	}
                }
    		}
    		catch(Exception e) //IOException and ClassNotFoundException 
    		{
        		JOptionPane.showMessageDialog(null, "Connection refused", "Error", JOptionPane.ERROR_MESSAGE);
    		}
    	}
    }
    
    /**
     * Used when a file is opened to listen to shapes already on the fule
     */
    private void listenToShapes() {
    	for (DShape s: canvas.getShapes()) {
			DShapeModel model = s.getModel();
			model.addModelListener(this);
		}
    }
    
    /**
     * Runs the WhiteBoard Application 
     * @param args command line arguments
     */
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
