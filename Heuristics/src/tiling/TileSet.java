package tiling;

import java.util.ArrayList;

public class TileSet extends ArrayList<Tile>
{
	private static final long serialVersionUID = -6861125589640362637L;
	private int numTileSizes;

	public TileSet()
	{
		super();

		numTileSizes = 0;
	}

	/**
	 * This method gets the tile at a certain index and removes it from the set
	 */
	public Tile get(int index)
	{
		Tile tile = super.get(index);
		this.remove(index);

		if (!contains(tile))
		{
			numTileSizes--;
		}

		return tile;
	}

	/**
	 * Peek at a tile at a certain index
	 * 
	 * @param index
	 * @return
	 */
	public Tile peek(int index)
	{
		return super.get(index);
	}

	@Override
	public boolean add(Tile e)
	{
		if (!contains(e))
		{
			numTileSizes++;
		}

		return super.add(e);
	}

	public int getNumberOfTileSizes()
	{
		return numTileSizes;
	}
}
