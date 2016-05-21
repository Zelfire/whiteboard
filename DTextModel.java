

public class DTextModel extends DShapeModel
{
    private String text;
    private String fontName;
    private String defaultText = "Hello";
    private String defaultFontName = "Dialog";
    
    public DTextModel()
    {
        text = defaultText;
        fontName = defaultFontName;
    }
    
    public String getText() {
        return text;
    }
    
    public String getFontName() {
        return fontName;
    }
    
    public void setText(String newText) {
        text = newText;
        notifyListeners();
    }
    
    public void setFontName(String f)
    {
    	fontName =f;
    	notifyListeners();
    }
    
    @Override
    public void mimic(DShapeModel other) {
    	DTextModel otherText = (DTextModel) other;
    	text = otherText.text;
    	fontName = otherText.fontName;
    	super.mimic(other);
    }
}
