package tiling.own;

import java.util.ArrayList;
import java.util.PriorityQueue;
import tiling.Field;
import tiling.TileSet;
import tiling.TilingFrame;
import tiling.own.history.History;

public class Algorithm
{
	public static final int DELAY = 10;
	public static final boolean DEBUG = true;
	private TilingFrame frame;
	private Field currentField;
	private final Field firstField;
	private TileList list;
	private History history;

	private Field undo;

	public Algorithm(TilingFrame frame, Field field, TileSet tiles)
	{
		this.frame = frame;
		this.firstField = this.currentField = field;
		this.list = new TileList(tiles);
		this.history = new History();
		this.undo = new Field(this.currentField.getWidth(), this.currentField.getHeight());
	}

	public void runAlgorithm()
	{
		ArrayList<TileValue> items = algo();
		if (items == null)
		{
			System.err.println("Algorithm didn't find solution :/");
			return;
		}
		for (TileValue ti : items)
		{
			Coordinate c = ti.getCoordinate();
			firstField.placeTileSecure(ti.getTile(), c.getX(), c.getY());
		}
		frame.setField(firstField);
		frame.redraw(0);
	}

	private ArrayList<TileValue> algo()
	{
		final FieldSet start = new FieldSet(null, firstField, null, list.getTileArray(), 0);
		ArrayList<FieldSet> closedSet = new ArrayList<FieldSet>();
		PriorityQueue<FieldSet> openSet = new PriorityQueue<FieldSet>(1, new FieldSetComparator());

		openSet.add(start);
		ArrayList<FieldSet> parent = new ArrayList<FieldSet>();
		FieldSet goal = null;

		while (!openSet.isEmpty())
		{
			FieldSet fs = openSet.poll();
			if (fs.getHScore() == 0)
				return reconstructPath(parent, goal);
			closedSet.add(fs);
			Field f = fs.getField();
			if (DEBUG)
			{
				frame.setField(f);
				frame.redraw(1);
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

	private ArrayList<TileValue> reconstructPath(ArrayList<FieldSet> from, FieldSet current)
	{
		ArrayList<TileValue> path = new ArrayList<TileValue>();
		if (from.contains(current))
		{
			FieldSet fs = from.get(from.indexOf(current) - 1);
			path.addAll(reconstructPath(from, fs));
			path.add(current.getPlacedTile());
			return path;
		}
		else
		{
			ArrayList<TileValue> item = new ArrayList<TileValue>();
			item.add(current.getPlacedTile());
			return item;
		}
	}

}
