import java.util.ArrayList;


public class CanvasAdapter
{
	private Canvas view;
	
	public CanvasAdapter()
	{
		view = null;
	}
	
	public void setCanvas(Canvas newCanvas) {
		view = newCanvas;
	}

	public int size()
	{
		return view.getShapes().size();
	}

	public DShape get(int x)
	{
		ArrayList<DShape> canvasShapes = view.getShapes();
		return canvasShapes.get(x);
	}

	public ArrayList<DShape> getShapes()
	{
		return view.getShapes();
	}

}
