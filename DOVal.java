import java.awt.*;
import java.awt.geom.Ellipse2D;


public class DOVal extends DShape
{
	
	public DOVal(DOvalModel shapeModel) {
		super(shapeModel);
	}

	@Override
	public void draw(Graphics g)
	{
		Shape oval = new Ellipse2D.Double(getX(), getY(), getWidth(), getHeight());
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(getColor());
		g2d.fill(oval);
		
	}

}
