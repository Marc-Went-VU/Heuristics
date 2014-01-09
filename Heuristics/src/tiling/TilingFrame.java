package tiling;
import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;


public class TilingFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -360885512080963508L;
	private FieldCanvas fieldCanvas;

	public TilingFrame(Field field, int scale){
		fieldCanvas = new FieldCanvas(field, scale);
	 	// frame settings:
		setTitle("Heuristics 2013 - Tegelzetten");
		setLayout(new BorderLayout());
		add(this.fieldCanvas, BorderLayout.CENTER);
		
		// Exit the program when the window is closed
		this.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				System.exit(0);
			}
		});
		
		// Close the window when escape is pressed
		this.addKeyListener(new KeyAdapter(){
			@Override
			public void keyPressed(KeyEvent e) {
				super.keyPressed(e);
				if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
					System.exit(0);
				}
			}
			
		});
		
		// Resize the window
		pack();
		
		// Make the window visible
		setVisible(true);
	}
	
	public void redraw(int delay){
		repaint();
		invalidate();
		validate();
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
