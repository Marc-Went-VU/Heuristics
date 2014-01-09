package tiling;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class TilingAssignment {
	private TileSet tiles;
	private int width;
	private int height;
	private int scale;

	public TilingAssignment(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public TileSet getTiles() {
		return tiles;
	}

	public void setTiles(TileSet tiles) {
		this.tiles = tiles;
	}

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}
	
	public static TilingAssignment loadFromFile(String filename) throws IOException{
		FileInputStream fis = new FileInputStream(filename);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
		String line = br.readLine();
		
		// parsing the first line:
		// width: %d height: %d scale: %d
		String[] parts = line.split(" ");
		int width = Integer.parseInt(parts[1]);
		int height = Integer.parseInt(parts[3]);
		int scale = Integer.parseInt(parts[5]);
		
		TilingAssignment assignment = new TilingAssignment(width, height);
		assignment.setScale(scale);
		
		TileSet tiles = new TileSet();
		while ((line = br.readLine()) != null) {
			// reading tile line:
			// %d times %dx%d
			parts = line.split(" ");
			String[] tileparts = parts[2].split("x");
			
			int times = Integer.parseInt(parts[0]);
			int tileWidth = Integer.parseInt(tileparts[0]);
			int tileHeight = Integer.parseInt(tileparts[1]);
			
			for(int i=0;i<times;i++){
				Tile tile = new Tile(tileWidth, tileHeight);
				tiles.add(tile);
			}
		}
		assignment.setTiles(tiles);
		
		// Done with the file
		br.close();
		
		return assignment;
	}
}
