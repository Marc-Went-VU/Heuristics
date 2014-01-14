package tiling.own;

import tiling.Field;
import tiling.Tile;
import tiling.TileSet;
import tiling.TilingFrame;
import tiling.own.history.History;
import tiling.own.history.HistoryValue;

public class Algorithm
{
	public static final int DELAY = 100;
	public static final boolean DEBUG = false;
	private TilingFrame frame;
	private Field field;
	private TileList list;
	private History history;
	private History undoneHistory;

	public Algorithm(TilingFrame frame, Field field, TileSet tiles)
	{
		this.frame = frame;
		this.field = field;
		this.list = new TileList(tiles);
		this.history = new History();
		this.undoneHistory = new History();
	}

	public void runAlgorithm()
	{
		//System.out.println(list.toString());
		//Tile tile = list.getFirst();
		Tile tile = null;
		int whileCounter = 0;
		for (int i = 0; i < field.getHeight(); i++)
		{
			for (int j = 0; j < field.getWidth(); j++)
			{
				if (field.isOccupied(j, i))
					continue;
				int maxWidth = 0;
				if (j == 5 && i == 18)
				{
					list.printFree();
				}
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
					tile = null;
					break;
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
				if (tile == null)
				{
					maxHeight = 0;
					for (int k = i; k < field.getHeight(); k++)
					{
						if (field.isOccupied(j, k))
							break;
						maxHeight++;
					}
					items = null;
					while ((items = list.getListByHeight(maxHeight)) == null && maxHeight > 0)
					{
						maxHeight--;
					}
					if (items == null)
					{
						tile = null;
						break;
					}
					maxWidth = 0;
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
				}
				if (tile == null)
					break;
				if ((field.placeTileSecure(tile, j, i) || field.placeTileSecure(tile.rotate(), j, i)))
				{
					list.setUsed(tile);
					history.add(tile, j, i);
				}
				frame.redraw(DELAY);
				list.printFree();
			}
			if (tile == null)
				break;
		}
		System.out.println("Stopped");
		if (DEBUG)
			System.out.println(list.toString());

	}

	private void undoLastMove(HistoryValue hv)
	{
		undoneHistory.add(hv);
		field.undo(hv.getTile(), hv.getX(), hv.getY());
		list.setUnUsed(hv.getTile());
	}
}
