import java.awt.*;
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
	
}
