

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
    
    public static void setText(String newText) {
        text = newText;
        
    }

}
