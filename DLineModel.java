import java.awt.*;
import java.beans.Transient;

public class DLineModel extends DShapeModel
{
	private Point p1;
	private Point p2;
	
	/**
	 * Constructs a DLineModel with null points
	 */
	public DLineModel()
	{
		super();
		p1 = new Point();
		p2 = new Point();
		super.setAnchor(p1);
	}
	
	/**
	 * Sets the position of the first endpoint of this line 
	 * @param newP the point where the endpoint should be 
	 */
	public void setP1(Point newP) {
		p1 = newP;
		boundPoints();
	}
	
	/**
	 * Sets the position of the second endpoint of this line
	 * @param newP the point where the endpoint should be
	 */
	public void setP2(Point newP) {
		p2 = newP;
		boundPoints();
	}
	
	/**
	 * Returns the first endpoint
	 * @return the first endpoint
	 */
	public Point getP1() {
		return p1;
	}
	
	/**
	 * Returns the second endpoint
	 * @return the second endpoint
	 */
	public Point getP2() {
		return p2;
	}
	
	@Override
	@Transient
	/**
	 * Changes the top left corner of the bounds of this line to given X location
	 * @param newX the x to move the bounds to
	 */
	public void setX(int newX) {
		int xChange = newX - getX();
		p1.x += xChange;
		p2.x += xChange;
		boundPoints();
	}
	
	@Override
	@Transient
	/**
	 * Changes the top left corner of the bounds of this line to given Y location
	 * @param newY the y to move the bounds to
	 */
	public void setY(int newY) {
		int yChange = newY - getY();
		p1.y += yChange;
		p2.y += yChange;
		boundPoints();
	}
	
	@Override
	@Transient
	/**
	 * Sets the horizontal distance between these two points
	 * @param newWidth the new width of this line  
	 */
	public void setWidth(int newWidth) {
		if (p1.equals(super.getAnchor()))
			p2.x = p1.x + newWidth;
		else
			p1.x = p2.x + newWidth;
		boundPoints();
	}
	
	@Override
	@Transient
	/**
	 * Sets the vertical distance between these two points
	 * @param newHeight the new height of this line
	 */
	public void setHeight(int newHeight) {
		if (p1.equals(super.getAnchor()))
			p2.y = p1.y + newHeight;
		else
			p1.y = p2.y + newHeight;
		boundPoints();
	}
	
	@Override
	/**
	 * Sets the anchor of this line to one of the endpoints. The anchor does not move during resizing
	 * @param p the point that should be the anchor of this line
	 */
	public void setAnchor(Point p) {
		if (!p.equals(p1) || !p.equals(p2))
		{
			if (p.x == p1.x || p.y == p1.y)
				super.setAnchor(new Point(p1.x, p1.y));
			else 
				super.setAnchor(new Point(p2.x, p2.y));
		}
	}
	
	/**
	 * Establishes the bounds around the two endpoints
	 */
	private void boundPoints()
	{
		Rectangle newBounds = new Rectangle(Math.min(p1.x, p2.x), Math.min(p1.y,p2.y), Math.abs(p1.x - p2.x), Math.abs(p1.y - p2.y));
		setBounds(newBounds);
	}

	@Override
	/**
	 * Makes this line a copy of another line
	 * @param other the line to copy
	 */
	public void mimic(DShapeModel other) {
		DLineModel line = (DLineModel) other;
		p1 = line.p1;
		p2 = line.p2;
		super.mimic(other);
	}
}
