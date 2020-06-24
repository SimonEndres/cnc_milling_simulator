package cnc_frase_testing;

import java.util.ArrayList;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class SimulateMill {
	
	protected ArrayList<Coordinates> coordinates;
	private WorkSurface workSurface;
	
	public SimulateMill(ArrayList<Coordinates> coordinates, WorkSurface workSurface) {
		this.coordinates = coordinates;
		this.workSurface = workSurface;
		System.out.println("beep");
		workSurface.drawPoint(0, 0, true);
	}
	//zum zeichnen eines Punktes auf der Oberfläche
//		void drawPoint(int x, int y, boolean fraesen) {
//			Circle bohrkopf = new Circle();
//			bohrkopf.setCenterX((float) x);
//			bohrkopf.setCenterY((float) y);
//			bohrkopf.setRadius(2.0f);
//	
//			bohrkopf.setFill(Color.ORANGE);
//			if (fraesen)
//				cutLine.getChildren().add(bohrkopf);
//		}



}
