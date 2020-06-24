package cnc_frase_testing;

import java.util.ArrayList;

import javafx.animation.AnimationTimer;

public class SimulateMill {

	protected ArrayList<Coordinates> coordinates;
	private WorkSurface workSurface;
	static int counter = 0;

	public SimulateMill(ArrayList<Coordinates> coordinates, WorkSurface workSurface) {
		this.coordinates = coordinates;
		this.workSurface = workSurface;
		System.out.println("beep");
	}

	public void startDrawing() {
		
		AnimationTimer timer = new AnimationTimer() {
			
			@Override
			public void handle(long now) {
				long lastUpdateTime = 0;
				if (now - lastUpdateTime >= 200000) {
					draw();
				    lastUpdateTime = now;
				}
			}
		};
		
		timer.start();
//		timer.stop();
		
	}
	
	private void draw() {
		
		workSurface.drawPoint(coordinates.get(counter).getX(), coordinates.get(counter).getY(),
				coordinates.get(counter).isMill());
		counter++;
		
	}
	// zum zeichnen eines Punktes auf der Oberfläche
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
