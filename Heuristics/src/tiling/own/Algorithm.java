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
		//System.out.println(list.toString());
		//Tile tile = list.getFirst();
		Tile tile = null;
		for (int i = 0; i < field.getHeight(); i++)
		{
			for (int j = 0; j < field.getWidth(); j++)
			{
				if (field.isOccupied(j, i))
					continue;
				Tile tile2 = getBiggestByHeight(i, j);
				tile = getBiggestByWidth(i, j);
				if (tile == null && tile2 != null)
					tile = tile2;
				if (tile != null && tile2 != null)
					tile = tile.getArea() < tile2.getArea() ? tile2 : tile;
				if (tile == null)
					break;
				if ((field.placeTileSecure(tile, j, i) || field.placeTileSecure(tile.rotate(), j, i)))
				{
					list.setUsed(tile);
					history.add(tile, j, i);
				}
				frame.redraw(DELAY);
				//list.printFree();
			}
			if (tile == null)
				break;
		}
		//		System.out.println("Stopped");
		list.printFree();
		if (DEBUG)
			System.out.println(list.toString());

	}

	private Tile getBiggestByWidth(int i, int j)
	{
		int maxWidth = 0;
		Tile tile = null;
		for (int k = j; k < field.getWidth(); k++)
		{
			if (field.isOccupied(k, i))
				break;
			maxWidth++;
		}
		TileList items = null;
		while ((items = list.getListByWidth(maxWidth)) == null && maxWidth > 0)
		{
			maxWidth--;
		}
		if (items == null)
		{
			return null;
		}
		int maxHeight = 0;
		for (int k = i; k < field.getHeight(); k++)
		{
			if (field.isOccupied(j, k))
				break;
			maxHeight++;
		}

		while ((tile = items.getSecureByHeight(maxHeight)) == null && maxHeight > 0)
		{
			maxHeight--;
		}
		return tile;
	}

	private Tile getBiggestByHeight(int i, int j)
	{
		int maxHeight = 0;
		Tile tile = null;
		for (int k = i; k < field.getHeight(); k++)
		{
			if (field.isOccupied(j, k))
				break;
			maxHeight++;
		}
		TileList items = null;
		while ((items = list.getListByHeight(maxHeight)) == null && maxHeight > 0)
		{
			maxHeight--;
		}
		if (items == null)
		{
			return null;
		}
		int maxWidth = 0;
		for (int k = j; k < field.getWidth(); k++)
		{
			if (field.isOccupied(k, i))
				break;
			maxWidth++;
		}
		tile = null;
		while ((tile = items.getSecureByWidth(maxWidth)) == null && maxWidth > 0)
		{
			maxWidth--;
		}
		return tile;
	}

	//	private void undoLastMove(HistoryValue hv)
	//	{
	//		undoneHistory.add(hv);
	//		field.undo(hv.getTile(), hv.getX(), hv.getY());
	//		list.setUnUsed(hv.getTile());
	//	}
}
