package tiling;

import java.util.ArrayList;
import java.util.List;

public class Field
{
	private ArrayList<Tile>[][] field;
	private int width, height;

	@SuppressWarnings("unchecked")
	public Field(int width, int height)
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

	public Field(Field src)
	{
		this(src.width, src.height);
		for (int i = 0; i < src.width; i++)
		{
			for (int j = 0; j < src.height; j++)
			{
				field[i][j] = new ArrayList<Tile>();
				field[i][j].addAll(src.getTiles(i, j));
			}
		}
	}

	public List<Tile> getTiles(int x, int y)
	{
		return field[x][y];
	}

	public int getHeight()
	{
		return height;
	}

	public void setHeight(int height)
	{
		this.height = height;
	}

	public int getWidth()
	{
		return width;
	}

	public void setWidth(int width)
	{
		this.width = width;
	}

	/**
	 * Places tile at position x, y
	 * 
	 * @param tile
	 * @param x
	 * @param y
	 * @return true - if the tile is placed onto the field false - if the tile
	 *         is (partly) outside the field
	 */
	public boolean placeTile(Tile tile, int x, int y)
	{
		if ((width - x >= tile.getWidth()) && (height - y >= tile.getHeight()))
		{
			for (int i = x; i < (x + tile.getWidth()); i++)
			{
				for (int j = y; j < (y + tile.getHeight()); j++)
				{
					field[i][j].add(tile);
				}
			}

			return true;
		}
		return false;
	}

	/**
	 * Places tile at position x, y
	 * 
	 * @param tile
	 * @param x
	 * @param y
	 * @return true - if the tile is placed onto the field false - if the tile
	 *         is (partly) outside the field or overlapping with another tile
	 */
	public boolean placeTileSecure(Tile tile, int x, int y)
	{
		if ((width - x >= tile.getWidth()) && (height - y >= tile.getHeight()))
		{
			boolean placed = true;

			for (int i = x; i < (x + tile.getWidth()); i++)
			{
				for (int j = y; j < (y + tile.getHeight()); j++)
				{
					if (!field[i][j].isEmpty())
						placed = false;
				}
			}

			if (placed)
			{
				for (int i = x; i < (x + tile.getWidth()); i++)
				{
					for (int j = y; j < (y + tile.getHeight()); j++)
					{
						field[i][j].add(tile);
					}
				}
			}

			return placed;
		}
		return false;
	}

	/**
	 * This method removes a tile from the board.
	 * 
	 * @param Tile
	 *            tile - the tile to remove
	 * @param x
	 *            - the x position of the tile to remove
	 * @param y
	 *            - the y position of the tile to remove
	 */
	public void undo(Tile tile, int x, int y)
	{
		for (int i = x; i < (x + tile.getWidth()); i++)
		{
			for (int j = y; j < (y + tile.getHeight()); j++)
			{
				if (i < width && j < height)
					field[i][j].remove(tile);
			}
		}
	}

	/**
	 * Checks whether a square is already occupied.
	 * 
	 * @param x
	 * @param y
	 * @return true - if the square (x,y) is occupied false - otherwise
	 */
	public boolean isOccupied(int x, int y)
	{
		return !field[x][y].isEmpty();
	}

	public int freeSpace()
	{
		int free = 0;
		for (ArrayList<Tile>[] one : this.field)
		{
			for (ArrayList<Tile> two : one)
			{
				if (two.isEmpty())
					free++;
			}
		}
		return free;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < this.height; i++)
		{
			for (int j = 0; j < this.width; j++)
			{
				if (isOccupied(j, i))
				{
					sb.append('X');
				}
				else
				{
					sb.append('O');
				}
				if (j < this.height - 1)
					sb.append(',');
			}
			if (i < this.width - 1)
				sb.append('\n');
		}
		return sb.toString();
	}

	@Override
	public boolean equals(Object o)
	{
		if (o == null || !(o instanceof Field))
			return false;
		Field f = (Field)o;
		for (int i = 0; i < this.getWidth(); i++)
		{
			for (int j = 0; j < this.getHeight(); j++)
			{
				ArrayList<Tile> thisTiles = this.field[i][j];
				ArrayList<Tile> otherTiles = f.field[i][j];
				Tile t1 = thisTiles.isEmpty() ? null : thisTiles.get(0);
				Tile t2 = otherTiles.isEmpty() ? null : otherTiles.get(0);
				if (t1 == null && t2 == null)
					continue;
				else if (t1 == null || t2 == null)
					return false;
				else if (!thisTiles.contains(t2) || !otherTiles.contains(t1))
					return false;
			}
		}
		return true;
	}
}
