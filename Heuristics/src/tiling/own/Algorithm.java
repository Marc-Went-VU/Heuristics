package tiling.own;

import tiling.Field;
import tiling.Tile;
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
		TileList tiles = new TileList(assignment.getTiles());
		Tile tile = tiles.current();
		int x = 0;
		int y = 0;
		while (tiles.hasNext())
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

			tile = tiles.next();
		}//
	}
}
