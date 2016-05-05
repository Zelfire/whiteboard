import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class ShapeTableModel extends AbstractTableModel implements ModelListener
{
	private CanvasAdapter adapter;
	private static final int NUM_COLUMNS = 4;
	private static final int X_COLUMN = 0;
	private static final int Y_COLUMN = 1;
	private static final int WIDTH_COLUMN = 2;
	
	public ShapeTableModel() {
		adapter = null;
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
	
	public void setAdapter(CanvasAdapter adapter) {
		this.adapter = adapter;
	}
	
	@Override
	public int getColumnCount()
	{
		return NUM_COLUMNS;
	}

	@Override
	public int getRowCount()
	{
		if (adapter != null)
			return adapter.size();
		else
			return 0;
	}

	@Override
	public Object getValueAt(int x, int y)
	{
		DShapeModel model = adapter.get(x).getModel();
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
	public void modelChanged(DShapeModel model)
	{
		ArrayList<DShape> shapes = adapter.getShapes();
		for (int i = 0; i < shapes.size(); i++) {
			if (shapes.get(i).getModel() == model)
				fireTableRowsUpdated(i, i);
		}
	}
}
