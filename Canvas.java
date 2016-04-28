import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class Canvas extends JPanel
{
	private ArrayList<DShape> shapes;
	
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
		}
	}
	
	public void addShape(DShapeModel shapeModel) {
		if (shapeModel instanceof DRectModel) {
			DRect rectangle = new DRect((DRectModel) shapeModel);
			shapes.add(rectangle);
		}
		else if (shapeModel instanceof DOvalModel) {
			
		}
		else if (shapeModel instanceof DTextModel) {
			
		}
		else if (shapeModel instanceof DLineModel) {
			
		}
		repaint();
	}
}
