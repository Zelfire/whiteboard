import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

/**
 * An abstract table model that pulls data from the DShapes on a Canvas
 */
public class ShapeTableModel extends AbstractTableModel implements ModelListener
{
	private Canvas canvas;
	private static final int NUM_COLUMNS = 4;
	private static final int X_COLUMN = 0;
	private static final int Y_COLUMN = 1;
	private static final int WIDTH_COLUMN = 2;
	
	/**
	 * Creates a ShapeTableModel with no canvas reference
	 */
	public ShapeTableModel() {
		canvas = null;
	}

	@Override
	public String getColumnName(int col) {
		if (col == X_COLUMN)
			return "X";
		else if (col == Y_COLUMN)
			return "Y";
		else if (col == WIDTH_COLUMN)
			return "WIDTH";
		else
			return "HEIGHT";
	}
	
	/**
	 * Sets the canvas for this table model to pull data from
	 * @param canvas the canvas to set
	 */
	public void setCanvas(Canvas canvas) {
		this.canvas = canvas;
	}
	
	@Override
	/**
	 * Gets the number of columns the table has
	 * @return the number of columns
	 */
	public int getColumnCount()
	{
		return NUM_COLUMNS;
	}

	@Override
	/**
	 * Gets the number of rows that the table has
	 * @return the number of rows
	 */
	public int getRowCount()
	{
		if (canvas != null)
			return canvas.getShapes().size();
		else
			return 0;
	}

	@Override
	/**
	 * Gets the value at the given row and column of the table
	 * @param x the row
	 * @param y the column
	 */
	public Object getValueAt(int x, int y)
	{
		DShapeModel model = canvas.getShapes().get(x).getModel();
		if (y == X_COLUMN)
			return model.getX();
		else if (y == Y_COLUMN)
			return model.getY();
		else if (y == WIDTH_COLUMN)
			return model.getWidth();
		else
			return model.getHeight();
	}

	@Override
	/**
	 * Updates the table when the given model has changed
	 * @param model the model that has changed
	 */
	public void modelChanged(DShapeModel model)
	{
		ArrayList<DShape> shapes = canvas.getShapes();
		for (int i = 0; i < shapes.size(); i++) {
			if (shapes.get(i).getModel() == model) {
				fireTableRowsUpdated(i, i);
				break;
			}
		}
	}
	
	@Override
	/**
	 * Empty implementation of ModelListeners modelSelected
	 * @param model the model that has been selected
	 */
	public void modelSelected(DShapeModel model) {
		/*Potential for future implementation to allow users to change
		 * the selected shape through the table 
		 */
		
	}
}
