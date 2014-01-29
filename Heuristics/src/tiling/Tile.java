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

	/**
	 * Note: this class has a natural ordering that is inconsistent with equals
	 */
	@Override
	public int compareTo(Tile o)
	{
		return this.getArea() - o.getArea();
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
		int tmpX = this.getWidth();
		int tmpY = this.getHeight();
		this.setWidth(tmpY);
		this.setHeight(tmpX);
		return this;
	}
}
