import java.awt.*;
import java.util.ArrayList;


public class DLine extends DShape
{

	@Override
	public void draw(Graphics g)
	{
		DLineModel model = (DLineModel) getModel();
		Point p1 = model.getP1();
		Point p2 = model.getP2();
		Graphics2D g2d = (Graphics2D) g;
		g.setColor(getColor());
		g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
	}
	
	@Override
	public ArrayList<Point> getKnobs() {
		DLineModel model = (DLineModel) getModel();
		ArrayList<Point> knobs = new ArrayList<>();
		knobs.add(model.getP1());
		knobs.add(model.getP2());
		return knobs;
	}
	

}
