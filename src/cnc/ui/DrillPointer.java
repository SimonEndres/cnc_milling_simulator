package cnc.ui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

/**
 * 
 * Class is needed to simulate the drill on the UI
 * 
 * @author Tim, Jonas, Simon
 *
 */
public class DrillPointer extends Canvas {

	private GraphicsContext gc;
	private DropShadow dropShadow;
	private Color color;

	public DrillPointer(int i, int j) {
		super(i, j);
		this.color = Color.RED;
		this.gc = this.getGraphicsContext2D();
		this.dropShadow = new DropShadow(8.0,15.0,15.0,Color.BLACK);
		
	}

	/**
	 * 
	 * Point is drawn onto surface and shadow is applied when not milling.
	 * 
	 * @author Simon, Jonas, Tim
	 * @param x
	 * @param y
	 * @param milling
	 */
	public void drawPoint(int x, int y, boolean milling) {

		gc.clearRect(0, 0, 854, 641);
		gc.fillOval(x, y, 6, 6);
		gc.setFill(this.color);
		if(!milling) {
			gc.applyEffect(dropShadow);			
		}

	}
	
	
	/**
	 * 
	 * Method to clear DrillPointer
	 * 
	 * @author Jonas
	 */
	public void clearAll() {
		gc.clearRect(0, 0, this.getWidth(), this.getHeight());
	}
	
	/**
	 * 
	 * Method to change the color of the drill
	 * 
	 * @author Tim, Jonas, Simon
	 * @param color
	 * @param x
	 * @param y
	 */
	public void setColor(Color color, int x, int y) {
		this.color = color;
		gc.setFill(this.color);
		gc.clearRect(0, 0, 854, 641);
		gc.fillOval(x, y, 6, 6);
	}

}
