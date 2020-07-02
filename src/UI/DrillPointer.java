package UI;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class DrillPointer extends Canvas {
	
	private GraphicsContext gc;
	
	public DrillPointer(int i, int j) {
		super(i,j);
		this.gc = this.getGraphicsContext2D();

	}
	
	
	public void drawPoint(int x, int y) {
		
		gc.clearRect(0, 0, 840, 630);
		gc.fillOval(x, y, 4, 4);
		gc.setFill(Color.RED);
		
	}
	
}
