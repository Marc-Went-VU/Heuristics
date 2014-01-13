package tiling.own;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import tiling.Tile;
import tiling.TileSet;

public class TileList
{

	private ArrayList<TileItem> list;

	private int pos;

	public TileList(TileSet ts)
	{
		list = new ArrayList<TileItem>();
		for (int i = 0; i < ts.size(); i++)
		{
			list.add(new TileItem(ts.peek(i), true));
		}
		Collections.sort(list, Collections.reverseOrder());
		pos = 0;
	}

	public boolean hasNext()
	{
		return pos < list.size();
	}

	public Tile current()
	{
		if (pos < 0 || pos >= list.size())
			return null;
		return list.get(pos).getTile();
	}

	public Tile next()
	{
		pos++;
		if (pos >= list.size())
			return null;
		return list.get(pos).getTile();
	}

	public Tile prev()
	{
		pos--;
		if (pos < 0)
			return null;
		return list.get(pos).getTile();
	}

	public Tile getFirst()
	{
		pos = 0;
		return list.get(pos).getTile();
	}

	public boolean remove(Tile t)
	{
		TileItem ti = findTileItem(t);
		if (ti != null)
		{
			return list.remove(ti);
		}
		return false;
	}

	public Tile getByWidth(int w)
	{
		ArrayList<TileItem> items = new ArrayList<TileItem>();
		TileItem item = null;
		Tile t;
		for (int i = 0; i < list.size(); i++)
		{
			item = list.get(i);
			if (!item.freeToUse())
				continue;
			t = item.getTile();
			if (t.getWidth() == w)
				items.add(item);
			else if (t.getHeight() == w)
			{
				t.rotate();
				items.add(item);
			}

		}
		if (items.size() == 0)
			return null;
		item = items.get(new Random().nextInt(items.size()));
		return item.getTile();
	}

	public Tile getByHeight(int h)
	{
		TileItem item = null;
		Tile t;
		for (int i = 0; i < list.size(); i++)
		{
			item = list.get(i);
			if (!item.freeToUse())
				continue;
			t = item.getTile();
			if (t.getWidth() == h)
				return t.rotate();
			else if (t.getHeight() == h)
				return t;
		}
		return null;
	}

	public Tile getBiggest()
	{
		TileItem item = null;
		for (int i = 0; i < list.size(); i++)
		{
			item = list.get(i);
			if (item.freeToUse())
				return item.getTile();
		}
		return null;
	}

	public void setUsed(Tile t)
	{
		TileItem ti = findTileItem(t);
		ti.setUsed();
	}

	public void setUnUsed(Tile t)
	{
		TileItem ti = findTileItem(t);
		ti.setUnUsed();
	}

	private TileItem findTileItem(Tile t)
	{
		TileItem item = null;
		for (int i = 0; i < list.size(); i++)
		{
			item = list.get(i);
			if (item.getTile().equals((Object)t))
				return item;
		}
		return null;
	}

	public void printFree()
	{
		ArrayList<TileItem> items = new ArrayList<TileItem>();
		TileItem item;
		for (int i = 0; i < list.size(); i++)
		{
			item = list.get(i);
			if (item.freeToUse())
				items.add(item);
		}
		System.out.println(items.toString());
	}

	public void printInUse()
	{
		ArrayList<TileItem> items = new ArrayList<TileItem>();
		TileItem item;
		for (int i = 0; i < list.size(); i++)
		{
			item = list.get(i);
			if (!item.freeToUse())
				items.add(item);
		}
		System.out.println(items.toString());
	}

	@Override
	public String toString()
	{
		return list.toString();
	}

	public class TileItem implements Comparable<TileItem>
	{
		private Tile t;
		private boolean b;

		public TileItem(Tile t, boolean b)
		{
			this.t = t;
			this.b = b;
		}

		public void setUnUsed()
		{
			this.b = true;
		}

		public void setUsed()
		{
			this.b = false;
		}

		public Tile getTile()
		{
			return this.t;
		}

		public boolean freeToUse()
		{
			return this.b;
		}

		@Override
		public int compareTo(TileItem o)
		{
			return this.getTile().compareTo(o.getTile());
		}

		@Override
		public String toString()
		{
			return t.toString() + "=" + b;
		}
	}
}
