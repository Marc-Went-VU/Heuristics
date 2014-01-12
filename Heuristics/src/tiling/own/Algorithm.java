package tiling.own;

import tiling.Field;
import tiling.Tile;
import tiling.TileSet;
import tiling.TilingAssignment;
import tiling.TilingFrame;

public class Algorithm
{
	public static final int DELAY = 50;
	private TilingFrame frame;
	private Field field;
	private TilingAssignment assignment;

	public Algorithm(TilingFrame frame, Field field, TilingAssignment assignment)
	{
		this.frame = frame;
		this.field = field;
		this.assignment = assignment;
	}

	public void runAlgorithm()
	{
		TileSet tiles = assignment.getTiles();
		Tile tile = tiles.get(0);
		int x = 0;
		int y = 0;
		while (tiles.size() >= 1)
		{
			System.out
				.printf("Placing tile of size:                     %d, %d at %d, %d", tile.getWidth(), tile.getHeight(), x, y);

			if (field.placeTile(tile, x, y))
				System.out.println("...succes!");
			else
				System.out.println("...failure! :(\n");

			frame.redraw(DELAY);

			System.out.printf("Undoing last placed tile!\n");
			field.undo(tile, x, y);
			frame.redraw(DELAY);

			tile = tiles.get(0);
		}//
	}
}
