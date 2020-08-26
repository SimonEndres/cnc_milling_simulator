package UI;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

public class DrillPointer extends Canvas {

	private GraphicsContext gc;
	private DropShadow dropShadow;

	public DrillPointer(int i, int j) {
		super(i, j);
		this.gc = this.getGraphicsContext2D();
		this.dropShadow = new DropShadow(8.0,15.0,15.0,Color.BLACK);
		
	}

	public void drawPoint(int x, int y, boolean milling) {

		gc.clearRect(0, 0, 840, 630);
		gc.fillOval(x, y, 6, 6);
		gc.setFill(Color.RED);
		if(!milling) {
			gc.applyEffect(dropShadow);			
		}

	}
	
	public void clearAll() {
		gc.clearRect(0, 0, this.getWidth(), this.getHeight());
	}

}
