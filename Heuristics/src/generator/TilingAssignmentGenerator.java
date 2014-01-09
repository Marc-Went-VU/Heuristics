package generator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import tiling.Field;
import tiling.Tile;
import tiling.TileSet;
import tiling.TilingAssignment;
import tiling.TilingFrame;
import tiling.TilingProblem;


public class TilingAssignmentGenerator {
	private long seed = 1233;
	private boolean gui = false;
	private boolean debug = false;
	private Random random;
	private Field field;
	private TilingFrame frame = null;
	
	// Adjustable parameters
	private int iterations = 2;
	private int maxSize;
	private int maxSizeModifier = 3;
	private boolean growBeyondMax = true;
	
	public TilingAssignmentGenerator() {
		random = new Random(seed);
	}
	
	public long getSeed() {
		return seed;
	}

	public void setSeed(long seed) {
		this.seed = seed;
	}

	public boolean isGuiOn() {
		return gui;
	}

	public void setGuiOn(boolean gui) {
		this.gui = gui;
	}

	public boolean isDebugging() {
		return debug;
	}

	public void setDebugging(boolean debug) {
		this.debug = debug;
	}

	public int getIterations() {
		return iterations;
	}

	public void setIterations(int iterations) {
		this.iterations = iterations;
	}

	public int getMaxSizeModifier() {
		return maxSizeModifier;
	}

	public void setMaxSizeModifier(int maxSizeModifier) {
		this.maxSizeModifier = maxSizeModifier;
	}

	public boolean isGrowBeyondMax() {
		return growBeyondMax;
	}

	public void setGrowBeyondMax(boolean growBeyondMax) {
		this.growBeyondMax = growBeyondMax;
	}

	/**
	 * This function will generate a tileset for an area of size x size using
	 * approximately numTiles tiles. The number of actual tiles used will usually
	 * be slightly higher than the number of requested tiles.
	 * 
	 * @param numTiles - The approximate number of tiles to use
	 * @param size - the width and height of the field
	 * @return An assignment
	 */
	public TilingAssignment generate(int numTiles, int size){
		return generate(numTiles,size,size);
	}
	
	/**
	 * This function will generate a tileset for an area of sizeX x sizeY using
	 * approximately numTiles tiles. The number of actual tiles used will usually
	 * be slightly higher than the number of requested tiles.
	 * 
	 * @param numTiles - The approximate number of tiles to use
	 * @param sizeX - the width of the field
	 * @param sizeY - the height of the field
	 * @return An assignment
	 */
	public TilingAssignment generate(int numTiles, int sizeX, int sizeY){
		// The assignment we are generating
		TilingAssignment assignment = initAssignment(sizeX,sizeY);
		
		// The set we will store the resulting tiles in
		TileSet tileSet = new TileSet();
				
		// The field to place the growing tiles on
		field = new Field(sizeX, sizeY);
		if(gui){
			frame = new TilingFrame(field, assignment.getScale());
		}
		
//		System.out.printf("Creating tileset of initially: %d tiles on a canvas of %d x %d\n", numTiles,sizeX,sizeY);
		
		maxSize = sizeX > sizeY ? sizeX / maxSizeModifier : sizeY / maxSizeModifier;
		
		// Some book keeping
		int tilesLeft = numTiles;
		
		List<GrowingTile> tiles = new ArrayList<GrowingTile>();
		List<Position> emptyFields = findEmptyPositions(field);
		int totalArea = emptyFields.size();
		
		// While the field has not yet been fully filled
		while(!emptyFields.isEmpty()){
			// pick a random number of tiles to place
			int tilesToPlace = 0;
			if(tilesLeft > 0){
				tilesToPlace = 1 + random.nextInt(tilesLeft);
				tilesLeft -= tilesToPlace;
			}
			
			// place the tiles
			placeTiles(tiles, emptyFields, tilesToPlace);
			
			if(gui)
				frame.redraw(TilingProblem.DELAY);
			
			// grow the tiles for a few iterations
			tilesLeft = growTiles(tileSet, tilesLeft, tiles);
			
			if(gui)
				frame.redraw(TilingProblem.DELAY);
			
			// find any non-filled places
			emptyFields = findEmptyPositions(field);
		}
		
		moveTilesToSet(tileSet, tiles);

		if(debug) {
			checkTileArea(tileSet, totalArea);
		}
		
		// Sort the tiles large to small
		Collections.sort(tileSet, new Comparator<Tile>(){
			@Override
			public int compare(Tile o1, Tile o2) {
				if(o2.getArea() - o1.getArea() == 0){
					if(o2.getWidth() - o1.getWidth() == 0){
						return o2.getHeight() - o1.getHeight();
					}else{
						return o2.getWidth() - o1.getWidth();
					}
				}else{
					return o2.getArea() - o1.getArea();
				}
			}
		});
		
		assignment.setTiles(tileSet);
		return assignment;
	}

	private int growTiles(TileSet tileSet, int tilesLeft, List<GrowingTile> tiles) {
		boolean regrowing = false;
		for (int i=0;i<iterations;i++){
			boolean stillGrowing = false;
			for(GrowingTile tile: tiles){
				stillGrowing = growTile(tile);
			}
			if(!stillGrowing && ((regrowing && tilesLeft == 0) || (tilesLeft == 0 && !growBeyondMax))){
				// Everyone stopped growing, even though we set everyone to grow again
				// Or we are not allowed to grow any further 
				// Therefore add another tile
				
				moveTilesToSet(tileSet, tiles);
				
				stillGrowing = true;
				regrowing = false;
				
				// Determine the number of tiles we will add
				tilesLeft = getNumberExtraTiles(tilesLeft);
				if(debug)
					System.out.printf("Adding %d extra tiles!\n",tilesLeft);
			} else if(!stillGrowing && tilesLeft == 0 && growBeyondMax){
				// If everyone stopped growing and we are at the maximum number of tiles
				// let everyone grow again
				if(debug)
					System.out.println("Regrowing tiles!");
				
				for(GrowingTile tile: tiles){
					tile.startGrowing();
				}
				regrowing = true;
			}
		}
		return tilesLeft;
	}

	private int getNumberExtraTiles(int tilesLeft) {
		int num = (int) Math.ceil(Math.sqrt(findEmptyPositions(field).size()));
		if(num > 1)
			tilesLeft = random.nextInt(num);
		else if (num == 1)
			tilesLeft = 1;
		return tilesLeft;
	}

	private void moveTilesToSet(TileSet tileSet, List<GrowingTile> tiles) {
		for(GrowingTile tile : tiles){
			// Move the tiles to the result set
			boolean flip = tile.getWidth() < tile.getHeight();
			if(!flip){
				tileSet.add(new Tile(tile.getWidth(),tile.getHeight()));
			}else{
				tileSet.add(new Tile(tile.getHeight(),tile.getWidth()));
			}
		}
		tiles.clear();
	}

	private boolean growTile(GrowingTile tile) {
		boolean stillGrowing = false;
		if(tile.isGrowing()){
			field.undo(tile, tile.getX(), tile.getY());
			if(tile.grow(field, maxSize)){
				stillGrowing = true;
			}
			
			if(!field.placeTile(tile, tile.getX(), tile.getY())){
				System.err.println("Error while generating assignment: Could not place tile on seemingly empty spot!");
			}
			
			if(gui)
				frame.redraw(TilingProblem.DELAY);
		}
		return stillGrowing;
	}

	private void placeTiles(List<GrowingTile> tiles, List<Position> emptyFields, int tilesToPlace) {
		for (int i=0;i<tilesToPlace;i++){
			if(emptyFields.size()>0) {
				int place = random.nextInt(emptyFields.size());
				Position position = emptyFields.get(place);
				emptyFields.remove(place);
				
				GrowingTile tile = new GrowingTile(position.x, position.y);
				if(!field.placeTile(tile, tile.getX(), tile.getY())){
					System.err.println("Error while generating assignment: Could not place tile on seemingly empty spot!");
				}
				tiles.add(tile);
			}
		}
	}
	
	private static TilingAssignment initAssignment(int sizeX, int sizeY) {
		TilingAssignment assignment = new TilingAssignment(sizeX,sizeY);
		if(sizeX > 40 || sizeY > 40){
			assignment.setScale(10);
		}else{
			assignment.setScale(20);
		}
		return assignment;
	}

	private static List<Position> findEmptyPositions(Field field){
		List<Position> emptyFields = new ArrayList<Position>();
		
		for(int i=0;i<field.getWidth();i++){
			for (int j = 0; j < field.getHeight(); j++) {
				if(!field.isOccupied(i, j)){
					emptyFields.add(new Position(i,j));
				}
			}
		}
		
		return emptyFields;
	}
	
	private void checkTileArea(TileSet tileSet, int totalArea) {
		int area = 0;
		for (Tile tile : tileSet){
			area += tile.getArea();
		}
		System.out.printf("New Tileset is done. Total area to cover: %d, Total area of tiles: %d\n",totalArea,area);
	}
}
