import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class Canvas extends JPanel
{
	private ArrayList<DShape> shapes;
	private DShape selected;
	private MouseListener mouseListener;
	
	public Canvas() {
		int INITIAL_WIDTH = 400;
		int INITIAL_HEIGHT = 400;
		setPreferredSize(new Dimension(INITIAL_WIDTH, INITIAL_HEIGHT));
		setBackground(Color.WHITE);
		mouseListener = new MouseListener() {
			int initialX;
			int initialY;
			@Override
			public void mouseReleased(MouseEvent e) {
				if (selected != null) {
					DShape selected = getSelected();
					DShapeModel selectedModel = selected.getModel();
					Rectangle bounds = selectedModel.getBounds();
					if (bounds.contains(new Point(initialX, initialY))) {
						selectedModel.setX(e.getX() - selected.getWidth() / 2);
						selectedModel.setY(e.getY() - selected.getHeight() / 2);
					} 
				}
				
			}
			
			@Override
			//Allows user to also select the shape in the background by double clicking, but it creates the problem that before switching the selection it translates the previous shape
			//We can remove this functionality to select the one behind, but I would like to try to make it work somehow
			public void mousePressed(MouseEvent e) {
				initialX = e.getX();
				initialY = e.getY();
				if (selected != null) {
					Point p = e.getPoint();
					if (!selected.getBounds().contains(p) || e.getClickCount() > 1)
					{
						for (DShape shape : shapes) {
							if (shape.getBounds().contains(p) &&  shape != selected) {
								setSelected(shape);
								break;
							}
						} 
					}
				}
		
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
			}
			
			@Override
			public void mouseExited(MouseEvent e) {	
			}
			@Override
			public void mouseEntered(MouseEvent e) {
			}
		};
		
		this.addMouseListener(mouseListener);
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
			theShape = new DRect((DRectModel) shapeModel);
		}
		else if (shapeModel instanceof DOvalModel) {
			theShape = new DOVal((DOvalModel) shapeModel);
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
