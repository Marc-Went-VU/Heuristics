package tiling.own;

import java.util.ArrayList;
import tiling.Field;

public class FieldSet implements Comparable<FieldSet>
{
	private Field field;
	private int depth;
	private double score;
	private TileValue placedTile;

	private FieldSet from;
	private double G;

	public FieldSet(Field field, TileValue tileItem, int depth)
	{
		this.field = field;
		this.depth = depth;
		this.placedTile = tileItem;
		this.score = calculateScore();
	}

	public double getHScore()
	{
		return score;
	}

	public TileValue getPlacedTile()
	{
		return placedTile;
	}

	//TODO: Scoring heuristic
	private double calculateScore()
	{
		double score = field.freeSpace();
		score *= (1 / (depth + 1));
		return score;
	}

	@Override
	public int compareTo(FieldSet o)
	{
		if (this.score < o.score)
			return 1;
		else if (this.score > o.score)
			return -1;
		else
			return 0;
	}

	public ArrayList<FieldSet> getNeighbours()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public FieldSet getFrom()
	{
		return from;
	}

	public void setFrom(FieldSet from)
	{
		this.from = from;
	}

	public void setGScore(double gScore)
	{
		this.G = gScore;
	}

	public double getGScore()
	{
		return this.G;
	}

}
