import java.awt.*;
import java.util.*;


public class DShapeModel
{
	private Rectangle bound;
	private Color color;
	private Point anchor;
	private ArrayList<ModelListener> listeners;
	private int ID;
	
	public DShapeModel() {
		bound = new Rectangle(0,0,0,0);
		color = Color.GRAY;
		listeners = new ArrayList<>();
	}
	
	public int getID() {
		return ID;
	}
	
	public void setID(int ID) {
		this.ID = ID;
	}
	
	public int getX() {
		return bound.x;
	}
	
	public int getY() {
		return bound.y;
	}
	
	public int getWidth() {
		return bound.width;
	}
	
	public int getHeight() {
		return bound.height;
	}
	
	public Color getColor() {
		return color;
	}
	
	public Rectangle getBounds()
	{
		return bound;
	}
	
	public void setX(int newX) {
		bound.x = newX;
		notifyListeners();
	}
	
	public void setY(int newY) {
		bound.y = newY;
		notifyListeners();
	}
	
	public void setWidth(int newWidth) {
		bound.width = Math.abs(newWidth);
		notifyListeners();
	}
	
	public void setHeight(int newHeight) {
		bound.height = Math.abs(newHeight);
		notifyListeners();
	}
	
	public void setColor(Color newColor) {
		color = newColor;
		notifyListeners();
	}
	
	public void addModelListener(ModelListener listener)
	{
		listeners.add(listener);
	}
	
	public void removeModelListener(ModelListener listener) {
		listeners.remove(listener);
	}
	
	public void setAnchor(Point p)
	{
		anchor = p;
	}
	
	public Point getAnchor()
	{
		return anchor;
	}
	
	private void notifyListeners()
	{
		for (ModelListener ml: listeners)
		{
			ml.modelChanged(this);
		}
	}
	
	
}
