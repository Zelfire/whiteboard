import java.awt.*;

public class DText extends DShape
{

	@Override
	public void draw(Graphics g)
	{
	    DTextModel model = (DTextModel) getModel();
	    String text = model.getText();
	    String fontName = model.getFontName();
	    	    
	    Graphics2D g2d = (Graphics2D) g;
  
	    g2d.drawString(text, getX() - 4, getY() + 14); 
	    
	}
	
}
