package UI;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Class for displaying HomePoint
 * @author Jonas
 *
 */
public class HomePoint extends Canvas {
	
	private GraphicsContext gc;
	private Color color;
	
	public HomePoint(int i, int j) {
		super(i,j);
		this.gc = this.getGraphicsContext2D();
		this.color = Color.GREEN;
		setHomePoint();
	}
	
	public void setColor(Color color) {
		this.color = color;
		setHomePoint();
	}
	
	private void setHomePoint() {
		this.gc.setFill(this.color);
		gc.fillOval(424, 322, 6, 6);
	}
}

