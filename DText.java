import java.awt.*;
import java.awt.font.GlyphVector;

public class DText extends DShape
{

	
	@Override
	public void draw(Graphics g)
	{
	    DTextModel model = (DTextModel) getModel();
	    String text = model.getText();
	    String fontName = model.getFontName();
	    	    
	    Graphics2D g2d = (Graphics2D) g;
	    Font font = computeFont();
	    g2d.setFont(font);
	    Shape clip = g2d.getClip();
	    g2d.setClip(clip.getBounds().intersection(getBounds()));
	    g2d.drawString(text, getX(), getY() + getHeight());
	    g.setClip(clip);
	}
	public void setText(String newText) {
	    DTextModel model = (DTextModel) getModel();
	    model.setText(newText);
	}
	
	public String getText() {
		DTextModel textModel = (DTextModel) getModel();
		return textModel.getText();
	}
	
	private Font computeFont()
	{
		double size = 1.0;
		double previousSize = 1.0;
		Font font = new Font(getFontName(),Font.PLAIN, (int) size);
		FontMetrics fm = new FontMetrics(font) {};
		while (fm.getHeight() <= getHeight())
		{
			previousSize = size;
			size = (size * 1.1) + 1;
			Font biggerFont = font.deriveFont((float)size);
			fm = new FontMetrics(biggerFont) {};
		}
		return font.deriveFont((float)previousSize);
	}
	
	public void setFont(String fontName)
	{
		DTextModel textModel = (DTextModel) getModel();
		textModel.setFontName(fontName);
	}
	
	public String getFontName()
	{
		DTextModel textModel = (DTextModel) getModel();
		return textModel.getFontName();
	}
	
}
