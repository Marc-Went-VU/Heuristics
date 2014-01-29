package tiling.own;

import java.util.ArrayList;
import java.util.PriorityQueue;
import tiling.Field;
import tiling.Tile;
import tiling.TileSet;
import tiling.TilingFrame;

public class Algorithm
{
	public static final int DELAY = 1;
	public static final boolean DEBUG = true;
	private TilingFrame frame;
	@SuppressWarnings("unused")
	private Field currentField;
	private final Field firstField;
	private TileList list;

	public Algorithm(TilingFrame frame, Field field, TileSet tiles)
	{
		this.frame = frame;
		this.firstField = this.currentField = field;
		this.list = new TileList(tiles);
	}

	public void runAlgorithm()
	{
		long startTime = System.nanoTime();

		ArrayList<TileValue> items = algo();
		if (items == null)
		{
			System.err.println("Algorithm didn't find solution :/");
			return;
		}
		long endTime = System.nanoTime();

		long duration = endTime - startTime;
		double seconds = (double)duration / 1000000000.0;
		System.out.println("It took " + seconds + " seconds");
		frame.setField(firstField);
		for (TileValue ti : items)
		{
			Tile t = ti.getTile();
			Coordinate c = ti.getCoordinate();
			if (!(firstField.placeTileSecure(t, c.getX(), c.getY())))
				if (!firstField.placeTileSecure(t.rotate(), c.getX(), c.getY()))
					System.err.println(ti);
			frame.redraw(100);
		}
	}

	private ArrayList<TileValue> algo()
	{
		final FieldSet start = new FieldSet(null, firstField, null, list.getTileArray(), 0);
		ArrayList<FieldSet> closedSet = new ArrayList<FieldSet>();
		PriorityQueue<FieldSet> openSet = new PriorityQueue<FieldSet>(1, new FieldSetComparator());

		openSet.add(start);

		while (!openSet.isEmpty())
		{
			FieldSet fs = openSet.poll();
			if (fs.getHScore() == 0)
			{
				//				if (DEBUG)
				//					printPath(fs);
				//				frame.setField(fs.getField());
				//				frame.redraw(0);
				return reconstructPath(fs);
			}
			closedSet.add(fs);
			Field f = fs.getField();
			if (DEBUG)
			{
				frame.setField(f);
				frame.redraw(DELAY);
			}
			ArrayList<FieldSet> neighbors = fs.getNeighbours();
			for (FieldSet neighbor : neighbors)
			{
				if (closedSet.contains(neighbor))
					continue;
				double tent_g_score = fs.getGScore() + (fs.getHScore() - neighbor.getHScore());

				if (!openSet.contains(neighbor) || tent_g_score < neighbor.getGScore())
				{
					neighbor.setFrom(fs);
					neighbor.setGScore(tent_g_score);
					if (!openSet.contains(neighbor))

						openSet.add(neighbor);
				}
			}
		}
		return null;
	}

	private void printPath(FieldSet fs)
	{
		if (fs == null)
			return;
		//System.out.println(fs.getField());
		System.out.println(fs.getPlacedTile());
		printPath(fs.getFrom());

	}

	private ArrayList<TileValue> reconstructPath(FieldSet current)
	{
		if (current == null || current.getPlacedTile() == null)
			return new ArrayList<TileValue>();
		ArrayList<TileValue> path = new ArrayList<TileValue>();
		path.add(current.getPlacedTile());
		path.addAll(reconstructPath(current.getFrom()));
		return path;
	}

}
