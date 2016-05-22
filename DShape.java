import java.awt.*;
import java.util.ArrayList;

public abstract class DShape implements ModelListener
{
	private DShapeModel shapeModel;
	private Canvas view;
	
	/**
	 * Constructs a DShape with a null shapeModel
	 */
	public DShape() {
		shapeModel = null;
	}
	
	/**
	 * Draws the shape
	 * @param g the graphics component to draw the shape
	 */
	public abstract void draw(Graphics g);
	
	/**
	 * The X coordinate of the upper left corner of this shape
	 * @return The X coordinate of the upper left corner of this shape
	 */
	public int getX() {
		return shapeModel.getX();
	}

	/**
	 * The Y coordinate of the upper left corner of this shape
	 * @return The Y coordinate of the upper left corner of this shape
	 */
	public int getY() {
		return shapeModel.getY();
	}

	/**
	 * Gets the width of this shape
	 * @return the width of this shape
	 */
	public int getWidth() {
		return shapeModel.getWidth();
	}

	/**
	 * Gets the height of this shape
	 * @return the height of this shape
	 */
	public int getHeight() {
		return shapeModel.getHeight();
	}
	
	/**
	 * Gets the color of this shape
	 * @return the color of this shape
	 */
	public Color getColor() {
		return shapeModel.getColor();
	}
	
	/**
	 * Gets the rectangle that bounds this shape
	 * @return the bounds of the shape
	 */
	public Rectangle getBounds()
	{
		return shapeModel.getBounds();
	}
	
	/**
	 * Method to add the canvas to the shape
	 * @param view the canvas that this shape is on
	 */
	public void attachView(Canvas view)
	{
		this.view = view;
	}
	
	@Override
	/**
	 * Repaints the canvas if the model is changed
	 * @param model the model that has changed
	 */
	public void modelChanged(DShapeModel model) {
		view.repaint();
	}
	
	@Override
	/**
	 * Empty implementation of modelSelected method of the model listener interface
	 * @param model the model that has changed
	 */
	public void modelSelected(DShapeModel model) {	
	}
	
	/**
	 * Sets the model of this shape
	 * @param newModel the model of this shape
	 */
	public void setModel(DShapeModel newModel) {
		shapeModel = newModel;
		shapeModel.addModelListener(this);
	}
	
	/**
	 * Gets the model that represents this shape
	 * @return the model of this shape
	 */
	public DShapeModel getModel()
	{
		return shapeModel;
	}
	
	/**
	 * Gets the points where the knobs this shape will be drawn
	 * @return an array list of points of where the knobs should be
	 */
	public ArrayList<Point> getKnobs() {
		ArrayList<Point> knobs = new ArrayList<>();
		Point upperLeft = new Point(getX(), getY());
		Point upperRight = new Point(getX() + getWidth(), getY());
		Point bottomLeft = new Point(getX(), getY() + getHeight());
		Point bottomRight = new Point(getX() + getWidth(), getY() + getHeight());
		knobs.add(upperLeft);
		knobs.add(upperRight);
		knobs.add(bottomLeft);
		knobs.add(bottomRight);
		return knobs;
	}
	
	/**
	 * Gets the rectangle that bounds the outside edges of the knobs
	 * @return the bounds of the knobs
	 */
	public Rectangle getBiggerBounds()
	{
		Rectangle originalBounds = this.getBounds();
		return new Rectangle (originalBounds.x - Canvas.KNOB_SIZE/2, originalBounds.y - Canvas.KNOB_SIZE/2, 
				originalBounds.width + Canvas.KNOB_SIZE, originalBounds.height+Canvas.KNOB_SIZE);
	}
	
}
