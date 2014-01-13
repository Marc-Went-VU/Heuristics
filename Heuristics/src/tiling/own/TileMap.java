package tiling.own;

import java.util.Comparator;
import java.util.NavigableMap;
import java.util.TreeMap;
import tiling.Tile;
import tiling.TileSet;

public class TileMap extends TreeMap<Tile, Boolean>
{
	private static final long serialVersionUID = 8452947308525236L;

	public TileMap(TileSet tiles)
	{
		super();

		while (tiles.size() >= 1)
		{
			this.put(tiles.get(0), true);
		}
	}

	public TileMap(TileSet tiles, Comparator<Object> reverseOrder)
	{
		super(reverseOrder);
		while (tiles.size() >= 1)
		{
			this.put(tiles.get(0), true);
		}
	}

	public Tile getBySize(int i)
	{
		NavigableMap<Tile, Boolean> ti = this.descendingMap();
		Tile t = ti.firstKey();
		while ((t = ti.higherKey(t)) != null)
		{
			if (!this.get(t))
				continue;
			if (t.getWidth() == i || t.getHeight() == i)
				return t;
		}
		return null;
	}

	public Tile getFirst()
	{
		return this.firstKey();
	}

	public void setUsed(Tile t)
	{
		this.put(t, false);
	}

	public void setUnUsed(Tile t)
	{
		this.put(t, true);
	}

	public Tile getNext(Tile t)
	{
		return this.higherKey(t);
	}

	public Tile getBiggest()
	{
		Tile t = this.firstKey();
		while (!this.get(t))
		{
			t = this.getNext(t);
		}
		return t;
	}
}
