import java.awt.Font;

public class DTextModel extends DShapeModel
{
    private static String text;
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
    }
    
    public void setFontName(String f)
    {
    	fontName =f;
    }
    
    @Override
    public void mimic(DShapeModel other) {
    	//TODO: Mimic text data;
    }
}
