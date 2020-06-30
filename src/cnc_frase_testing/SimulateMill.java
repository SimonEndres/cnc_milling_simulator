package cnc_frase_testing;

import java.util.ArrayList;

import javafx.animation.AnimationTimer;

public class SimulateMill {

	protected ArrayList<Coordinates> coordinates;
	private WorkSurface workSurface;
	private UI ui;
	private CommandProcessor cp;
	static int counter = 0;

	public SimulateMill(ArrayList<Coordinates> coordinates, WorkSurface workSurface, CommandProcessor cp, UI ui) {
		this.coordinates = coordinates;
		this.workSurface = workSurface;
		this.cp = cp;
		this.ui = ui;
		System.out.println("beep");
	}

	public void startDrawing() {
		//für Logzeiten
		cp.setStartzeit();
		
		AnimationTimer timer = new AnimationTimer() {
			
			@Override
			public void handle(long now) {
				long lastUpdateTime = 0;
				if (now - lastUpdateTime >= 2000000) {
					if (counter < coordinates.size()) {
						draw();
					}else {
						//Speicherung des Logs im File -  muss ans Ende der Simulate Mill
						cp.logAll();
					}
				    lastUpdateTime = now;
				}
			}
		};
		
		timer.start();
//		timer.stop();
		
	}
	
	private void draw() {
		if (coordinates.get(counter).isEnd()) {
			ui.setCommandsDone();
			ui.updateCommandsToDo();
			cp.putLogArray();
		} else {
			workSurface.drawPoint((coordinates.get(counter).getX() + 420), ( - coordinates.get(counter).getY() + 315),
				coordinates.get(counter).isMill());
		}
		
		counter++;
//		System.out.println("( "+ ((coordinates.get(counter).getX() + 420)) + " / " + (( - coordinates.get(counter).getY() + 315)) + " )");
		
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
