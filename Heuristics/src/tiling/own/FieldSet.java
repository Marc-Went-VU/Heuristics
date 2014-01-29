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

	public FieldSet(FieldSet from, Field field, TileValue tileItem, ArrayList<Tile> tiles, int depth)
	{
		this.field = field;
		this.depth = depth;
		this.placedTile = tileItem;
		this.score = calculateScore();
		this.G = 0;
		this.from = from;
		usableTiles = new ArrayList<Tile>();
		if (tiles != null)
			usableTiles.addAll(tiles);
		if (!usableTiles.isEmpty() && tileItem != null)
			usableTiles.remove(tileItem.getTile());
	}

	public Field getField()
	{
		return field;
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
		score *= ((depth + 1.0));
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
			return -1;
		else if (this.score > o.score)
			return 1;
		else
			return 0;
	}

	public ArrayList<FieldSet> getNeighbours()
	{
		ArrayList<FieldSet> neighbors = new ArrayList<FieldSet>();
		ArrayList<Coordinate> placableCoordinates = getFreeNeighborCoordinates();
		if (placableCoordinates.isEmpty())
			placableCoordinates.add(new Coordinate(0, 0));
		for (Coordinate pC : placableCoordinates)
		{
			int maxWidth = 0;
			for (int i = pC.getX(); i < field.getWidth(); i++)
			{
				maxWidth++;
				if (field.isOccupied(i, pC.getY()))
					break;
			}
			ArrayList<Tile> usage = null;
			int tmpMaxWidth = maxWidth;
			while (usage == null && tmpMaxWidth > 0)
			{
				usage = findUsable(usableTiles, tmpMaxWidth--);
			}
			ArrayList<Tile> nonUsable = new ArrayList<Tile>();
			nonUsable.addAll(usableTiles);
			nonUsable.removeAll(usage);
			for (Tile u : usage)
			{
				Field f = new Field(this.field);
				TileValue tileItem = new TileValue(u, pC);
				if (f.placeTileSecure(tileItem.getTile(), tileItem.getCoordinate().getX(), tileItem.getCoordinate().getY()))
				{
					FieldSet fs = new FieldSet(this, f, tileItem, usableTiles, this.depth + 1);
					neighbors.add(fs);
				}
				else
					nonUsable.add(u.rotate());
			}
			usage = null;
			tmpMaxWidth = maxWidth;
			while (usage == null && tmpMaxWidth > 0)
			{
				usage = findUsable(nonUsable, tmpMaxWidth--);
			}

			for (Tile u : usage)
			{
				Field f = new Field(this.field);
				TileValue tileItem = new TileValue(u, pC);
				if (f.placeTileSecure(tileItem.getTile(), tileItem.getCoordinate().getX(), tileItem.getCoordinate().getY()))
				{
					FieldSet fs = new FieldSet(this, f, tileItem, usableTiles, this.depth + 1);
					neighbors.add(fs);
				}
				else
					u.rotate();
			}
		}

		return neighbors;
	}

	private ArrayList<Tile> findUsable(ArrayList<Tile> usableTiles, int maxWidth)
	{
		ArrayList<Tile> tiles = new ArrayList<Tile>();
		for (Tile t : usableTiles)
		{
			if (t.getWidth() <= maxWidth)
				tiles.add(t);
			if (t.getHeight() == maxWidth)
			{
				tiles.add(t.rotate());
			}
		}
		return tiles;
	}

	private ArrayList<Coordinate> getFreeNeighborCoordinates()
	{
		ArrayList<Coordinate> ac = new ArrayList<Coordinate>();
		for (int i = 0; i < this.field.getHeight(); i++)
		{
			for (int j = 0; j < this.field.getWidth(); j++)
			{
				Coordinate c = new Coordinate(j, i);
				if (!this.field.isOccupied(c.getX(), c.getY()) && hasNeighbors(this.field, c))
					ac.add(c);

			}
		}
		return ac;
	}

	private boolean hasNeighbors(Field field2, Coordinate c)
	{
		Coordinate cN = new Coordinate(c, 0, -1);
		Coordinate cE = new Coordinate(c, 1, 0);
		Coordinate cS = new Coordinate(c, 0, 1);
		Coordinate cW = new Coordinate(c, -1, 0);

		if (this.inField(cN) && field2.isOccupied(cN.getX(), cN.getY()))
			return true;
		else if (this.inField(cE) && field2.isOccupied(cE.getX(), cE.getY()))
			return true;
		else if (this.inField(cS) && field2.isOccupied(cS.getX(), cS.getY()))
			return true;
		else if (this.inField(cW) && field2.isOccupied(cW.getX(), cW.getY()))
			return true;
		else
			return false;
	}

	private TileValue findTileValue(Tile usable)
	{
		Coordinate tmp = null;
		Tile tmpTile = null;
		Coordinate topLeft = null;
		Coordinate bottomRight = null;
		if (placedTile != null)
		{
			tmp = placedTile.getCoordinate();
			tmpTile = placedTile.getTile();
			topLeft = new Coordinate(tmp.getX() - 1, tmp.getY() - 1);
			bottomRight = new Coordinate(tmp, tmpTile.getWidth(), tmpTile.getHeight());
		}
		else
		{
			tmp = new Coordinate(0, 0);
			topLeft = new Coordinate(tmp);
			bottomRight = new Coordinate(tmp);
		}
		//Left && right placedTile
		if (field.getHeight() > bottomRight.getY())
		{
			int xLeft = topLeft.getX();
			int xRight = bottomRight.getX();
			for (int y = topLeft.getY() > 0 ? topLeft.getY() : 0; y + usable.getHeight() < field.getHeight()
				&& y <= bottomRight.getY(); y++)
			{
				TileValue tv1 = new TileValue(usable, new Coordinate(xLeft - usable.getWidth() + 1, y));
				TileValue tv2 = new TileValue(usable, new Coordinate(xRight, y));
				if (tileFits(tv1))
					return tv1;
				else if (tileFits(tv2))
					return tv2;
			}
		}
		//Above && beneath placedTile
		if (field.getWidth() > bottomRight.getX())
		{
			int yAbove = topLeft.getY();
			int yBeneath = bottomRight.getY();
			for (int x = topLeft.getX() > 0 ? topLeft.getX() : 0; x + usable.getWidth() < field.getWidth()
				&& x <= bottomRight.getX(); x++)
			{
				Coordinate c1 = new Coordinate(x, yAbove - usable.getHeight() - 1);
				Coordinate c2 = new Coordinate(x, yBeneath);
				TileValue tv1 = new TileValue(usable, c1);
				TileValue tv2 = new TileValue(usable, c2);
				if (tileFits(tv1))
					return tv1;
				else if (tileFits(tv2))
					return tv2;
			}
		}

		return null;
	}

	private boolean tileFits(TileValue tv)
	{
		Tile t = tv.getTile();
		Coordinate c = tv.getCoordinate();
		Coordinate bottomRight = new Coordinate(c, tv.getTile().getWidth() - 1, tv.getTile().getHeight() - 1);
		if (!(inField(c) && inField(bottomRight)))
			return false;
		for (int x = 0; x < t.getWidth(); x++)
		{
			for (int y = 0; y < t.getHeight(); y++)
			{
				if (field.isOccupied(x + tv.getCoordinate().getX(), y + tv.getCoordinate().getY()))
					return false;
			}
		}

		return true;
	}

	private boolean inField(Coordinate c)
	{
		int x = c.getX();
		int y = c.getY();
		return x >= 0 && x < field.getWidth() && y >= 0 && y < field.getHeight();
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

	@Override
	public boolean equals(Object o)
	{
		if (o == null)
			return false;
		if (o instanceof FieldSet)
		{
			FieldSet fs = (FieldSet)o;
			if (fs.getPlacedTile() == null || this.getPlacedTile() == null)
				return false;
			this.getPlacedTile().equals(fs.getPlacedTile());
		}
		return super.equals(o);
	}
}
