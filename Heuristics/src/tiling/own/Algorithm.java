package tiling.own;

import tiling.Field;
import tiling.Tile;
import tiling.TileSet;
import tiling.TilingFrame;
import tiling.own.History.HistoryValue;

public class Algorithm
{
	public static final int DELAY = 50;
	private TilingFrame frame;
	private Field field;
	private TileList list;
	private History history;

	public Algorithm(TilingFrame frame, Field field, TileSet tiles)
	{
		this.frame = frame;
		this.field = field;
		this.list = new TileList(tiles);
		this.history = new History();
	}

	public void runAlgorithm()
	{
		Tile tile = list.getFirst();
		int whileCounter = 0;
		for (int i = 0; i < field.getHeight(); i++)
		{
			for (int j = 0; j < field.getWidth(); j++)
			{
				if (field.isOccupied(j, i))
					continue;
				while (tile != null && !(field.placeTileSecure(tile, j, i) || field.placeTileSecure(tile.rotate(), j, i)))
				{
					tile = list.getByWidth(field.getWidth() - j);
					if (tile == null || whileCounter++ >= 5)
						tile = list.getByHeight(field.getHeight() - i);
				}
				whileCounter = 0;
				if (tile == null)
				{
					HistoryValue hv = history.undo();
					field.undo(hv.getTile(), hv.getX(), hv.getY());
					tile = list.getBiggest();
					list.setUnUsed(hv.getTile());
				}
				else
				{
					list.setUsed(tile);
					history.add(tile, j, i);
					tile = list.getBiggest();
				}
				list.printFree();
				frame.redraw(DELAY);

			}
			if (tile == null)
				break;
		}
	}
}
