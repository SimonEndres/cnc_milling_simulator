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
	private CoolingSimulater coolingSimulater;
	private UIController ui;
	private CommandProcessor cp;
	private int counter = 0;
	private int speedTmp;
	private int speed = 150000000;
	private boolean running;
	AnimationTimer timer = null;
	
	/**
	 * Constructor for SimulateMill Class
	 * 
	 * @param coordinates
	 * @param workSurface
	 * @param drillPointer
	 * @param cp
	 * @param ui
	 */

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
	
	/**
	 * Method to start drawing using a animation Timer
	 * which is responsible for drawing the next coordinate after a given amount of time
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
						// Speicherung des Logs im File - muss ans Ende der Simulate Mill
						cp.logAll();
					}
					lastUpdateTime = now;
				}
			}
		};

		timer.start();

	}
	
	/**
	 * Method to draw the coordinates. Also determines end off single commands to add them
	 * to done commands, update commands toDo and log commands which are done.
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
				if (coordinates.get(counter).isCooling()) {
					speed = 100000000;
					workSurface.drawPoint((coordinates.get(counter).getX() + 420),
							(-coordinates.get(counter).getY() + 315));
					drillPointer.drawPoint((coordinates.get(counter).getX() + 420),
							(-coordinates.get(counter).getY() + 315));
					coolingSimulater.drawPoint((coordinates.get(counter).getX() + 420),
							(-coordinates.get(counter).getY() + 315));
				} else {
					speed = 150000000;
					workSurface.drawPoint((coordinates.get(counter).getX() + 420),
							(-coordinates.get(counter).getY() + 315));
					drillPointer.drawPoint((coordinates.get(counter).getX() + 420),
							(-coordinates.get(counter).getY() + 315));

				}
			} else {
				if (speedTmp != ui.getSpeed()) {
					speedTmp = ui.getSpeed();
					switch (speedTmp) {
					case 4:
						speed = 80000000;
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
				if (coordinates.get(counter).isCooling()) {
					coolingSimulater.drawPoint((coordinates.get(counter).getX() + 420),
							(-coordinates.get(counter).getY() + 315));
				}
			}
		}

		counter++;
//		System.out.println("( "+ ((coordinates.get(counter).getX() + 420)) + " / " + (( - coordinates.get(counter).getY() + 315)) + " )");

	}

	/**
	 * Method to stop drawing by stopping the animation Timer
	 * @author Tim, Jonas, Simon
	 */
	public void pause() {
		timer.stop();
	}
	
	/**
	 * Method to start drawing by starting the animation Timer
	 * @author Tim, Jonas, Simon
	 */
	public void unpause() {
		timer.start();
	}

	/**
	 * Method to reset worksurface and commands to start over. Also logs all commands and termination.
	 * @author Tim, Jonas, Simon
	 */
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

	/**
	 * Method to determine whether drawing is running.
	 * @author Tim, Jonas, Simon
	 */

	public boolean isRunning() {
		return running;
	}
	
	/**
	 * Method to set running attribute.
	 * @author Tim, Jonas, Simon
	 */
	public void setRunning(boolean running) {
		this.running = running;
	}

}
