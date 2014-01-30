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
		usableTiles = new ArrayList<Tile>();
		if (tiles != null)
			usableTiles.addAll(tiles);
		if (!usableTiles.isEmpty() && tileItem != null)
		{
			for (int i = 0; i < usableTiles.size(); i++)
			{
				Tile t = usableTiles.get(i);
				if (t.equals(tileItem.getTile()))
				{
					usableTiles.remove(i);
					break;
				}

			}
		}
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
		if (freeSpace == 0)
			return score;
		//score += (score / ((depth + 1.0)));

		//		score += placedTile == null ? 0 : (placedTile.getTile().getArea()) / depth;
		score += usableTiles == null ? 0 : (usableTiles.size() * (depth + 1.0));
		score += placedTile == null ? 0 : placedTile.getCoordinate().getY();
		if (from != null)
		{
			TileValue curr = getPlacedTile();
			Coordinate currC = curr.getCoordinate();
			Coordinate currCBott = new Coordinate(currC, 0, 1);

			int maxWidth = getMaxWidth(currC);
			int maxHeight = getMaxHeight(currC);
			int preferredWidth = getPreferredWidth(currC, maxWidth);
			int preferredHeight = getPreferredHeight(currC, maxHeight);

			Tile t = curr.getTile();
			if (t.getWidth() == preferredWidth && t.getHeight() == preferredHeight)
				score = 0;
			else
			{
				int widthPenalty = Math.abs(preferredWidth - curr.getTile().getWidth());
				int heightPenalty = Math.abs(preferredHeight - curr.getTile().getHeight());
				if (t.getWidth() == preferredWidth)
				{
					if (maxWidth != 0 || !inField(new Coordinate(currC, curr.getTile().getWidth(), 0)))
						score -= heightPenalty;
					score /= 2;
				}
				else if (preferredWidth != Integer.MIN_VALUE)
					score += widthPenalty;

				if (t.getHeight() == preferredHeight)
				{
					if (maxHeight != 0 || !inField(currCBott))
						score -= widthPenalty;
					score /= 2;
				}
				else if (preferredHeight != Integer.MIN_VALUE)
					score += heightPenalty;
			}
			if (from.score < 10)
				score /= 10;
			if (score <= 0)
				score = 1;
		}
		return score;
	}

	private int getPreferredHeight(Coordinate currC, int maxHeight)
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
		int preferredHeight = left.getHeight();

		return preferredHeight;
	}

	private int getPreferredWidth(Coordinate currC, int maxWidth)
	{
		Coordinate currCAbove = new Coordinate(currC, 0, -1);

		Tile above = null;
		if (inField(currCAbove))
		{
			List<Tile> tiles = this.field.getTiles(currCAbove.getX(), currCAbove.getY());
			if (!tiles.isEmpty())
				above = tiles.get(0);
		}
		if (above == null)
			return maxWidth;
		int preferredWidth = above.getWidth();

		return preferredWidth;

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
		// TODO Auto-generated method stub
		return max;
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
			if (pC.equals(new Coordinate(6, 21)))
				System.out.println("blah");
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
			{
				usage = findUsable(usableTiles, tmpMaxWidth--);
			}
			//			ArrayList<Tile> nonUsable = new ArrayList<Tile>();
			//			nonUsable.addAll(usableTiles);
			//			nonUsable.removeAll(usage);
			//			for (Tile u : usage)
			//			{
			//				Field f = new Field(this.field);
			//				TileValue tileItem = new TileValue(u, pC);
			//				if (f.placeTileSecure(tileItem.getTile(), tileItem.getCoordinate().getX(), tileItem.getCoordinate().getY()))
			//				{
			//					FieldSet fs = new FieldSet(this, f, tileItem, usableTiles, this.depth + 1);
			//					neighbors.add(fs);
			//				}
			//				//				else
			//				//					nonUsable.add(new Tile(u, true));
			//			}

			//			usage = null;
			//			tmpMaxWidth = maxWidth;
			//			while (usage == null && tmpMaxWidth > 0)
			//			{
			//				usage = findUsable(nonUsable, tmpMaxWidth--);
			//			}

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
			return fs.getField().equals(this.getField());
			//			return this.getPlacedTile().equals(fs.getPlacedTile());
		}
		return super.equals(o);
	}
}
