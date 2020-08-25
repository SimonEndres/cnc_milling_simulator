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
	private int counter = 0;
	private int speedTmp;
	private int speed = 150000000;
	private boolean running;
	AnimationTimer timer = null;

	public SimulateMill(ArrayList<Coordinates> coordinates, WorkSurface workSurface, DrillPointer drillPointer,
			CommandProcessor cp, UIController ui) {
		this.coordinates = coordinates;
		this.workSurface = workSurface;
		this.drillPointer = drillPointer;
		this.cp = cp;
		this.ui = ui;
		this.speedTmp = 0;
		this.running = true;
		System.out.println("beep");
	}

	public void startDrawing() {
		// für Logzeiten
		cp.setStartTime();

		timer = new AnimationTimer() {

			long lastUpdateTime = 0;

			@Override
			public void handle(long now) {
				if (now - lastUpdateTime >= speed) {
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

	}

	private void draw() {
		if (coordinates.get(counter).isEnd()) {
			ui.setCommandsDone();
			ui.updateCommandsToDo();
			cp.logCommandsDone();
		} else {
			if (coordinates.get(counter).isMill()) {
				if (coordinates.get(counter).isCooling()) {
					speed = 100000000;
//				System.out.println("speed: " + speed);
				} else {
					speed = 150000000;
				}
				workSurface.drawPoint((coordinates.get(counter).getX() + 420),
						(-coordinates.get(counter).getY() + 315));
				drillPointer.drawPoint((coordinates.get(counter).getX() + 420),
						(-coordinates.get(counter).getY() + 315));
			} else {
				if (speedTmp != ui.getSpeed()) {
					speedTmp = ui.getSpeed();
					switch (speedTmp) {
					case 4:
						speed = 80000000;
						System.out.println("speed4");
						break;
					case 5:
						speed = 60000000;
						break;
					case 6:
						speed = 40000000;
						break;
					case 7:
						speed = 20000000;
						break;
					case 8:
						speed = 9900000;
						break;
					}

				}
				drillPointer.drawPoint((coordinates.get(counter).getX() + 420),
						(-coordinates.get(counter).getY() + 315));
			}
		}

		counter++;
//		System.out.println("( "+ ((coordinates.get(counter).getX() + 420)) + " / " + (( - coordinates.get(counter).getY() + 315)) + " )");

	}

	public void pause() {
		timer.stop();
	}

	public void unpause() {
		timer.start();
	}

	public void terminate() {
		timer.stop();
		coordinates.clear();
		coordinates.add(new Coordinates(0, 0, false, false));
		counter = 0;
		workSurface.clearAll();
		drillPointer.clearAll();
		drillPointer = null;
		cp.logMessage("Terminate", "Process terminated by User", "reset");
		cp.logAll();
		cp.resetCpCounter();
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

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

}
