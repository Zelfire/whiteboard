import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.beans.*;
import java.io.*;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * A Canvas of DShape
 */
public class Canvas extends JPanel
{   
	private ArrayList<DShape> shapes;
	private DShape selected;
	private ShapeTableModel tmodel;
	private File currentFile;

	public static final int KNOB_SIZE = 9;
	
	private class CanvasMouseListener implements MouseListener, MouseMotionListener
	{	
		private int preXGap; 
		private int preYGap;
		private int nextToVisit;
		private boolean moving = false;
		private boolean resizingHorizontally = false;
		private boolean resizingVertically = false;
		private boolean cursorChange = false;
	
		@Override
		public void mouseDragged(MouseEvent e) {
			if (selected != null) {
				DShapeModel selectedModel = selected.getModel();
				if (moving) {
					selectedModel.setX(e.getX() - preXGap);
					selectedModel.setY(e.getY() - preYGap);
				} else if (resizingHorizontally || resizingVertically) {
					Point anchorPoint = selectedModel.getAnchor();
					int newWidth = e.getX() - anchorPoint.x;
					int newHeight = e.getY() - anchorPoint.y;
					if(resizingHorizontally)
					{
						selectedModel.setWidth(newWidth);
						if (e.getX() < anchorPoint.x) // coming from the right
						{
							selectedModel.setX(e.getX());
						}
					}
					if (resizingVertically)
					{
						selectedModel.setHeight(newHeight);
						if(e.getY() < anchorPoint.y)
						{
							selectedModel.setY(e.getY());
						}
					}
				} 
			}
		}
		
		
		@Override
		public void mousePressed(MouseEvent e) {
			//Allows user to iterate through overlapping shapes by double clicking by going to the index of the next shape
			if (e.getClickCount() > 1) {
				for (int i = 0; i < shapes.size(); i++) {
					DShape s = shapes.get(nextToVisit++);
					if (nextToVisit == shapes.size())
						nextToVisit = 0;
					Rectangle bounds = s.getBounds();
					if (bounds.contains(e.getPoint()) && s != selected) {
						setSelected(s);
						break;
					}
				}
			}
			else if (selected != null && selected.getBiggerBounds().contains(e.getPoint())) {
				
				if (selected.getBounds().contains(e.getPoint())) {
					moving = true;
					setCursor(new Cursor(Cursor.MOVE_CURSOR));
					cursorChange = true;
					preXGap = e.getX() - selected.getX();
					preYGap = e.getY() - selected.getY();
				}
				else
				{
					Point resizePoint = e.getPoint();
					Point anchorPoint = new Point();
					boolean left = resizePoint.x < selected.getX() + KNOB_SIZE/2;
					boolean right = resizePoint.x > selected.getX() + selected.getWidth() - KNOB_SIZE/2;
					boolean top = resizePoint.y < selected.getY() + KNOB_SIZE/2;
					boolean bottom = resizePoint.y > selected.getY() + selected.getHeight() - KNOB_SIZE/2;
					resizingHorizontally = left || right;
					resizingVertically = top || bottom;
					if (left)
					{
						anchorPoint.x = selected.getX() + selected.getWidth();
					}
					else if (right)
					{
						anchorPoint.x = selected.getX();
					}
					if (top)
					{
						anchorPoint.y = selected.getY() + selected.getHeight();
					}
					else if (bottom)
					{
						anchorPoint.y = selected.getY();
					}
					selected.getModel().setAnchor(anchorPoint);
				}
			}
		
			//Else select front-most shape
			else {
				for (DShape s : shapes) {
					Rectangle bounds = s.getBounds();
					if (bounds.contains(e.getPoint())) {
						setSelected(s);
					} 
				}
			}
			
			
		}
			
		
		@Override
		public void mouseReleased(MouseEvent e) {
			moving = false;
			resizingHorizontally = false;
			resizingVertically = false;
			if (cursorChange) {setCursor(new Cursor(Cursor.DEFAULT_CURSOR));}
		}
		
		@Override
		public void mouseMoved(MouseEvent e) {	
			
			if (selected != null) {
				Rectangle maxResizeBound = selected.getBiggerBounds();
				if (maxResizeBound.contains(e.getPoint()) && !selected.getBounds().contains(e.getPoint())) {
					setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
					cursorChange = true;
				} else if (cursorChange) {
					setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					cursorChange = false;
				} 
			}
		}
		
		@Override
		public void mouseEntered(MouseEvent e) {
		}
		
		@Override
		public void mouseExited(MouseEvent e) {
		}
		
		
		@Override
		public void mouseClicked(MouseEvent e) {	
		}
		
	}
	
	public Canvas() {
		int INITIAL_WIDTH = 400;
		int INITIAL_HEIGHT = 400;
		setPreferredSize(new Dimension(INITIAL_WIDTH, INITIAL_HEIGHT));
		setBackground(Color.WHITE);
		CanvasMouseListener mouseListener = new CanvasMouseListener();
		this.addMouseMotionListener(mouseListener);
		this.addMouseListener(mouseListener);
		shapes = new ArrayList<>();
		tmodel = new ShapeTableModel();
		tmodel.setCanvas(this);
	}
	

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (DShape shape : shapes) {
			shape.draw(g);
			if (shape.equals(selected)) {
				ArrayList<Point> knobs = shape.getKnobs();
				g.setColor(Color.BLACK);
				for (int i = 0; i < knobs.size(); i++) {
					g.fillRect(knobs.get(i).x - KNOB_SIZE / 2, knobs.get(i).y - KNOB_SIZE / 2, KNOB_SIZE, KNOB_SIZE);	
				}
				Rectangle biggerBounds = shape.getBiggerBounds();
				g.drawRect(biggerBounds.x, biggerBounds.y, biggerBounds.width, biggerBounds.height);
			}
		}
	}
	
	/**
	 * Creates a shape using the given shape model and adds it to the canvas
	 * @param shapeModel the model of the shape
	 */
	public void addShape(DShapeModel shapeModel) {
		DShape theShape;
		if (shapeModel instanceof DRectModel) {
			theShape = new DRect();
		}
		else if (shapeModel instanceof DOvalModel) {
			theShape = new DOVal();
		}
		else if (shapeModel instanceof DTextModel) {
			theShape = new DText(); 
		}
		else{
			theShape = new DLine();
		}
		shapeModel.addModelListener(tmodel);
		theShape.setModel(shapeModel);
		theShape.attachView(this);
		shapes.add(theShape);
		setSelected(theShape);
		repaint();
		tmodel.fireTableDataChanged();
	}
	
	/**
	 * Gets the currently selected shape
	 * @return the selected shape
	 */
	public DShape getSelected()
	{
		return selected;
	}
	
	/**
	 * helper method to set the selected shape
	 * @param shape the shape to be set as the selected shape
	 */
	public void setSelected(DShape shape)
	{
		selected = shape;	
		if (shape!=null) {
			DShapeModel model = selected.getModel();
			model.notifyModelSelected();
		}
		repaint();
	}
	
	/**
	 * Gets a shape with the given ID
	 * @param ID the ID to check for
	 * @return the shape with the given ID, null if no shape found
	 */
	public DShape getShape(int ID) {
		DShape shape = null;
		for (int i = 0; i < shapes.size(); i++) {
			DShapeModel model = shapes.get(i).getModel();
			if (model.getID() == ID) {
				shape = shapes.get(i);
			}
		}
		if (shape == null)
			System.err.println("ID not found");
		return shape;
	}
	
	/**
	 * Removes the currently selected shape
	 * @return the model of the shape that was removed
	 */
	public DShapeModel removeShape()
	{
		DShapeModel selectedModel = null;
		if (selected != null) {
			selectedModel = selected.getModel();
			shapes.remove(selected);
			if (shapes.size() > 0)
				selected = shapes.get(shapes.size() -1);
			else
				selected = null;
			repaint();	
			tmodel.fireTableDataChanged();
		}
		return selectedModel;
	}

	/**
	 * Removes the shape with the given ID
	 * @param ID the ID of the shape to remove
	 */
	public void removeShape(int ID) {
		selected = getShape(ID);
		removeShape();
	}
	
	/**
	 * Moves the currently selected shape to the front of the canvas
	 * @return the model of the moved shape
	 */
	public DShapeModel moveToFront()
	{
		DShapeModel selectedModel = null;
		if (selected != null) {
			selectedModel = selected.getModel();
			shapes.remove(selected);
			shapes.add(shapes.size(), selected);
			repaint();	
			tmodel.fireTableDataChanged();
		}
		return selectedModel;
	}
	
	/**
	 * Moves the shape with the given ID to the front of the canvas
	 * @param ID the ID of the shape to move
	 */
	public void moveToFront(int ID) 
	{
		selected = getShape(ID);
		moveToFront();
	}

	/**
	 * Moves the currently selected shape to the back of the canvas
	 * @return the model of the shape that was moved
	 */
	public DShapeModel moveToBack()
	{
		DShapeModel selectedModel = null;
		if (selected != null) {
			selectedModel = selected.getModel();
			shapes.remove(selected);
			shapes.add(0, selected);
			repaint();
			tmodel.fireTableDataChanged();
		}
		return selectedModel;
	}
	
	/**
	 * Moves the shape with the given ID to the back of the canvas
	 * @param ID the ID of the shape to move
	 */
	public void moveToBack(int ID) 
	{
		selected = getShape(ID);
		moveToBack();
	}
	
	/**
	 * Updates the selected shape with the given string if the selected shape is a text shape
	 * @param txt the string to set the text shape
	 */
	public void updateTextShape(String txt)
	{
	    if (selected != null && selected instanceof DText ) {
	        DText tm = (DText) selected;
	        tm.setText(txt);
	        repaint();
	    }
	}
	
	/**
	 * Sets the font of the selected shape if the selected shape is a text shape
	 * @param font the font to set the shape
	 */
	public void setFont(String font)
	{
		if (selected != null && selected instanceof DText ) {
	        DText tm = (DText) selected;
	        tm.setFont(font);
	        repaint();
	    }
	}
	
	/**
	 * Gets an arraylist containing all the shapes in the canvas
	 * @return
	 */
	public ArrayList<DShape> getShapes() {
		return shapes;
	}
	
	/**
	 * Saves the current canvas as an XML file
	 * @param f the file name to save as
	 * @throws FileNotFoundException
	 */
	public void save(File f) throws FileNotFoundException{
		if (!f.equals(currentFile)) {currentFile = f;}
		XMLEncoder out = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(f)));
		DShapeModel[] outputShapes = new DShapeModel[shapes.size()];
		for (int i = 0; i < outputShapes.length; i++) {
			outputShapes[i] = shapes.get(i).getModel();
		}
		out.writeObject(outputShapes);
		out.flush();
		out.close();
	}
	
	/**
	 * Loads the canvas with the given .xml file
	 * @param f the file to load
	 * @throws FileNotFoundException
	 */
	public void open(File f) throws FileNotFoundException{
		currentFile = f;
		XMLDecoder in = new XMLDecoder(new BufferedInputStream(new FileInputStream(f)));
		DShapeModel[] inputShapes = (DShapeModel[]) in.readObject();
		clear();
		for (DShapeModel shape : inputShapes) {
			addShape(shape);
		}
		tmodel.fireTableDataChanged();
		in.close();
	}
	
	/**
	 * Saves the current canvas as a .png file
	 * @param f the name of the .png file to save as
	 * @throws IOException
	 */
	public void saveAsPNG(File f) throws IOException {
		DShape tempSelected = selected;
		selected = null; //Get rid of knobs temporarily
		repaint();
		BufferedImage image = (BufferedImage) createImage(getWidth(), getHeight());
        Graphics g = image.getGraphics();
        paintAll(g);
        g.dispose();
        ImageIO.write(image, "PNG", f);
        selected = tempSelected; //Restore knobs
	}

	/**
	 * Clears the canvas
	 */
	public void clear()
	{
		shapes.clear();
		tmodel.fireTableDataChanged();
		repaint();
	}

	/**
	 * Gets a table model that dynamically retrieves data from the canvas
	 * @return the table model of the canvas shapes
	 */
	public ShapeTableModel getTableModel()
	{
		return tmodel;
	}
	
	/**
	 * Gets the current file used by the canvas
	 * @return
	 */
	public File getCurrentFile()
	{
		return currentFile;
	}
	
	/**
	 * Removes the mouse listener from the canvas
	 */
	public void removeListeners() {
		MouseListener[] listeners = getMouseListeners();
		removeMouseListener(listeners[0]);
	}
}
