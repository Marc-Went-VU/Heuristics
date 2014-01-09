package generator;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import tiling.Tile;
import tiling.TileSet;
import tiling.TilingAssignment;


public class AssignmentGenerator {

	public void start(){
		TilingAssignmentGenerator generator = new TilingAssignmentGenerator();
		Random random = new Random(42);
		generator.setGuiOn(false);
		for(int tiles=15;tiles<=55;tiles+=10){
			for (int settings=0;settings<5;settings++){
				setGeneratorSettings(generator,settings);
				for(int number=0;number<5;number++){
					TilingAssignment assignment = createAssignment(generator, random, tiles);
					saveAssignmentToFile(assignment,tiles,settings,number);
				}
			}
		}
	}

	private void saveAssignmentToFile(TilingAssignment assignment, int tiles,
			int settings, int number) {
		try {
			PrintWriter writer = new PrintWriter(String.format("%d-%d-%d.tiles", tiles,settings,number),"UTF-8");
			
			writer.printf("width: %d height: %d scale: %d\n", assignment.getWidth(),assignment.getHeight(),assignment.getScale());
			
			TileSet tileset = assignment.getTiles();
			int num = 1;
			for(int i=0;i<tileset.size();i++){
				Tile tile = tileset.peek(i);
				if(i < tileset.size()-1 && tile.equals(tileset.peek(i+1))){
					num++;
				}else{
					writer.printf("%d times %dx%d\n", num,tile.getWidth(),tile.getHeight());
					num = 1;
				}
			}
			
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private void setGeneratorSettings(TilingAssignmentGenerator generator,
			int settings) {
		switch (settings) {
			default:
			case 0:
				generator.setGrowBeyondMax(false);
				generator.setIterations(2);
				generator.setMaxSizeModifier(3);
				break;
			case 1:
				generator.setGrowBeyondMax(true);
				generator.setIterations(2);
				generator.setMaxSizeModifier(3);
				break;
			case 2:
				generator.setGrowBeyondMax(false);
				generator.setIterations(3);
				generator.setMaxSizeModifier(3);
				break;
			case 3:
				generator.setGrowBeyondMax(true);
				generator.setIterations(3);
				generator.setMaxSizeModifier(3);
				break;
			case 4:
				generator.setGrowBeyondMax(false);
				generator.setIterations(4);
				generator.setMaxSizeModifier(2);
				break;
		}
	}

	private TilingAssignment createAssignment(TilingAssignmentGenerator generator, Random random, int tiles) {
		int sizeX = Math.max(tiles + (int)Math.ceil((Math.sqrt(tiles)*random.nextGaussian())),tiles-(int)Math.sqrt(tiles));
		int sizeY = Math.max(tiles + (int)Math.ceil((Math.sqrt(tiles)*random.nextGaussian())),tiles-(int)Math.sqrt(tiles));
		long startTime = System.currentTimeMillis();
		
		System.out.printf("Creating tileset of initially: %d tiles on a canvas of %d x %d\n", tiles,sizeX,sizeY);
		TilingAssignment assignment = generator.generate(tiles,sizeX,sizeY);
		int numTiles = assignment.getTiles().size();
		while(numTiles > tiles+5 || numTiles < tiles-5){
			assignment = generator.generate(tiles,sizeX,sizeY);
			numTiles = assignment.getTiles().size();	
		}
		long endTime = System.currentTimeMillis();
		
		long duration = endTime - startTime;
		long hours = TimeUnit.MILLISECONDS.toHours(duration);
		duration -= TimeUnit.HOURS.toMillis(hours);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(duration);
		duration -= TimeUnit.MINUTES.toMillis(minutes);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(duration);
		duration -= TimeUnit.SECONDS.toMillis(seconds);

		String val = String.format("%d hours, %d minutes, %d seconds, %d milliseconds",
				hours,
				minutes,
				seconds,
				duration
		);
		
		System.out.println("Generated a new set in "+val);
		
		System.out.printf("Number of tiles in the set:               %d \n",
		assignment.getTiles().size());
		System.out.printf("Number of differently sized tiles in set: %d \n",
		assignment.getTiles().getNumberOfTileSizes());
		
		return assignment;
	}
	
	public static void main(String[] args) {
		new AssignmentGenerator().start();
	}
}
