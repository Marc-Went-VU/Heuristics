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

	private TileList(ArrayList<TileItem> list)
	{
		this.list = list;
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

	public Tile getFirstAvailable()
	{
		pos = 0;
		while (!list.get(pos).freeToUse())
		{
			pos++;
		}
		if (pos < 0 || pos >= list.size())
			return null;
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

	public Tile getByWidth(int w, SORT s)
	{
		TileList items = getListByWidth(w);
		if (items == null)
			return null;
		switch (s)
		{
		case AREA:
			return items.get(0).getTile();
		case RANDOM:
		default:
			return items.get(new Random().nextInt(items.size())).getTile();
		}
	}

	public TileList getListByWidth(int w)
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
				tryAdd(items, item);
			else if (t.getHeight() == w)
			{
				t.rotate();
				tryAdd(items, item);
			}

		}
		if (items.size() == 0)
			return null;
		return new TileList(items);

	}

	public TileList getListByHeight(int h)
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
			if (t.getWidth() == h)
			{
				t.rotate();
				tryAdd(items, item);
			}
			else if (t.getHeight() == h)
			{
				tryAdd(items, item);
			}

		}
		if (items.size() == 0)
			return null;
		return new TileList(items);
	}

	public Tile getByHeight(int h, SORT s)
	{
		TileList items = getListByHeight(h);
		if (items == null)
			return null;
		switch (s)
		{
		case AREA:
			return items.get(0).getTile();
		case RANDOM:
		default:
			return items.get(new Random().nextInt(items.size())).getTile();
		}
	}

	public Tile getSecureByWidth(int w)
	{
		TileItem item = null;
		Tile t;
		for (int i = 0; i < list.size(); i++)
		{
			item = list.get(i);
			if (!item.freeToUse())
				continue;
			t = item.getTile();
			if (t.getWidth() == w)
				return t;
		}
		return null;
	}

	public Tile getSecureByHeight(int h)
	{
		TileItem item = null;
		Tile t;
		for (int i = 0; i < list.size(); i++)
		{
			item = list.get(i);
			if (!item.freeToUse())
				continue;
			t = item.getTile();
			if (t.getHeight() == h)
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

	private int size()
	{
		return this.list.size();
	}

	private TileItem get(int i)
	{
		if (i < 0 || i > this.list.size())
			return null;
		return this.list.get(i);
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

	private void tryAdd(ArrayList<TileItem> itemList, TileItem tI)
	{
		Tile t1;
		Tile t2 = tI.getTile();
		for (TileItem item : itemList)
		{
			t1 = item.getTile();
			if ((t1.getWidth() == t2.getWidth() && t1.getHeight() == t2.getHeight())
				|| (t1.getHeight() == t2.getWidth() && t1.getWidth() == t2.getHeight()))
			{
				return;
			}
		}
		itemList.add(tI);
	}

	public enum SORT
	{
		AREA,
		RANDOM
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

	public ArrayList<Tile> getAvailableTiles()
	{
		ArrayList<Tile> tiles = new ArrayList<Tile>();
		for (TileItem ti : list)
		{
			if (ti.freeToUse())
				tiles.add(ti.getTile());
		}
		return tiles;
	}
}
