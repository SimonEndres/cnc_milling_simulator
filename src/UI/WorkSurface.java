package UI;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;

public class WorkSurface extends Canvas {

	private GraphicsContext gc;
	private GaussianBlur gausBlur;

	public WorkSurface(int i, int j) {
		super(i, j);
		this.gc = this.getGraphicsContext2D();
		this.gausBlur = new GaussianBlur(0.5);
	}

	public void drawPoint(int x, int y) {

		gc.fillOval(x, y, 4, 4);
		gc.applyEffect(gausBlur);
		
	}
	public void clearAll() {
		gc.clearRect(0, 0, this.getWidth(), this.getHeight());
	}

}
