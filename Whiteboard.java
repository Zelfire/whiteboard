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

public class Whiteboard extends JFrame
{
	private static final String ADD_COMMAND = "add";
	private static final String REMOVE_COMMAND = "remove";
	private static final String MOVE_FRONT_COMMAND = "front";
	private static final String MOVE_BACK_COMMAND = "back";
	
	public static final int DEFAULT_PORT = 21413;
	public static final String DEFAULT_IP = "127.0.0.1:" + DEFAULT_PORT;

	private ArrayList<ObjectOutputStream> clientStreams = new ArrayList<>();
	
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
		
		//Add the file menu
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
				//String filename = JOptionPane.showInputDialog("File name", null);
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
				else {
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
		JTextField status = new JTextField("N/A");
		final String CLIENT_MODE = "Client Mode";
		final String SERVER_MODE = "Server Mode";
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
						ClientHandler client = new ClientHandler(host, port);
						client.start();
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
		
		
		//Add the controls
		JLabel addLabel = new JLabel("Add: ");
		JButton rectButton = new JButton("Rect");
		rectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DShapeModel rectangle = new DRectModel();
				addShape(rectangle);
				disableTextControls();
			}
		});
		JButton ovalButton = new JButton("Oval");
		ovalButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DShapeModel oval = new DOvalModel();
				addShape(oval);
				disableTextControls();
			}
		});
		JButton lineButton = new JButton("Line");
		lineButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				DShapeModel line = new DLineModel();
				addShape(line);
				disableTextControls();
			}
		});
		JButton textButton = new JButton("Text");
		textButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                DShapeModel text = new DTextModel();
                addShape(text);
                enableTextControls();
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
		
		//JTextField textInput = new JTextField();
		textInput.setEnabled(false);
		textInput.setMaximumSize(new Dimension(100, textInput.getPreferredSize().height)); //Temporary size for now
		textInput.getDocument().addDocumentListener(new DocumentListener() {
		    public void changedUpdate(DocumentEvent e) {
		        canvas.updateTextShape(textInput.getText());
		    }
		    public void removeUpdate(DocumentEvent e) {
		        canvas.updateTextShape(textInput.getText());
		    }
		    public void insertUpdate(DocumentEvent e) {
		        canvas.updateTextShape(textInput.getText());
		    }
		});
		
		//JComboBox<String> fonts = new JComboBox<>();
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		String[] fontList = ge.getAvailableFontFamilyNames();
		fonts = new JComboBox<>(fontList);
		fonts.setEnabled(false);
		fonts.setMaximumSize(new Dimension(100, textInput.getPreferredSize().height)); //Temporary size for now
		fonts.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = (String) fonts.getSelectedItem();
				canvas.setFont(name);
			}
		});
		
		
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
		updateClients(ADD_COMMAND, model);
	}
	
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
	
	public void enableTextControls() {
        textInput.setEnabled(true);
        fonts.setEnabled(true);
    }
    public void disableTextControls() {
        textInput.setEnabled(false);
        textInput.setText("");
        fonts.setEnabled(false);
    }
    public void updateTextControls(String theText, String theFont) {
        textInput.setText(theText);
    }
    
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
        			System.out.println("Server: Got client"); //Testing purposes
        		}
        	}
        	catch(IOException e) {
        		JOptionPane.showMessageDialog(null, "An error occurred", "Error", JOptionPane.ERROR_MESSAGE);
        	}
        }
    }
    
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
                	}
                	else if (command.equals(REMOVE_COMMAND)) {
                		
                	}
                	else if (command.equals(MOVE_FRONT_COMMAND)) {
                		
                	}
                	else {
                		
                	}
                }
    		}
    		catch(Exception e) //IOException and ClassNotFoundException 
    		{
    			e.printStackTrace();
        		JOptionPane.showMessageDialog(null, "Connection refused", "Error", JOptionPane.ERROR_MESSAGE);
    		}
    	}
    }

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				new Whiteboard();
				new Whiteboard();
			}
		});
	}
}
