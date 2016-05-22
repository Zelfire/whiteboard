import java.awt.*;

public class DText extends DShape
{

	
	@Override
	/**
	 * Draws this text shape
	 * @param g the graphics component to draw this shape
	 */
	public void draw(Graphics g)
	{
	    DTextModel model = (DTextModel) getModel();
	    String text = model.getText();
	    	    
	    Graphics2D g2d = (Graphics2D) g;
	    Font font = computeFont();
	    g2d.setFont(font);
	    g2d.setColor(getColor());
	    
	    Shape clip = g2d.getClip();
	    g2d.setClip(clip.getBounds().intersection(getBounds()));
	    g2d.drawString(text, getX(), getY() + getHeight());
	    g.setClip(clip);
	}
	
	/**
	 * Sets the text
	 * @param newText the new text
	 */
	public void setText(String newText) {
	    DTextModel model = (DTextModel) getModel();
	    model.setText(newText);
	}
	
	/**
	 * Gets the text
	 * @return the text of this shape
	 */
	public String getText() {
		DTextModel textModel = (DTextModel) getModel();
		return textModel.getText();
	}
	
	/**
	 * Private helper method to compute font and its required size
	 * @return the font at the correct size
	 */
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
	
	/**
	 * Sets the font of the text to the given fontname
	 * @param fontName the name of the new font
	 */
	public void setFont(String fontName)
	{
		DTextModel textModel = (DTextModel) getModel();
		textModel.setFontName(fontName);
	}
	
	/**
	 * Gets the font of the text
	 * @return the font name
	 */
	public String getFontName()
	{
		DTextModel textModel = (DTextModel) getModel();
		return textModel.getFontName();
	}
	
}
