package UI;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class HomePoint extends Canvas {
	
	private GraphicsContext gc;
	
	public HomePoint(int i, int j) {
		super(i,j);
		this.gc = this.getGraphicsContext2D();
		this.gc.setFill(Color.GREEN);
		gc.fillOval(420, 315, 6, 6);
	}
}

