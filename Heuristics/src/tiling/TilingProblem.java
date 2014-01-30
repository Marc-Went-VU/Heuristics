package tiling;

import java.io.IOException;
import tiling.own.Algorithm;
import tiling.own.Int;

public class TilingProblem
{
	private Field field;
	TilingAssignment assignment;
	private TilingFrame frame;
	// Delay in milliseconds, slows down the program so you can see the steps
	public static final int DELAY = 200;

	public static final String PROBLEM = "15-0-4";

	TilingProblem()
	{
	}

	// Initialises the field and graphical display
	public void init(int fieldWidth, int fieldHeight, int scale, String problemSet)
	{
		field = new Field(fieldWidth, fieldHeight);
		frame = new TilingFrame(this.field, scale, problemSet);
	}

	public void start()
	{
		this.start(PROBLEM);

	}

	private void start(String problemSet)
	{
		/**
		 * Settings: Put the delay to a value > 0 You can also disable the
		 * creation of the frame to disable the GUI in total. This will allow
		 * the program to run much quicker, but you will not see the results
		 */

		TilingAssignment assignment;
		try
		{
			assignment = TilingAssignment.loadFromFile("tilesets/" + problemSet + ".tiles");

			System.out.printf("Number of tiles in the set:               %d \n", assignment.getTiles().size());
			System.out.printf("Number of differently sized tiles in set: %d \n", assignment.getTiles().getNumberOfTileSizes());

			init(assignment.getWidth(), assignment.getHeight(), assignment.getScale(), problemSet);

			Algorithm a = new Algorithm(frame, field, assignment.getTiles());
			a.runAlgorithm();//
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void start(int tile, int x, int y)
	{
		this.start(tile + "-" + x + "-" + y);
	}

	public static void main(String[] args)
	{
		if (args.length == 1)
		{
			String problemSet = args[0];
			new TilingProblem().start(problemSet);
		}
		else if (args.length == 3)
		{
			int t = Integer.parseInt(args[0]);
			int x = Integer.parseInt(args[1]);
			int y = Integer.parseInt(args[2]);
			new TilingProblem().start(t, x, y);
		}
		else
		{
			boolean multiple = false;
			boolean thread = true;
			if (multiple)
			{
				int counter = 0;
				for (final Int t = new Int(45); t.getI() <= 55; t.add(10))
				{
					for (final Int x = new Int(0); x.getI() <= 4; x.add(1))
					{
						for (final Int y = new Int(0); y.getI() <= 4; y.add(1))
						{
							System.out.printf("Starting t: %d, x: %d, y: %d\n", t.getI(), x.getI(), y.getI());
							if (counter >= Runtime.getRuntime().availableProcessors() - 3)
								break;
							final int ti = t.getI();
							final int xi = x.getI();
							final int yi = y.getI();

							if (thread)
							{
								new Thread(new Runnable()
								{

									@Override
									public void run()
									{
										new TilingProblem().start(ti, xi, yi);
									}
								}).start();
							}
							else
								new TilingProblem().start(ti, xi, yi);
							counter++;

						}
					}
				}
			}
			else
			{
				int t = 55;
				int x = 0;
				int y = 0;
				new TilingProblem().start(t, x, y);
			}
		}
	}

}
