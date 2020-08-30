package cnc.ui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;

/**
 * 
 * Class needed to simulate the cooling function on the UI.
 * 
 * @author Simon
 *
 */
public class CoolingSimulator extends Canvas {

	private GraphicsContext gc;
	private GaussianBlur gausBlur;

	public CoolingSimulator(int i, int j) {
		super(i, j);
		this.gc = this.getGraphicsContext2D();
		this.gausBlur = new GaussianBlur(8);
	}
	
	/**
	 * 
	 * Simulates the cooling using a GaussianBlur effect
	 * 
	 * @param x
	 * @param y
	 */
	public void drawPoint(int x, int y) {

		gc.fillOval(x - 4, y - 4, 14, 14);
		gc.setFill(Color.SKYBLUE);
		gc.applyEffect(gausBlur);

	}

	public void clearAll() {
		gc.clearRect(0, 0, this.getWidth(), this.getHeight());
	}

}
