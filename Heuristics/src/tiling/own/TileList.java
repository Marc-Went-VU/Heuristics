package tiling.own;

import tiling.Tile;
import tiling.TileSet;

public class TileList
{

	private int pos;
	private TileSet tileSet;

	public TileList(TileSet ts)
	{
		tileSet = ts;
		pos = 0;
	}

	public boolean hasNext()
	{
		return pos < tileSet.size();
	}

	public Tile current()
	{
		if (pos < 0 || pos >= tileSet.size())
			return null;
		return tileSet.peek(pos);
	}

	public Tile next()
	{
		pos++;
		if (pos >= tileSet.size())
			return null;
		return tileSet.peek(pos);
	}

	public Tile prev()
	{
		pos--;
		if (pos < 0)
			return null;
		return tileSet.peek(pos);
	}

	public Tile first()
	{
		pos = 0;
		return tileSet.peek(pos);
	}

	public boolean remove(Tile t)
	{
		return tileSet.remove(t);
	}

}
