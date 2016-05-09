import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.beans.*;
import java.io.*;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.TableModel;

public class Canvas extends JPanel
{   
    //private Whiteboard whiteboard;
	private ArrayList<DShape> shapes;
	private DShape selected;
	private ShapeTableModel tmodel;

	public static final int KNOB_SIZE = 9;
	
	private class CanvasMouseListener implements MouseListener, MouseMotionListener
	{	
		
		private int preXGap; 
		private int preYGap;
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
						if (preXGap < 0) // coming from the left
						{
							selectedModel.setX(e.getX());
							if (e.getX() > anchorPoint.x)
							{
								selectedModel.setX(anchorPoint.x);
							}
						}
						else if (preXGap > 0 && e.getX() < anchorPoint.x) // coming from the right
						{
							selectedModel.setX(e.getX());
						}
					}
					if (resizingVertically)
					{
						selectedModel.setHeight(newHeight);
						if (preYGap < 0) //coming from the top
						{
							selectedModel.setY(e.getY());
							if (e.getY() > anchorPoint.y)
							{
								selectedModel.setY(anchorPoint.y);
							}
						}
						else if(preYGap > 0 && e.getY() < anchorPoint.y)
						{
							selectedModel.setY(e.getY());
						}
					}
					preXGap = e.getX() - anchorPoint.x;
					preYGap = e.getY() - anchorPoint.y;
				} 
			}
		}
		
		
		@Override
		public void mousePressed(MouseEvent e) {
			//Allows user to select shape in background by double clicking
			if (e.getClickCount() > 1) {
				for (DShape s : shapes) {
					Rectangle bounds = s.getBounds();
					if (bounds.contains(e.getPoint()) && s != selected) {
						setSelected(s);
						break;
					}
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
			
			if (selected != null && selected.getBiggerBounds().contains(e.getPoint())) {
				
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
					boolean left = resizePoint.x > selected.getX() - KNOB_SIZE/2 && resizePoint.x < selected.getX() + KNOB_SIZE/2;
					boolean right = resizePoint.x > selected.getX() + selected.getWidth() - KNOB_SIZE/2 && resizePoint.x < selected.getX() + selected.getWidth() + KNOB_SIZE/2;
					boolean top = resizePoint.y > selected.getY() - KNOB_SIZE/2 && resizePoint.y < selected.getY() + KNOB_SIZE/2;
					boolean bottom = resizePoint.y > selected.getY() + selected.getHeight() - KNOB_SIZE/2 && resizePoint.y < selected.getY() + selected.getHeight() + KNOB_SIZE/2;
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
					preXGap = (left) ?  selected.getWidth() * -1 : selected.getWidth();
					preYGap = (top)? selected.getHeight() * -1 : selected.getHeight();
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
				Rectangle smallBounds = selected.getBounds();
				Rectangle bigBounds = selected.getBiggerBounds();
				if (bigBounds.contains(e.getPoint()) && !smallBounds.contains(e.getPoint())) {
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
		selected = theShape;
		repaint();
		tmodel.fireTableDataChanged();
	}
	
	public DShape getSelected()
	{
		return selected;
	}
	
	private void setSelected(DShape shape)
	{
		selected = shape;	
//		if (selected instanceof DText ) {
//		    whiteboard.enableTextControls();
//        }
//		else {
//		    whiteboard.disableTextControls();
//		}
		repaint();
	}
	
	public void removeShape()
	{
		if (selected != null) {
			shapes.remove(selected);
			if (shapes.size() > 0)
				selected = shapes.get(shapes.size() -1);
			else
				selected = null;
			repaint();	
			tmodel.fireTableDataChanged();
		}
	}
	
	public void moveToFront()
	{
		if (selected != null) {
			shapes.remove(selected);
			shapes.add(shapes.size(), selected);
			repaint();	
			tmodel.fireTableDataChanged();
		}
	}

	public void moveToBack()
	{
		if (selected != null) {
			shapes.remove(selected);
			shapes.add(0, selected);
			repaint();
			tmodel.fireTableDataChanged();
		}
	}
	
	public void updateTextShape(String txt)
	{
	    if (selected != null && selected instanceof DText ) {
	        DText tm = (DText) selected;
	        tm.setText(txt);
	        repaint();
	    }
	}
	
	public ArrayList<DShape> getShapes() {
		return shapes;
	}
	
	public void save(File f) throws FileNotFoundException{
		XMLEncoder out = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(f)));
		DShapeModel[] outputShapes = new DShapeModel[shapes.size()];
		for (int i = 0; i < outputShapes.length; i++) {
			outputShapes[i] = shapes.get(i).getModel();
		}
		out.writeObject(outputShapes);
		out.flush();
		out.close();
	}
	
	public void open(File f) throws FileNotFoundException{
		XMLDecoder in = new XMLDecoder(new BufferedInputStream(new FileInputStream(f)));
		DShapeModel[] inputShapes = (DShapeModel[]) in.readObject();
		clear();
		for (DShapeModel shape : inputShapes) {
			addShape(shape);
		}
		tmodel.fireTableDataChanged();
		in.close();
	}
	
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

	private void clear()
	{
		shapes.clear();
		tmodel.fireTableDataChanged();
		repaint();
	}

	public TableModel getTableModel()
	{
		return tmodel;
	}
}
