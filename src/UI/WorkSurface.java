package UI;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class WorkSurface extends Canvas {

	private GraphicsContext gc;

	public WorkSurface(int i, int j) {
		super(i, j);
		this.gc = this.getGraphicsContext2D();

	}

	public void drawPoint(int x, int y) {

		gc.fillOval(x, y, 4, 4);
		
	}

}