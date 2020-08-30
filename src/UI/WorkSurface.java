package UI;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;

/**
 * Class for worksurface off CNC_Machine this is the drawing area
 * @author Tim und Jonas
 *
 */
public class WorkSurface extends Canvas {

	private GraphicsContext gc;
	/**
	 * Constructor for worksurface class
	 * @param i
	 * @param j
	 * 
	 * @author Tim und Jonas
	 */

	public WorkSurface(int i, int j) {
		super(i, j);
		this.gc = this.getGraphicsContext2D();
	}

	/**
	 * Method for drawing a point on the surface
	 * @param x
	 * @param y
	 * 
	 * @author Tim und Jonas
	 */
	public void drawPoint(int x, int y) {

		gc.fillOval(x + 1, y + 1, 4, 4);
		
	}
	
	/**
	 * Method for clearing surface
	 * @param x
	 * @param y
	 * 
	 * @author Tim und Jonas
	 */
	public void clearAll() {
		gc.clearRect(0, 0, this.getWidth(), this.getHeight());
	}

}
