package tiling;

import java.awt.Color;
import java.util.Random;

public class Tile implements Comparable<Tile>
{
	private int width;
	private int height;
	private Color color;

	public Tile(int width, int height)
	{
		this.width = width;
		this.height = height;
		this.color = new Color(new Random().nextInt(0x1000000));
	}

	public Tile(int size)
	{
		this(size, size);
	}

	public Tile(Tile t, boolean rotate)
	{
		this(t.width, t.height);
		if (rotate)
		{
			int tmpW = this.getWidth();
			int tmpH = this.getHeight();
			this.setHeight(tmpW);
			this.setWidth(tmpH);
		}

		this.color = t.color;
	}

	public int getHeight()
	{
		return this.height;
	}

	public int getWidth()
	{
		return this.width;
	}

	public void setWidth(int width)
	{
		this.width = width;
	}

	public void setHeight(int height)
	{
		this.height = height;
	}

	public int getArea()
	{
		return this.width * this.height;
	}

	public Color getColor()
	{
		return this.color;
	}

	//@Override
	// This equals is used in TileSet to count the number of
	// different sized tiles
	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof Tile)
		{
			Tile tile = (Tile)obj;
			return ((tile.width == this.width) && (tile.height == this.height))
				|| ((tile.width == this.height) && (tile.height == this.width));
		}
		return super.equals(obj);
	}

	public boolean equals(Tile t)
	{
		return this.getColor().equals(t.getColor())
			&& ((this.getWidth() == t.getWidth() && this.getHeight() == t.getHeight()) || (this.getWidth() == t.getHeight() && this
				.getHeight() == t.getWidth()));
	}

	/**
	 * Note: this class has a natural ordering that is inconsistent with equals
	 */
	@Override
	public int compareTo(Tile o)
	{
		if (this.getArea() < o.getArea())
			return -1;
		else if (this.getArea() > o.getArea())
			return 1;
		else
		{
			int pythThis = pyth(this.getWidth(), this.getHeight());
			int pythO = pyth(o.getWidth(), o.getHeight());
			if (pythThis > pythO)
				return -1;
			else if (pythThis < pythO)
				return 1;
			else
				return 0;
		}
	}

	private int pyth(int width, int height)
	{
		return (width * width) + (height * height);
	}

	@Override
	public String toString()
	{
		return this.getWidth() + "x" + this.getHeight() + "=" + (this.getWidth() * this.getHeight());
	}

	public String excessiveString()
	{
		return this.toString() + " - " + color.toString();
	}

	public Tile rotate()
	{
		int tmpW = this.getWidth();
		int tmpH = this.getHeight();
		this.setHeight(tmpW);
		this.setWidth(tmpH);
		return this;
	}

	public boolean isSquare()
	{
		return this.width == this.height;
	}
}
