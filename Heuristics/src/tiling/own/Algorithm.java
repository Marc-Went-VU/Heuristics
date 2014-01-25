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
	public static final boolean DEBUG = false;
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
		for (TileValue ti : items)
		{
			firstField.placeTileSecure(ti.getTile(), ti.getX(), ti.getY());
		}
	}

	private ArrayList<TileValue> algo()
	{
		final FieldSet start = new FieldSet(firstField, null, 0);
		ArrayList<FieldSet> closedSet = new ArrayList<FieldSet>();
		PriorityQueue<FieldSet> openSet = new PriorityQueue<FieldSet>(1, new FieldSetComparator());

		openSet.add(start);
		ArrayList<FieldSet> parent = null;
		FieldSet goal = null;

		while (!openSet.isEmpty())
		{
			FieldSet fs = openSet.poll();
			if (fs.getHScore() == 0)
				return reconstructPath(parent, goal);
			closedSet.add(fs);
			for (FieldSet neighbor : fs.getNeighbours())
			{
				if (closedSet.contains(neighbor))
					continue;
				double tent_g_score = fs.getGScore() + start.getHScore();

				if (!openSet.contains(neighbor))
				{
					openSet.add(neighbor);
					neighbor.setFrom(fs);
					neighbor.setGScore(tent_g_score);
				}
				else if (tent_g_score < neighbor.getGScore())
				{
					neighbor.setFrom(fs);
					neighbor.setGScore(tent_g_score);
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
