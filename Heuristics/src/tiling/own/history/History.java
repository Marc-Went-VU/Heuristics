package tiling.own.history;

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

	public void add(HistoryValue hv)
	{
		if (list.add(hv))
			last++;
	}

	public HistoryValue undo()
	{
		if (last > 0)
			return list.remove(--last);
		else
			return null;
	}

	public boolean contains(HistoryValue hv)
	{
		for (int i = list.size() - 1; i >= 0; i--)
		{
			if (list.get(i).equals(hv))
				return true;
		}
		return false;
		//				return list.contains(hv);
	}

	public boolean contains(Tile t, int x, int y)
	{
		for (int i = list.size() - 1; i >= 0; i--)
		{
			HistoryValue hv = list.get(i);
			if (hv.getTile().equals(t))
				return true;
		}
		return false;
	}
}
