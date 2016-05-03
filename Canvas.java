import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

public class Canvas extends JPanel
{
	private ArrayList<DShape> shapes;
	private DShape selected;
	private int preXGap; //preXGap and preYGap should probably be moved someplace else
	private int preYGap;
	
	public Canvas() {
		int INITIAL_WIDTH = 400;
		int INITIAL_HEIGHT = 400;
		setPreferredSize(new Dimension(INITIAL_WIDTH, INITIAL_HEIGHT));
		setBackground(Color.WHITE);
		this.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseDragged(MouseEvent e)
			{
				if (selected != null) { 
					if (selected.getBounds().contains(e.getPoint())) { 
						DShapeModel selectedModel = selected.getModel(); 
					    selectedModel.setX(e.getX() - preXGap); 
					    selectedModel.setY(e.getY() - preYGap); 
		 			} 
				} 
			}

			@Override
			public void mouseMoved(MouseEvent e)
			{
			}
			
		});
		this.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
			}
			@Override
			public void mouseReleased(MouseEvent e) {
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
				
				//Set the distance between the clicked point and the bounded rectangle of the shape (Used for shape movement when dragging mouse)
				if (selected.getBounds().contains(e.getPoint())) {
					preXGap = e.getX() - selected.getX();
					preYGap = e.getY() - selected.getY();	
				}
			}
			@Override
			public void mouseExited(MouseEvent e) {	
			}
			@Override
			public void mouseEntered(MouseEvent e) {
			}
		});
		shapes = new ArrayList<>();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (DShape shape : shapes) {
			shape.draw(g);
			if (shape.equals(selected)) {
				final int KNOB_SIZE = 9;
				ArrayList<Point> knobs = shape.getKnobs();
				g.setColor(Color.BLACK);
				for (int i = 0; i < knobs.size(); i++) {
					g.fillRect(knobs.get(i).x - KNOB_SIZE / 2, knobs.get(i).y - KNOB_SIZE / 2, KNOB_SIZE, KNOB_SIZE);	
				}
			}
		}
	}
	
	public void addShape(DShapeModel shapeModel) {
		DShape theShape;
		if (shapeModel instanceof DRectModel) {
			theShape = new DRect();
			theShape.setModel(shapeModel);
		}
		else if (shapeModel instanceof DOvalModel) {
			theShape = new DOVal();
			theShape.setModel(shapeModel);
		}
		else if (shapeModel instanceof DTextModel) {
			theShape = new DText(); 
		}
		else{
			theShape = new DLine();
		}
		theShape.attachView(this);
		shapes.add(theShape);
		selected = theShape;
		repaint();
	}
	
	public DShape getSelected()
	{
		return selected;
	}
	
	public void setSelected(DShape shape)
	{
		selected = shape;
		repaint();
	}
	
	public void removeShape()
	{
		if (selected != null) {
			shapes.remove(selected);
			selected = null;
			repaint();	
		}
	}
	
	public void moveToFront()
	{
		if (selected != null) {
			shapes.remove(selected);
			shapes.add(shapes.size(), selected);
			repaint();	
		}
	}

	public void moveToBack()
	{
		if (selected != null) {
			shapes.remove(selected);
			shapes.add(0, selected);
			repaint();
		}
	}
	
}
