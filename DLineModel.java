import java.awt.*;

public class DLineModel extends DShapeModel
{
	private Point p1;
	private Point p2;
	
	
	public DLineModel()
	{
		super();
		p1 = new Point();
		p2 = new Point();
		super.setAnchor(p1);
	}
	
	public Point getP1() {
		return p1;
	}
	
	public Point getP2() {
		return p2;
	}
	
	@Override
	public int getWidth() {
		return Math.abs(p1.x -p2.x);
	}
	
	@Override
	public int getHeight() {
		return Math.abs(p1.y - p2.y);
	}
	
	@Override
	public int getX() {
		if (p1.x < p2.x)
			return p1.x;
		else
			return p2.x;
	}
	
	public int getY() {
		if (p1.y < p2.y)
			return p1.y;
		else
			return p2.y;
	}
	
	@Override
	public void setX(int newX) {
		int xChange = newX - getX();
		p1.x += xChange;
		p2.x += xChange;
		boundPoints();
	}
	
	@Override
	public void setY(int newY) {
		int yChange = newY - getY();
		p1.y += yChange;
		p2.y += yChange;
		boundPoints();
	}
	
	@Override
	public void setWidth(int newWidth) {
		if (p1.equals(super.getAnchor()))
			p2.x = p1.x + newWidth;
		else
			p1.x = p2.x + newWidth;
		boundPoints();
	}
	
	@Override
	public void setHeight(int newHeight) {
		if (p1.equals(super.getAnchor()))
			p2.y = p1.y + newHeight;
		else
			p1.y = p2.y + newHeight;
		boundPoints();
	}
	
	@Override
	public void setAnchor(Point p) {
		if (!p.equals(p1) || !p.equals(p2))
		{
			if (p.x == p1.x || p.y == p1.y)
				super.setAnchor(new Point(p1.x, p1.y));
			else 
				super.setAnchor(new Point(p2.x, p2.y));
		}
	}
	
	private void boundPoints()
	{
		super.setX((p1.x < p2.x)? p1.x : p2.x);
		super.setY((p1.y < p2.y)? p1.y : p2.y);
		super.setWidth(this.getWidth());
		super.setHeight(this.getHeight());
		
	}

	@Override
	public void mimic(DShapeModel other) {
		DLineModel line = (DLineModel) other;
		p1 = line.p1;
		p2 = line.p2;
		super.mimic(other);
	}
}
