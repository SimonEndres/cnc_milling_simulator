package UI;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class DrillPointer extends Canvas {
	
	private GraphicsContext gc;
	
	public DrillPointer(int i, int j) {
		super(i,j);
		this.gc = this.getGraphicsContext2D();

	}
	
	
	public void drawPoint(int x, int y) {
		gc.clearRect(0, 0, getLayoutX(), getLayoutY());
		gc.fillOval(x, y, 2, 2);
	
	}
	
}
