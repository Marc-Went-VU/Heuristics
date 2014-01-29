package tiling.own;

import tiling.Field;
import tiling.Tile;

public class Test
{

	public static void main(String[] args)
	{
		new Test().start();
	}

	private void start()
	{
		Tile t = new Tile(6, 6);
		Field f = new Field(10, 10);
		Field f2 = new Field(f);
		f.placeTileSecure(t, 0, 0);
		f2.placeTileSecure(t, 0, 0);

		TileValue tv1 = new TileValue(t, new Coordinate(0, 0));
		TileValue tv2 = new TileValue(t, new Coordinate(0, 0));
		FieldSet fs1 = new FieldSet(null, f, tv1, null, 0);
		FieldSet fs2 = new FieldSet(null, f2, tv2, null, 0);
		System.out.println(fs1.equals(fs2));
	}

}
