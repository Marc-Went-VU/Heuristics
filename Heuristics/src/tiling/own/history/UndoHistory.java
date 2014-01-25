package tiling.own.history;

import java.util.ArrayList;
import tiling.Tile;

public class UndoHistory
{

	private ArrayList<Tile>[][] field;
	private int width, height;

	@SuppressWarnings("unchecked")
	public UndoHistory(int width, int height)
	{
		this.width = width;
		this.height = height;
		field = (ArrayList<Tile>[][])new ArrayList<?>[width][height];

		for (int i = 0; i < width; i++)
		{
			for (int j = 0; j < height; j++)
			{
				field[i][j] = new ArrayList<Tile>();
			}
		}
	}

	public void placeTile(Tile t, int x, int y)
	{
		field[x][y].add(t);
	}
}
