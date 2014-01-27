package tiling.own;

public class Coordinate
{
	private int x, y;

	public Coordinate(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	public Coordinate(Coordinate c)
	{
		this.x = c.getX();
		this.y = c.getY();
	}

	public Coordinate(Coordinate c, int width, int height)
	{
		this.x = c.getX() + width;
		this.y = c.getY() + height;
	}

	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}

	@Override
	public boolean equals(Object o)
	{
		if (o instanceof Coordinate)
		{
			Coordinate c = (Coordinate)o;
			return this.x == c.getX() && this.y == c.getY();
		}
		return false;
	}

	@Override
	public String toString()
	{
		return "(" + x + "," + y + ")";
	}
}
