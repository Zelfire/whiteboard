import java.awt.*;

public class DRect extends DShape
{
	@Override
	public void draw(Graphics g) {
		Rectangle rect = new Rectangle(getX(), getY(), getWidth(), getHeight());
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(getColor());
		g2d.fill(rect);
	}
	
}
