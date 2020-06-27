package cnc_frase_testing;

import java.util.ArrayList;

import javafx.animation.AnimationTimer;

public class SimulateMill {

	protected ArrayList<Coordinates> coordinates;
	private WorkSurface workSurface;
	private UI ui;
	static int counter = 0;

	public SimulateMill(ArrayList<Coordinates> coordinates, WorkSurface workSurface, UI ui) {
		this.coordinates = coordinates;
		this.workSurface = workSurface;
		this.ui = ui;
		System.out.println("beep");
	}

	public void startDrawing() {
		//f�r Logzeiten
		ServiceClass.setStartzeit();
		
		AnimationTimer timer = new AnimationTimer() {
			
			@Override
			public void handle(long now) {
				long lastUpdateTime = 0;
				if (now - lastUpdateTime >= 2000000) {
					draw();
				    lastUpdateTime = now;
				}
			}
		};
		
		timer.start();
//		timer.stop();
		
		//Speicherung des Logs im File -  muss ans Ende der Simulate Mill
		//ServiceClass.logToFile();
	}
	
	private void draw() {
		if (coordinates.get(counter).isEnd()) {
			ui.setCommandsDone();
			ui.updateCommandsToDo();
			ServiceClass.putLogArray();
		} else {
			workSurface.drawPoint((coordinates.get(counter).getX() + 420), ( - coordinates.get(counter).getY() + 315),
				coordinates.get(counter).isMill());
		}
		
		counter++;
//		System.out.println("( "+ ((coordinates.get(counter).getX() + 420)) + " / " + (( - coordinates.get(counter).getY() + 315)) + " )");
		
	}
	// zum zeichnen eines Punktes auf der Oberfl�che
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
