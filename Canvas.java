import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

public class Canvas extends JPanel
{
	private ArrayList<DShape> shapes;
	private DShape selected;
	
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
						selectedModel.setX(e.getX() - selected.getWidth() / 2);
						selectedModel.setY(e.getY() - selected.getHeight() / 2);
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
				//Allows user to select shape in background by double clicking
				if (e.getClickCount() > 1) {
					for (DShape s : shapes) {
						Rectangle bounds = s.getBounds();
						if (bounds.contains(e.getPoint()) && s != selected) {
							selected = s;
							break;
						}
					}
				}
				else {
					for (DShape s : shapes) {
						Rectangle bounds = s.getBounds();
						if (bounds.contains(e.getPoint())) {
							selected = s;
						}
					}
				}
				
				repaint();
				
			}
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			@Override
			public void mousePressed(MouseEvent e) {
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
			if (shape.equals(selected))
			{
				Rectangle bound = shape.getBounds();
				g.setColor(Color.green);
				int offset = 6;
				if (shape instanceof DOVal)
				{
					g.drawOval(bound.x - (offset/2), bound.y - (offset/2), bound.width + offset, bound.height + offset);
				}
				else
				{
					g.drawRect(bound.x - (offset/2), bound.y - (offset/2), bound.width + offset, bound.height + offset);
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
		shapes.remove(selected);
		repaint();
	}
	
	public void moveToFront()
	{
		shapes.remove(selected);
		shapes.add(shapes.size()-1, selected);
		repaint();
	}
	
}
