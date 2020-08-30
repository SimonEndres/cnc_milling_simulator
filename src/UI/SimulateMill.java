package UI;

import java.io.File;
import java.util.ArrayList;

import cnc_frase_testing.CommandProcessor;
import cnc_frase_testing.Coordinates;
import cnc_frase_testing.ExceptionHandler;
import javafx.animation.AnimationTimer;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

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
	private AnimationTimer timer;

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
		this.speed = 100000000;
		this.speedTmp = 0;
		this.timer = null;
		this.running = true;
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
						try {
							cp.logAll();
						}
						catch(Exception e){
							ExceptionHandler.handleErrorByMessage(ui, cp, "Could not write log",  "retry");
						}
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
	 * Worksurface on the UI is only 840x315px. Due to that the coordinates are calculated with factor 0.6 for the UI.
	 * 
	 * @author Tim, Jonas, Simon
	 */
	private void draw() {
		if (coordinates.get(counter).isEnd()) {
			ui.setCommandsDone();
			ui.updateCommandsToDo();
			cp.logCommandsDone();
		} else {
			
			int x = (int) (coordinates.get(counter).getX() * 0.6 + 424);
			int y = (int) (-coordinates.get(counter).getY() * 0.6 + 322);
			
			if (coordinates.get(counter).isMill()) {
				if (speedTmp!= ui.getMillSpeed()) {
					speedTmp = ui.getMillSpeed();
				}
				switch (speedTmp) {
				case 1:
					speed = 100000000;
					break;
				case 2:
					speed = 85000000;
					break;
				case 3:
					speed = 70000000;
					break;
				}
				
				if (coordinates.get(counter).isCooling()) {
					speed-= 10000000;
					ui.setCurrSpeed(speedTmp + 1);
					workSurface.drawPoint(x,y);
					drillPointer.drawPoint(x,y,true);
					coolingSimulater.drawPoint(x,y);
				} else {
					coolingSimulater.clearAll();
					workSurface.drawPoint(x,y);
					drillPointer.drawPoint(x,y,true);
					ui.setCurrSpeed(speedTmp);
				}
			} else {
				if (speedTmp != ui.getDriveSpeed()) {
					speedTmp = ui.getDriveSpeed();
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
				//Speed on UI should be updated for all G-Commands (not for StartPoint and M Commands - value null)
				if (coordinates.get(counter).getRotation() != null) {
					ui.setCurrSpeed(speedTmp);
				}
				drillPointer.drawPoint(x,y,true);
			}
		}

		ui.setRotDir(coordinates.get(counter).getRotation());
		ui.setCoolStat(coordinates.get(counter).isCooling());
		ui.setPosition(coordinates.get(counter).getX() + " / " + coordinates.get(counter).getY());
		counter++;
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
		try {
			cp.logAll();
		}
		catch(Exception e){
			ExceptionHandler.handleErrorByMessage(ui, cp, "Could not write log",  "retry");
		}
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
	
	/**
	 * Method to get current coordinate of drill
	 * @author Jonas
	 */
	public Coordinates getCurrentCoordinate() {
		return coordinates.get(counter);
	}

}
