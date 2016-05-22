import java.awt.*;
import java.util.*;


public class DShapeModel
{
	private Rectangle bound;
	private Color color;
	private Point anchor;
	private ArrayList<ModelListener> listeners;
	private int ID;
	
	/**
	 * Constructs a DShapeModel with no height or width and color grey 
	 */
	public DShapeModel() {
		bound = new Rectangle(0,0,0,0);
		color = Color.GRAY;
		listeners = new ArrayList<>();
	}
	
	/**
	 * Gets the integer id that identifies this shape model
	 * @return the int id of this shape model
	 */
	public int getID() {
		return ID;
	}
	
	/**
	 * Sets the id number of this shape model
	 * @param ID the new id for this shape
	 */
	public void setID(int ID) {
		this.ID = ID;
	}
	
	/**
	 * Gets the x coordinate of the top left corner of the bounds
	 * @return the x coordinate of the top left corner of the bounds
	 */
	public int getX() {
		return bound.x;
	}
	
	/**
	 * Gets the y coordinate of the top left corner of the bounds
	 * @return the y coordinate of the top left corner of the bounds
	 */
	public int getY() {
		return bound.y;
	}
	
	/**
	 * Gets the width of this shape 
	 * @return the width of the shape
	 */
	public int getWidth() {
		return bound.width;
	}
	
	/**
	 * Gets the height of this shape
	 * @return the height of this shape
	 */
	public int getHeight() {
		return bound.height;
	}
	
	/**
	 * Gets the color of this shape
	 * @return the color of this shape
	 */
	public Color getColor() {
		return color;
	}
	
	/**
	 * The rectangle that bounds this shape
	 * @return the bounds of this shape
	 */
	public Rectangle getBounds()
	{
		return bound;
	}
	
	/**
	 * Sets the x coordinate of the top left corner of the bounds
	 * @param newX the new x coordinate of the top left corner 
	 */
	public void setX(int newX) {
		bound.x = newX;
		notifyModelChange();
	}
	
	/**
	 * Sets the y coordinate of the top left corner of the bounds
	 * @param newY the new y coordinate of the top left corner 
	 */
	public void setY(int newY) {
		bound.y = newY;
		notifyModelChange();
	}
	
	/**
	 * Sets the width of this shape
	 * @param newWidth the new width of this shape 
	 */
	public void setWidth(int newWidth) {
		bound.width = Math.abs(newWidth);
		notifyModelChange();
	}
	
	/**
	 * Sets the height of this shape
	 * @param newHeight the new width of this shape 
	 */
	public void setHeight(int newHeight) {
		bound.height = Math.abs(newHeight);
		notifyModelChange();
	}
	
	/**
	 * Sets the color of this shape
	 * @param newColor the new color of this shape
	 */
	public void setColor(Color newColor) {
		color = newColor;
		notifyModelChange();
	}
	
	/**
	 * Adds a ModelListener to listens to operations performed on the model
	 * @param listener the ModelListener to add
	 */
	public void addModelListener(ModelListener listener)
	{
		listeners.add(listener);
	}
	
	/**
	 * Removes a ModelListener from this object
	 * @param listener the ModelListener to remove
	 */
	public void removeModelListener(ModelListener listener) {
		listeners.remove(listener);
	}
	
	/**
	 * Sets the anchor of this model. Used for resizing purposes.
	 * @param p the point that should be the anchor of this shape 
	 */
	public void setAnchor(Point p)
	{
		anchor = p;
	}
	
	/**
	 * Gets the anchor of this shape
	 * @return the anchor of this shape
	 */
	public Point getAnchor()
	{
		return anchor;
	}
	
	/**
	 * Notifies listeners that this model has been changed
	 */
	public void notifyModelChange()
	{
		for (ModelListener ml: listeners)
		{
			ml.modelChanged(this);
		}
	}
	
	/**
	 * Notifies listeners that this model has been selected
	 */
	public void notifyModelSelected() {
		for (ModelListener ml: listeners)
		{
			ml.modelSelected(this);
		}
	}
	
	/**
	 * Makes this model a copy another DShapeModel
	 * @param other the other model to copy
	 */
	public void mimic(DShapeModel other) {
		this.bound = other.bound;
		this.color = other.color;
		this.anchor = other.anchor;
		notifyModelChange();
	}
	
	/**
	 * Sets the bounds of this shape
	 * @param newBounds the rectangle that will bound this shape
	 */
	public void setBounds(Rectangle newBounds) {
		bound = newBounds;
		notifyModelChange();
	}
}
