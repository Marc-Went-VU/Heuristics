package tiling.own;

import java.util.ArrayList;
import tiling.Tile;

public class History
{
	private ArrayList<HistoryValue> list;
	private int last;

	public History()
	{
		list = new ArrayList<HistoryValue>();
		last = 0;
	}

	public void add(Tile t, int x, int y)
	{
		HistoryValue hv = new HistoryValue(t, x, y);
		if (list.add(hv))
			last++;

	}

	public HistoryValue undo()
	{
		return list.remove(--last);
	}

	public class HistoryValue
	{
		private Tile t;
		private int x;
		private int y;

		public HistoryValue(Tile t, int x, int y)
		{
			this.t = t;
			this.x = x;
			this.y = y;
		}

		public Tile getTile()
		{
			return this.t;
		}

		public int getX()
		{
			return this.x;
		}

		public int getY()
		{
			return this.y;
		}
	}
}
