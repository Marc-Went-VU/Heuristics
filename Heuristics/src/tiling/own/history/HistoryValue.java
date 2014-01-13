package tiling.own.history;

import tiling.Tile;

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

	@Override
	public boolean equals(Object o)
	{
		if (o instanceof HistoryValue)
		{
			HistoryValue hv = (HistoryValue)o;
			return this.getTile() == hv.getTile() && this.getX() == hv.getX() && this.getY() == hv.getY();
		}
		return false;
	}
}
