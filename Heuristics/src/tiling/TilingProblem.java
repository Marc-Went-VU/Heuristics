package tiling;

import java.io.IOException;
import tiling.own.Algorithm;

public class TilingProblem
{
	private Field field;
	TilingAssignment assignment;
	private TilingFrame frame;
	private int numberOfSteps, counter;
	// Delay in milliseconds, slows down the program so you can see the steps
	public static final int DELAY = 50;

	TilingProblem()
	{
	}

	// Initialises the field and graphical display
	public void init(int fieldWidth, int fieldHeight, int delay, int scale)
	{
		field = new Field(fieldWidth, fieldHeight);
		frame = new TilingFrame(this.field, scale);
		numberOfSteps = counter = 0;
	}

	public void start()
	{

		/**
		 * Settings: Put the delay to a value > 0 You can also disable the
		 * creation of the frame to disable the GUI in total. This will allow
		 * the program to run much quicker, but you will not see the results
		 */

		TilingAssignment assignment;
		try
		{
			assignment = TilingAssignment.loadFromFile("tilesets/15-0-0.tiles");

			System.out.printf("Number of tiles in the set:               %d \n", assignment.getTiles().size());
			System.out.printf("Number of differently sized tiles in set: %d \n", assignment.getTiles().getNumberOfTileSizes());

			init(assignment.getWidth(), assignment.getHeight(), DELAY, assignment.getScale());

			Algorithm a = new Algorithm(frame, field, assignment);
			a.runAlgorithm();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		new TilingProblem().start();
	}

}
