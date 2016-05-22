

public class DTextModel extends DShapeModel
{
    private String text;
    private String fontName;
    private String defaultText = "Hello";
    private String defaultFontName = "Dialog";
    
    /**
     * Creates a text model with a default text and font name
     */
    public DTextModel()
    {
        text = defaultText;
        fontName = defaultFontName;
    }
    
    /**
     * Gets the text of this model
     * @return the text
     */
    public String getText() {
        return text;
    }
    
    /**
     * Gets the font name of this model
     * @return the font name
     */
    public String getFontName() {
        return fontName;
    }
    
    /**
     * Sets the text of this model
     * @param newText the new text
     */
    public void setText(String newText) {
        text = newText;
        notifyModelChange();
    }
    
    /**
     * Sets the font of this model
     * @param f the font name
     */
    public void setFontName(String f)
    {
    	fontName =f;
    	notifyModelChange();
    }
    
    @Override
	/**
	 * Makes this text shape a copy of another text shape
	 * @param other the text shape to copy
	 */
    public void mimic(DShapeModel other) {
    	DTextModel otherText = (DTextModel) other;
    	text = otherText.text;
    	fontName = otherText.fontName;
    	super.mimic(other);
    }
}
