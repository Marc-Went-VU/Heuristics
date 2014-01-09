package tiling;

import java.io.IOException;

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
			assignment = TilingAssignment.loadFromFile("15-0-0.tiles");

			System.out.printf("Number of tiles in the set:               %d \n", assignment.getTiles().size());
			System.out.printf("Number of differently sized tiles in set: %d \n", assignment.getTiles().getNumberOfTileSizes());

			init(assignment.getWidth(), assignment.getHeight(), DELAY, assignment.getScale());

			Tile tile = assignment.getTiles().get(0);
			;
			int x = 0;
			int y = 0;
			while (assignment.getTiles().size() >= 1)
			{
				System.out.printf(
					"Placing tile of size:                     %d, %d at %d, %d",
					tile.getWidth(),
					tile.getHeight(),
					x,
					y);

				if (field.placeTile(tile, x, y))
					System.out.println("...succes!");
				else
					System.out.println("...failure! :(\n");

				frame.redraw(DELAY);

				System.out.printf("Undoing last placed tile!\n");
				field.undo(tile, x, y);
				frame.redraw(DELAY);

				tile = assignment.getTiles().get(0);
			}
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
