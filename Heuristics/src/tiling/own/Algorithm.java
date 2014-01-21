package tiling.own;

import tiling.Field;
import tiling.Tile;
import tiling.TileSet;
import tiling.TilingFrame;
import tiling.own.TileList.SORT;
import tiling.own.history.History;

public class Algorithm
{
	public static final int DELAY = 200;
	public static final boolean DEBUG = false;

	private TilingFrame frame;
	private Field field;
	private TileList list;
	private History history;

	private int numSearchSteps;
	private int maxSteps = -1;

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
		algo(0, 0, DIR.HEIGHT);
		list.printFree();
	}

	private void algo(int x, int y, DIR d)
	{

		int maxWidth = getBiggest(x, y, d);
		Tile t = getBiggestTile(maxWidth, d);
		if (t == null)
			return;
		if (field.placeTileSecure(t, x, y))
		{
			list.setUsed(t);
			frame.redraw(DELAY);
		}

		algo(x, y + t.getHeight(), d);
		algo(x + t.getWidth(), y, d);
	}

	private Tile getBiggestTile(int maxSize, DIR d)
	{
		Tile t = null;
		if (d == DIR.WIDTH)
		{
			while (maxSize > 0 && (t = list.getByWidth(maxSize, SORT.AREA)) == null)
			{
				maxSize--;
			}
		}
		else
		{
			while (maxSize > 0 && (t = list.getByHeight(maxSize, SORT.AREA)) == null)
			{
				maxSize--;
			}
		}
		return t;
	}

	private int getBiggest(int x, int y, DIR d)
	{
		int size = 0;

		while (x < field.getWidth() && y < field.getHeight() && !field.isOccupied(x, y))
		{
			size++;
			if (d == DIR.WIDTH)
			{
				x++;
				if (x >= field.getWidth())
					return size;
			}
			else
			{
				y++;
				if (y >= field.getHeight())
					return size;
			}
		}
		return size;
	}

	public enum DIR
	{
		WIDTH,
		HEIGHT
	}
}
