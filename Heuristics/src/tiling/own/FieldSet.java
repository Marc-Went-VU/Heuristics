package tiling.own;

import java.util.ArrayList;
import java.util.List;
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
		this.from = from;
		this.score = calculateScore();
		this.G = 0;
		usableTiles = getUsableTiles(tiles, tileItem);
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
				if (field.isOccupied(i, pC.getY()))
					break;
				maxWidth++;
			}
			ArrayList<Tile> usage = null;
			int tmpMaxWidth = maxWidth;
			while (usage == null && tmpMaxWidth > 0)
				usage = findUsable(usableTiles, tmpMaxWidth--);

			if (usage.size() > 10)
			{
				List<Tile> tmp = new ArrayList<Tile>();
				tmp = usage.subList(0, 10);
				if (!tmp.isEmpty())
				{
					usage = new ArrayList<Tile>();
					usage.addAll(tmp);
				}
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
				{
					int index = 0;
					for (int i = 0; i < usableTiles.size(); i++)
					{
						if (usableTiles.get(i).equals(u))
						{
							index = i;
							break;
						}

					}
					if (!u.isSquare())
						usableTiles.set(index, new Tile(u, true));
				}
			}
		}

		return neighbors;
	}

	private double calculateScore()
	{
		double score = 0;
		score = freeSpace = (from == null ? field.freeSpace() : from.getFreeSpace() - placedTile.getTile().getArea());
		if (freeSpace == 0)
			return score;

		score -= placedTile == null ? 0 : (placedTile.getTile().getArea() * 2) / depth;
		if (from != null)
		{
			TileValue curr = getPlacedTile();
			Coordinate currC = curr.getCoordinate();

			int maxWidth = getMaxWidth(currC);
			int maxHeight = getMaxHeight(currC);
			int preferredWidth = getPreferredWidth(currC, maxWidth, curr.getTile().getWidth());
			int preferredHeight = getPreferredHeight(currC, maxHeight, curr.getTile().getHeight());

			if (maxWidth - preferredWidth == 1 && !inField(new Coordinate(currC, maxWidth + 1, 0)))
				preferredWidth = maxWidth;
			if (maxHeight - preferredHeight == 1 && !inField(new Coordinate(currC, 0, maxHeight + 1)))
				preferredHeight = maxHeight;

			Tile t = curr.getTile();
			if (t.getWidth() == preferredWidth && t.getHeight() == preferredHeight)
				score = 0;
			else
			{
				int widthPenalty = Math.abs(preferredWidth - curr.getTile().getWidth());
				int heightPenalty = Math.abs(preferredHeight - curr.getTile().getHeight());

				boolean substractWidthPenalty = maxWidth == preferredWidth;
				boolean substractHeightPenalty = maxHeight == preferredHeight;
				if (t.getWidth() == preferredWidth)
					score /= 2;
				else if (preferredWidth != Integer.MIN_VALUE)
					score += widthPenalty;

				if (t.getHeight() == preferredHeight)
					score /= 2;
				else if (preferredHeight != Integer.MIN_VALUE)
					score += heightPenalty;

				if (substractWidthPenalty)
					score -= widthPenalty;
				if (substractHeightPenalty)
					score -= heightPenalty;
			}
			if (from.score < 10)
				score /= 10;
			if (score <= 0)
				score = 1;
		}
		return score;
	}

	private ArrayList<Tile> getUsableTiles(ArrayList<Tile> tiles, TileValue tileItem)
	{
		ArrayList<Tile> usable = new ArrayList<Tile>();
		if (tiles != null)
			usable.addAll(tiles);
		if (!usable.isEmpty() && tileItem != null)
		{
			for (int i = 0; i < usable.size(); i++)
			{
				Tile t = usable.get(i);
				if (t.equals(tileItem.getTile()))
				{
					usable.remove(i);
					break;
				}

			}
		}
		return usable;
	}

	private int getPreferredWidth(Coordinate currC, int maxWidth, int tileWidth)
	{
		Coordinate currCAbove = new Coordinate(currC, 0, -1);

		Tile above = getTileAt(currCAbove);

		if (above == null)
			return maxWidth;

		Coordinate topLeft = findTopCoordinate(above, currCAbove);

		int preferredWidth = above.getWidth() - (currCAbove.getX() - topLeft.getX());

		if (preferredWidth < tileWidth)
		{
			Coordinate currCAboveAdjecent = new Coordinate(currCAbove);
			while (getTileAt(currCAboveAdjecent) == above)
				currCAboveAdjecent = new Coordinate(currCAboveAdjecent, 1, 0);

			preferredWidth += getPreferredWidth(currCAboveAdjecent, maxWidth, tileWidth);
		}

		return preferredWidth;

	}

	private int getPreferredHeight(Coordinate currC, int maxHeight, int tileHeight)
	{
		Coordinate currCLeft = new Coordinate(currC, -1, 0);
		Tile left = null;
		if (inField(currCLeft))
		{
			List<Tile> tiles = this.field.getTiles(currCLeft.getX(), currCLeft.getY());
			if (!tiles.isEmpty())
				left = tiles.get(0);
		}
		if (left == null)
			return maxHeight;

		Coordinate topLeft = findTopCoordinate(left, currCLeft);

		int preferredHeight = left.getHeight() - (currCLeft.getY() - topLeft.getY());
		if (preferredHeight < tileHeight)
		{
			Coordinate currCLeftAdjecent = new Coordinate(currCLeft);
			while (getTileAt(currCLeftAdjecent) == left)
				currCLeftAdjecent = new Coordinate(currCLeftAdjecent, 0, 1);

			preferredHeight += getPreferredHeight(currCLeftAdjecent, maxHeight, tileHeight);
		}
		return preferredHeight;
	}

	private int getMaxWidth(Coordinate c)
	{
		int max = 0;
		for (int i = c.getX(); i < this.field.getWidth(); i++)
		{
			max++;
			if (this.from.getField().isOccupied(i, c.getY()))
				break;
		}
		return max;
	}

	private int getMaxHeight(Coordinate c)
	{
		int max = 0;
		for (int i = c.getY(); i < this.field.getHeight(); i++)
		{
			max++;
			if (this.from.getField().isOccupied(c.getX(), i))
				break;
		}
		return max;
	}

	private ArrayList<Tile> findUsable(ArrayList<Tile> usableTiles, int maxWidth)
	{
		ArrayList<Tile> tiles = new ArrayList<Tile>();
		for (Tile t : usableTiles)
		{
			if (t.getWidth() <= maxWidth)
				tiles.add(t);
			else if (t.getHeight() <= maxWidth)
			{
				if (t.isSquare())
					tiles.add(t);
				else
					tiles.add(new Tile(t, true));
			}
		}
		return tiles;
	}

	private Tile getTileAt(Coordinate c)
	{
		Tile t = null;
		if (inField(c))
		{
			List<Tile> tiles = this.field.getTiles(c.getX(), c.getY());
			if (!tiles.isEmpty())
				t = tiles.get(0);
		}
		return t;
	}

	private Coordinate findTopCoordinate(Tile left, Coordinate c)
	{
		int k = 0;
		int l = 0;
		for (int i = c.getX(); i >= 0; i--)
		{
			List<Tile> tiles = this.field.getTiles(i, c.getY());
			if (!tiles.isEmpty())
			{
				Tile t = tiles.get(0);
				if (t != left)
				{
					k = i + 1;
					break;
				}
			}
		}
		for (int j = c.getY(); j >= 0; j--)
		{
			List<Tile> tiles = this.field.getTiles(k, j);
			if (!tiles.isEmpty())
			{
				Tile t = tiles.get(0);
				if (t != left)
				{
					l = j + 1;
					break;
				}
			}
		}
		return new Coordinate(k, l);
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
				else if (this.field.isOccupied(c.getX(), c.getY()))
				{
					Tile t = this.field.getTiles(j, i).get(0);
					j += t.getWidth() - 1;
				}
			}
		}
		return ac;
	}

	private boolean hasNeighbors(Field field2, Coordinate c)
	{
		Coordinate cN = new Coordinate(c, 0, -1);
		Coordinate cW = new Coordinate(c, -1, 0);

		if (occupied(field2, cN) && occupied(field2, cW))
			return true;
		return false;
	}

	private boolean occupied(Field f, Coordinate c)
	{
		if (!this.inField(c))
			return true;
		else if (f.isOccupied(c.getX(), c.getY()))
			return true;
		return false;
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

	public Field getField()
	{
		return field;
	}

	public TileValue getPlacedTile()
	{
		return placedTile;
	}

	public double getGScore()
	{
		return this.G;
	}

	public double getHScore()
	{
		return score;
	}

	public int getFreeSpace()
	{
		return freeSpace;
	}

	public void setFrom(FieldSet from)
	{
		this.from = from;
	}

	public void setGScore(double gScore)
	{
		this.G = gScore;
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
			return fs.getField().equals(this.getField());
			//			return this.getPlacedTile().equals(fs.getPlacedTile());
		}
		return super.equals(o);
	}

	@Override
	public String toString()
	{
		return "(" + placedTile.toString() + " - " + this.getHScore() + ")";
	}
}
