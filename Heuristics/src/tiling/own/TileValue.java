package tiling.own;

import tiling.Tile;

public class TileValue
{
	private Tile t;
	private Coordinate c;

	public TileValue(Tile t, Coordinate c)
	{
		this.t = t;
		this.c = new Coordinate(c);
	}

	public Tile getTile()
	{
		return this.t;
	}

	public Coordinate getCoordinate()
	{
		return this.c;
	}

	@Override
	public boolean equals(Object o)
	{
		if (o instanceof TileValue)
		{
			TileValue hv = (TileValue)o;
			return this.getTile().equals(hv.getTile()) && this.c.equals(hv.getCoordinate());
		}
		return false;
	}

	@Override
	public String toString()
	{
		return "{"
			+ t.toString()
			+ ", "
			+ c.toString()
			+ " - "
			+ Integer.toHexString(System.identityHashCode((Object)this.getTile()))
			+ "}";
	}
}
