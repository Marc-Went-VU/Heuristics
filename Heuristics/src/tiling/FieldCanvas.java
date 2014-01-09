package tiling;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.util.List;
import javax.swing.JPanel;

public class FieldCanvas extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2544621875608225004L;
	private final static int MARGINLEFT = 50, MARGINTOP = 20;
	private Field field;
	private int scale;

	public FieldCanvas(Field field, int scale)
	{
		this.field = field;
		this.scale = scale;

		this.setPreferredSize(new Dimension((field.getWidth() * scale) + (2 * MARGINLEFT), (field.getHeight() * scale)
			+ (2 * MARGINTOP)));
	}

	// Draw the current field onto the canvas
	public void paint(Graphics g)
	{
		Image bufferImage = createImage(this.getWidth(), this.getHeight());
		Graphics g2 = bufferImage.getGraphics();

		setBackground(Color.white);
		g2.setColor(Color.DARK_GRAY);
		g2.drawRect(
			FieldCanvas.MARGINLEFT - 1,
			FieldCanvas.MARGINTOP - 1,
			(field.getWidth() * scale) + 2,
			(scale * field.getHeight()) + 2);
		g2.drawRect(
			FieldCanvas.MARGINLEFT - 2,
			FieldCanvas.MARGINTOP - 2,
			(field.getWidth() * scale) + 4,
			(scale * field.getHeight()) + 4);
		String opp = "Area: " + field.getWidth() + " x " + field.getHeight();
		g2.drawString(opp, FieldCanvas.MARGINLEFT, FieldCanvas.MARGINTOP - 5);
		this.tekenVeld(g2);

		g.drawImage(bufferImage, 0, 0, this);
	}

	// Draws all the tiles onto the screen
	private void tekenVeld(Graphics g)
	{
		for (int i = 0; i < field.getWidth(); i++)
		{
			for (int j = 0; j < field.getHeight(); j++)
			{
				List<Tile> tiles = field.getTiles(i, j);
				if (tiles != null && !tiles.isEmpty())
				{
					// only draw the topmost tile
					Tile tile = tiles.get(tiles.size() - 1);
					Color color = tile.getColor();
					g.setColor(color);
					g.fillRect((i * scale) + FieldCanvas.MARGINLEFT, (j * scale) + FieldCanvas.MARGINTOP, scale, scale);
					g.setColor(color.darker());
					g.drawRect((i * scale) + FieldCanvas.MARGINLEFT, (j * scale) + FieldCanvas.MARGINTOP, scale, scale);
				}
				else
				{
					g.setColor(Color.LIGHT_GRAY);
					g.drawRect((i * scale) + FieldCanvas.MARGINLEFT, (j * scale) + FieldCanvas.MARGINTOP, scale, scale);
				}
			}
		}
	}
}
