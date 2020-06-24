package cnc_frase_testing;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class WorkSurface extends Canvas{
	private GraphicsContext gc;
	public WorkSurface(int i, int j) {
		super(i,j);
		this.gc = this.getGraphicsContext2D();
		//gc.strokeText("Hello Canvas", 150, 100);
	}
	void drawPoint(int x, int y, boolean fraesen) {

		if (fraesen)
			gc.fillOval(x, y, 2, 2);
	}
//	void drawPoint(int x, int y, boolean fraesen) {
//		Circle bohrkopf = new Circle();
//		bohrkopf.setCenterX((float) x);
//		bohrkopf.setCenterY((float) y);
//		bohrkopf.setRadius(200.0f);
//
//		bohrkopf.setFill(Color.ORANGE);
//		if (fraesen)
//			cutLine.getChildren().add(bohrkopf);
//	}

}
