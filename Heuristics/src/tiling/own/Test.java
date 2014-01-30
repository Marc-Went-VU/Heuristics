package tiling.own;

import tiling.Tile;

public class Test
{

	public static void main(String[] args)
	{
		new Test().start();
	}

	private void start()
	{
		Tile t = new Tile(6, 5);
		Tile t2 = new Tile(t, true);
		System.out.println(t.equals((Object)t2));
	}

}
