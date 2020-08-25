package UI;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;

public class CoolingSimulater extends Canvas {

	private GraphicsContext gc;
	private GaussianBlur gausBlur;

	public CoolingSimulater(int i, int j) {
		super(i, j);
		this.gc = this.getGraphicsContext2D();
		this.gausBlur = new GaussianBlur(8);
	}

	public void drawPoint(int x, int y) {

//		gc.clearRect(0, 0, 840, 630);
		gc.fillOval(x-4, y-4, 14, 14);
		gc.setFill(Color.SKYBLUE);
		gc.applyEffect(gausBlur);

	}

	public void clearAll() {
		gc.clearRect(0, 0, this.getWidth(), this.getHeight());
	}

}
