package tiling.own;

import tiling.Field;
import tiling.Tile;
import tiling.TileSet;
import tiling.TilingFrame;
import tiling.own.history.History;

public class Algorithm
{
	public static final int DELAY = 200;
	public static final boolean DEBUG = false;
	private TilingFrame frame;
	private Field field;
	private TileList list;
	private History history;

	//	private History undoneHistory;

	public Algorithm(TilingFrame frame, Field field, TileSet tiles)
	{
		this.frame = frame;
		this.field = field;
		this.list = new TileList(tiles);
		this.history = new History();
		//		this.undoneHistory = new History();
	}

	public void runAlgorithm()
	{
		Coord c = new Coord(0, 0);
		algo(field, c, c);
	}

	private void algo(Field f, Coord c, Coord c2)
	{
		Tile tile1 = list.getFirstAvailable();
		if (f.placeTileSecure(tile1, c.getX(), c.getY()))
		{
			list.setUsed(tile1);
			algo(f, new Coord(c.getX() + tile1.getWidth(), c.getY()), new Coord(c.getX(), c.getY() + tile1.getHeight()));
		}
		tile1 = list.getFirstAvailable();
		if (c2 != null && f.placeTileSecure(tile1, c2.getX(), c2.getY()))
		{
			list.setUsed(tile1);
			algo(f, new Coord(c2.getX() + tile1.getWidth(), c2.getY()), new Coord(c2.getX(), c2.getY() + tile1.getHeight()));
		}
		list.printFree();
	}

	private class Coord
	{
		private int y;
		private int x;

		Coord(int x, int y)
		{
			this.setX(x);
			this.setY(y);
		}

		public int getX()
		{
			return x;
		}

		public void setX(int x)
		{
			this.x = x;
		}

		public int getY()
		{
			return y;
		}

		public void setY(int y)
		{
			this.y = y;
		}

	}
}
