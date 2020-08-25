package UI;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Class for displaying drillpointer
 * @author Jonas
 *
 */
public class DrillPointer extends Canvas {

	private GraphicsContext gc;

	/**
	 * Constructor for Drillpointer class
	 * @param i
	 * @param j
	 */
	public DrillPointer(int i, int j) {
		super(i, j);
		this.gc = this.getGraphicsContext2D();
		gc.setFill(Color.RED);

	}
	/**
	 * Method for displaying drillpointer on current position
	 * @param x
	 * @param y
	 */
	public void drawPoint(int x, int y) {

		gc.clearRect(0, 0, 840, 630);
		gc.fillOval(x, y, 4, 4);

	}
	
	/**
	 * Method to clear drillpointer
	 * @param x
	 * @param y
	 */
	public void clearAll() {
		gc.clearRect(0, 0, this.getWidth(), this.getHeight());
	}

}
