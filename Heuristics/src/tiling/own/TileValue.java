package tiling.own;

import tiling.Tile;

public class TileValue
{
	private Tile t;
	private int x;
	private int y;

	public TileValue(Tile t, int x, int y)
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
		if (o instanceof TileValue)
		{
			TileValue hv = (TileValue)o;
			return this.getTile() == hv.getTile() && this.getX() == hv.getX() && this.getY() == hv.getY();
		}
		return false;
	}

	@Override
	public String toString()
	{
		return "{" + t.toString() + ", x:" + x + ", y:" + y + "}";
	}
}
