import java.awt.Color;
import java.awt.Graphics;

public abstract class DShape
{
	private DShapeModel shapeModel;
	
	public DShape() {
		
	}
	
	public DShape(DShapeModel model) {
		shapeModel = model;
	}
	
	public abstract void draw(Graphics g);
	
	public int getX() {
		return shapeModel.getX();
	}

	public int getY() {
		return shapeModel.getY();
	}

	public int getWidth() {
		return shapeModel.getWidth();
	}

	public int getHeight() {
		return shapeModel.getHeight();
	}
	
	public Color getColor() {
		return shapeModel.getColor();
	}
	
	
}
