package UI;

import java.util.ArrayList;

import cnc_frase_testing.CommandProcessor;
import cnc_frase_testing.Coordinates;
import javafx.animation.AnimationTimer;

public class SimulateMill {

	protected ArrayList<Coordinates> coordinates;
	private WorkSurface workSurface;
	private DrillPointer drillPointer;
	private UIController ui;
	private CommandProcessor cp;
	static int counter = 0;

	public SimulateMill(ArrayList<Coordinates> coordinates, WorkSurface workSurface, DrillPointer drillPointer,
			CommandProcessor cp, UIController ui) {
		this.coordinates = coordinates;
		this.workSurface = workSurface;
		this.drillPointer = drillPointer;
		this.cp = cp;
		this.ui = ui;
		System.out.println("beep");
	}

	public void startDrawing() {
		// für Logzeiten
		cp.setStartzeit();

		AnimationTimer timer = new AnimationTimer() {

			long lastUpdateTime = 0;

			@Override
			public void handle(long now) {
				if (now - lastUpdateTime >= 500000) {
					if (counter < coordinates.size()) {
						draw();
					} else {
						// Speicherung des Logs im File - muss ans Ende der Simulate Mill
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
			if (coordinates.get(counter).isMill()) {
				workSurface.drawPoint((coordinates.get(counter).getX() + 420), (-coordinates.get(counter).getY() + 315));
			}
			drillPointer.drawPoint((coordinates.get(counter).getX() + 420), (-coordinates.get(counter).getY() + 315));
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
