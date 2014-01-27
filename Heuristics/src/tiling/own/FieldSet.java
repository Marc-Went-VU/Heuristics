package tiling.own;

import java.util.ArrayList;
import tiling.Field;
import tiling.Tile;

public class FieldSet implements Comparable<FieldSet>
{
	private Field field;
	private int depth;
	private double score;
	private TileValue placedTile;
	private ArrayList<Tile> usableTiles;

	private FieldSet from;
	private double G;
	private int freeSpace;

	public FieldSet(Field field, TileValue tileItem, ArrayList<Tile> tiles, int depth)
	{
		this.field = field;
		this.depth = depth;
		this.placedTile = tileItem;
		this.score = calculateScore();
		usableTiles = new ArrayList<Tile>();
		usableTiles.addAll(tiles);
		usableTiles.remove(tileItem.getTile());
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
		double score = 0;
		score = freeSpace = (from == null ? field.freeSpace() : from.getFreeSpace() - placedTile.getTile().getArea());
		score *= (1 / (depth + 1));
		return score;
	}

	public int getFreeSpace()
	{
		return freeSpace;
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
		ArrayList<FieldSet> neighbors = new ArrayList<FieldSet>();
		for (Tile usable : usableTiles)
		{
			Field f = new Field(this.field);
			TileValue tileItem = findTileValue(usable);
			FieldSet fs = new FieldSet(f, tileItem, usableTiles, this.depth + 1);
			neighbors.add(fs);
		}
		return neighbors;
	}

	private TileValue findTileValue(Tile usable)
	{
		Coordinate topLeft = new Coordinate(placedTile.getCoordinate());

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
