import java.awt.Color;


public class DShapeModel
{
	private int x;
	private int y;
	private int width;
	private int height;
	private Color color;
	
	public DShapeModel() {
		x = 0;
		y = 0;
		width = 0;
		height = 0;
		color = Color.GRAY;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setX(int newX) {
		x = newX;
	}
	
	public void setY(int newY) {
		y = newY;
	}
	
	public void setWidth(int newWidth) {
		width = newWidth;
	}
	
	public void setHeight(int newHeight) {
		height = newHeight;
	}
	
	public void setColor(Color newColor) {
		color = newColor;
	}
}
