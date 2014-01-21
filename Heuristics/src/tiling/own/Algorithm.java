package tiling.own;

import java.util.ArrayList;
import java.util.PriorityQueue;
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
		PriorityQueue<Tile> openSet = new PriorityQueue<Tile>();
		openSet.add(list.getFirstAvailable());
		ArrayList<Tile> closedSet = new ArrayList<Tile>();
		this.numSearchSteps = 0;
		while (openSet.size() > 0 && (maxSteps < 0 || this.numSearchSteps < maxSteps))
		{
			Tile t = openSet.poll();
			/*
			 * if(goalNode.inGoal(t)
			 * return t;
			 */
			ArrayList<Tile> successors = list.getAvailableTiles();
			for (Tile successor : successors)
			{
				boolean inOpenSet;
				if (closedSet.contains(successor))
					continue;
				Tile discSuccessorNode = getNode(openSet, successor);
				if (discSuccessorNode != null)
					inOpenSet = true;
				else
					inOpenSet = false;

				int tentativeG = field.getFreeArea() + successor.getArea();

				if (inOpenSet && tentativeG >= field.getFreeArea())
					continue;
			}
		}
	}

	private Tile getNode(PriorityQueue<Tile> queue, Tile searchedNode)
	{
		for (Tile openSearchNode : queue)
		{
			if (openSearchNode.equals(searchedNode))
			{
				return openSearchNode;
			}
		}
		return null;
	}
}
