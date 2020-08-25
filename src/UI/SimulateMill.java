package UI;

import java.util.ArrayList;

import cnc_frase_testing.CommandProcessor;
import cnc_frase_testing.Coordinates;
import javafx.animation.AnimationTimer;

public class SimulateMill {

	protected ArrayList<Coordinates> coordinates;
	private WorkSurface workSurface;
	private DrillPointer drillPointer;
	private CoolingSimulater coolingSimulater;
	private UIController ui;
	private CommandProcessor cp;
	private int counter = 0;
	private int speedTmp;
	private int speed = 150000000;
	private boolean running;
	AnimationTimer timer = null;

	public SimulateMill(ArrayList<Coordinates> coordinates, WorkSurface workSurface, DrillPointer drillPointer, CoolingSimulater coolingSimulater, 
			CommandProcessor cp, UIController ui) {
		this.coordinates = coordinates;
		this.workSurface = workSurface;
		this.drillPointer = drillPointer;
		this.coolingSimulater = coolingSimulater;
		this.cp = cp;
		this.ui = ui;
		this.speedTmp = 0;
		this.running = true;
		System.out.println("beep");
	}

	public void startDrawing() {
		// f�r Logzeiten
		cp.setStartTime();

		timer = new AnimationTimer() {

			long lastUpdateTime = 0;

			@Override
			public void handle(long now) {
				if (now - lastUpdateTime >= speed) {
					if (counter < coordinates.size()) {
						draw();
					} else {
						//End of milling process
						ui.millEnd();
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
				speedTmp = 0;
				if (coordinates.get(counter).isCooling()) {
					speed = 70000000;
					workSurface.drawPoint((coordinates.get(counter).getX() + 420),
							(-coordinates.get(counter).getY() + 315));
					drillPointer.drawPoint((coordinates.get(counter).getX() + 420),
							(-coordinates.get(counter).getY() + 315), true);
					coolingSimulater.drawPoint((coordinates.get(counter).getX() + 420),
							(-coordinates.get(counter).getY() + 315));
				} else {
					coolingSimulater.clearAll();
					speed = 80000000;
					workSurface.drawPoint((coordinates.get(counter).getX() + 420),
							(-coordinates.get(counter).getY() + 315));
					drillPointer.drawPoint((coordinates.get(counter).getX() + 420),
							(-coordinates.get(counter).getY() + 315), true);

				}
			} else {
				if (speedTmp != ui.getSpeed()) {
					speedTmp = ui.getSpeed();
					switch (speedTmp) {
					case 4:
						speed = 60000000;
						break;
					case 5:
						speed = 50000000;
						break;
					case 6:
						speed = 40000000;
						break;
					case 7:
						speed = 30000000;
						break;
					case 8:
						speed = 20000000;
						break;
					}

				}
				drillPointer.drawPoint((coordinates.get(counter).getX() + 420),
						(-coordinates.get(counter).getY() + 315), false);
				if (coordinates.get(counter).isCooling()) {
					coolingSimulater.drawPoint((coordinates.get(counter).getX() + 420),
							(-coordinates.get(counter).getY() + 315));
				}
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

	public void reset() {
		timer.stop();
		coordinates.clear();
		coordinates.add(new Coordinates(0, 0, false, false));
		counter = 0;
		workSurface.clearAll();
		drillPointer.clearAll();
		coolingSimulater.clearAll();
		drillPointer = null;
		cp.logMessage("Reset", "Worksurface sucessfully reset", " by User");
		cp.logAll();
		cp.resetCpCounter();
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

}
