import java.awt.*;
import java.awt.Graphics;

public abstract class DShape implements ModelListener
{
	private DShapeModel shapeModel;
	private Canvas view;
	
	public DShape() {
		
	}
	
	public DShape(DShapeModel model) {
		shapeModel = model;
		model.addModelListener(this);
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
	
	public Rectangle getBounds()
	{
		return shapeModel.getBounds();
	}
	
	public void attachView(Canvas view)
	{
		this.view = view;
	}
	
	@Override
	public void modelChanged(DShapeModel model) {
		view.repaint();
	}
	
	public DShapeModel getModel()
	{
		return shapeModel;
	}
}
