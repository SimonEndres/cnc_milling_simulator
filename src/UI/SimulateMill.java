package UI;

import java.util.ArrayList;

import cnc_frase_testing.CommandProcessor;
import cnc_frase_testing.Coordinates;
import javafx.animation.AnimationTimer;

/**
 * 
 * Class for simulating the drilling of previously calculated coordinates
 * coordinates have already been calculated in Drill
 * 
 * @author Tim, Jonas, Simon
 *
 */
public class SimulateMill {

	protected ArrayList<Coordinates> coordinates;
	private WorkSurface workSurface;
	private DrillPointer drillPointer;
	private CoolingSimulator coolingSimulater;
	private UIController ui;
	private CommandProcessor cp;
	private int counter;
	private int speedTmp;
	private int speed;
	private boolean running;
	AnimationTimer timer;

	/**
	 * Constructor for SimulateMill Class
	 * 
	 * @param coordinates
	 * @param workSurface
	 * @param drillPointer
	 * @param cp
	 * @param ui
	 */

	public SimulateMill(ArrayList<Coordinates> coordinates, WorkSurface workSurface, DrillPointer drillPointer,
			CoolingSimulator coolingSimulater, CommandProcessor cp, UIController ui) {
		this.coordinates = coordinates;
		this.workSurface = workSurface;
		this.drillPointer = drillPointer;
		this.coolingSimulater = coolingSimulater;
		this.cp = cp;
		this.ui = ui;
		this.counter = 0;
		this.speed = 150000000;
		this.speedTmp = 0;
		this.timer = null;
		this.running = true;
		System.out.println("beep");
	}

	/**
	 * Method to start drawing using a animation Timer which is responsible for
	 * drawing the next coordinate after a given amount of time
	 * 
	 * @author Tim, Jonas, Simon
	 */
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
						// End of milling process
						ui.millEnd();
						cp.logAll();
					}
					lastUpdateTime = now;
				}
			}
		};

		timer.start();

	}

	/**
	 * Method to draw the coordinates. Also determines end off single commands to
	 * add them to done commands, update commands toDo and log commands which are
	 * done.
	 * 
	 * When drill is just moving water does not to be dispensed even when cooling is turned on.
	 * 
	 * @author Tim, Jonas, Simon
	 */
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
//				if (coordinates.get(counter).isCooling()) {
//					coolingSimulater.drawPoint((coordinates.get(counter).getX() + 420),
//							(-coordinates.get(counter).getY() + 315));
//				}
			}
		}

		// checking if the rotation direction has changed
		ui.setRotDir(coordinates.get(counter).getRotation());
		ui.setCoolStat(coordinates.get(counter).isCooling());
		ui.setPosition(coordinates.get(counter).getX() + " / " + coordinates.get(counter).getY());
		counter++;
//		System.out.println("( "+ ((coordinates.get(counter).getX() + 420)) + " / " + (( - coordinates.get(counter).getY() + 315)) + " )");

	}

	/**
	 * Method to stop drawing by stopping the animation Timer
	 * 
	 * @author Tim, Jonas, Simon
	 */
	public void pause() {
		timer.stop();
	}

	/**
	 * Method to start drawing by starting the animation Timer
	 * 
	 * @author Tim, Jonas, Simon
	 */
	public void unpause() {
		timer.start();
	}

	/**
	 * Method to reset worksurface and commands to start over. Also logs all
	 * commands and termination.
	 * 
	 * @author Tim, Jonas, Simon
	 */
	public void reset() {
		timer.stop();
		coordinates.clear();
		coordinates.add(new Coordinates(0, 0, false, false, "right"));
		counter = 0;
		workSurface.clearAll();
		drillPointer.clearAll();
		coolingSimulater.clearAll();
		drillPointer = null;
		cp.logMessage("Reset", "Worksurface sucessfully reset", " by User");
		cp.logAll();
		cp.resetCpCounter();
	}

	/**
	 * Method to determine whether drawing is running.
	 * 
	 * @author Tim, Jonas, Simon
	 */

	public boolean isRunning() {
		return running;
	}

	/**
	 * Method to set running attribute.
	 * 
	 * @author Tim, Jonas, Simon
	 */
	public void setRunning(boolean running) {
		this.running = running;
	}

}
