package tiling.own;

import java.util.Collections;
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
	private TileMap map;
	private History history;

	public Algorithm(TilingFrame frame, Field field, TileSet tiles)
	{
		this.frame = frame;
		this.field = field;
		this.map = new TileMap(tiles, Collections.reverseOrder());
		this.history = new History();
	}

	public void runAlgorithm()
	{
		Tile tile = map.getFirst();
		for (int i = 0; i < field.getHeight(); i++)
		{
			for (int j = 0; j < field.getWidth(); j++)
			{
				if (field.isOccupied(j, i))
					continue;
				while (tile != null && !(field.placeTileSecure(tile, j, i) || field.placeTileSecure(tile.rotate(), j, i)))
				{
					tile = map.getBySize(field.getWidth() - j);
				}
				if (tile == null)
				{
					HistoryValue hv = history.undo();
					field.undo(hv.getTile(), hv.getX(), hv.getY());
					tile = map.getBiggest();
					map.setUnUsed(hv.getTile());
				}
				else
				{
					map.setUsed(tile);
					history.add(tile, j, i);
					tile = map.getBiggest();
				}
				frame.redraw(DELAY);

			}
			if (tile == null)
				break;
		}
	}
}
