package generator;

import tiling.Field;
import tiling.Tile;

public class GrowingTile extends Tile
{
	private boolean grow[] = { true, true, true, true }; // growing up, right, down, left
	boolean forceGrow = false;
	private int x;
	private int y;

	public GrowingTile(int x, int y)
	{
		super(1);
		this.setX(x);
		this.setY(y);
	}

	public boolean grow(Field field, int maxSize)
	{
		if (grow[0])
		{
			if ((getHeight() < maxSize) || forceGrow)
			{
				// we'er growing upwards
				if (getY() == 0)
				{
					// we've hit the top, stop growing upwards
					grow[0] = false;
				}
				else
				{
					grow[0] = growVertical(field, -1);
				}
			}
			else
			{
				grow[0] = false;
			}
		}

		if (grow[2])
		{
			if ((getHeight() < maxSize) || forceGrow)
			{
				// we'er growing downwards
				if (getY() + getHeight() == field.getHeight())
				{
					// we've hit the bottom, stop growing downwards
					grow[2] = false;
				}
				else
				{
					grow[2] = growVertical(field, 1);
				}
			}
			else
			{
				grow[2] = false;
			}
		}

		if (grow[1])
		{
			if (getWidth() < maxSize || forceGrow)
			{
				// we'er growing right
				if (getX() + getWidth() == field.getWidth())
				{
					// we've hit the right side
					grow[1] = false;
				}
				else
				{
					grow[1] = growHorizontal(field, 1);
				}
			}
			else
			{
				grow[1] = false;
			}
		}

		if (grow[3])
		{
			if (getWidth() < maxSize || forceGrow)
			{
				// we'er growing left
				if (getX() == 0)
				{
					// we've hit the left side
					grow[3] = false;
				}
				else
				{
					grow[3] = growHorizontal(field, -1);
				}
			}
			else
			{
				grow[3] = false;
			}
		}

		return grow[0] || grow[1] || grow[2] || grow[3];
	}

	public boolean isGrowing()
	{
		boolean growing = false;
		for (int i = 0; i < grow.length; i++)
		{
			if (grow[i])
			{
				growing = true;
			}
		}
		return growing;
	}

	public int getX()
	{
		return x;
	}

	public void setX(int x)
	{
		this.x = x;
	}

	public int getY()
	{
		return y;
	}

	public void setY(int y)
	{
		this.y = y;
	}

	/**
	 * Grows the tile vertically
	 * 
	 * @param field
	 *            - the field to check whether we can grow
	 * @param direction
	 *            - -1 = grow upwards 1 = grow downwards
	 */
	private boolean growVertical(Field field, int direction)
	{
		// Check if the row to grow into is not occupied
		int j = getY();
		if (direction < 0)
		{
			// Check the row above
			j = j - 1;
		}
		else
		{
			// Check the row below
			j += getHeight();
		}
		boolean canPlace = checkRow(field, getX(), j, getWidth());
		// If not, grow in that direction
		if (canPlace)
		{
			if (direction < 0)
			{
				setY(getY() - 1);
				setHeight(getHeight() + 1);
			}
			else if (direction > 0)
			{
				setHeight(getHeight() + 1);
			}

		}
		return canPlace;
	}

	/**
	 * Grows the tile horizontally
	 * 
	 * @param field
	 *            - the field to check whether we can grow
	 * @param direction
	 *            - -1 = grow left 1 = grow right
	 */
	private boolean growHorizontal(Field field, int direction)
	{
		// Check if the column to grow into is not occupied
		int i = getX();
		if (direction < 0)
		{
			// Check the column to the left
			i = i - 1;
		}
		else
		{
			// Check the column to the right
			i += getWidth();
		}

		boolean canPlace = checkColumn(field, i, getY(), getHeight());
		// If not, grow in that direction
		if (canPlace)
		{
			if (direction < 0)
			{
				setX(getX() - 1);
				setWidth(getWidth() + 1);
			}
			else if (direction > 0)
			{
				setWidth(getWidth() + 1);
			}
		}
		return canPlace;
	}

	private boolean checkRow(Field field, int x, int y, int width)
	{
		int j = y;
		for (int i = x; i < x + width; i++)
		{
			if (field.isOccupied(i, j))
			{
				return false;
			}
		}
		return true;
	}

	private boolean checkColumn(Field field, int x, int y, int height)
	{
		int i = x;
		for (int j = y; j < y + height; j++)
		{
			if (field.isOccupied(i, j))
			{
				return false;
			}
		}
		return true;
	}

	public void startGrowing()
	{
		for (int i = 0; i < 4; i++)
		{
			grow[i] = true;
		}
		forceGrow = true;
	}
}
