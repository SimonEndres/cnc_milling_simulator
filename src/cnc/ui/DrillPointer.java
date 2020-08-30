package cnc.ui;

import cnc.logic.Coordinates;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

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

	public void drawPoint(int x, int y, boolean milling) {

		gc.clearRect(0, 0, 854, 641);
		gc.fillOval(x, y, 6, 6);
		gc.setFill(this.color);
		if(!milling) {
			gc.applyEffect(dropShadow);			
		}

	}
	
	public void clearAll() {
		gc.clearRect(0, 0, this.getWidth(), this.getHeight());
	}
	
	public void setColor(Color color, int x, int y) {
		this.color = color;
		gc.setFill(this.color);
		gc.clearRect(0, 0, 854, 641);
		gc.fillOval(x, y, 6, 6);
	}

}
