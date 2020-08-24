package cnc_frase_testing;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Exceptions.OutOfWorksurfaceException;
import Exceptions.WrongCommandException;
import UI.UIController;

/**
 * 
 * The class serves as CPU to coordinate all tasks of the CNC milling machine.
 * It reads, sorts and processes the commands (checking, calculate, logging).
 * 
 * @author Tim
 *
 */
public class CNC_Machine {

	/**
	 * Arraylist containing x and y values and additional information regarding each
	 * point, such as milling status, cooling status, etc. It serves to store the
	 * calculated data for later execution.
	 */
	private ArrayList<Coordinates> coordinates;

	private UIController ui;
	private Drill drill;
	private CommandProcessor cp;

	/**
	 * Constructor of class CNC_Machine
	 * 
	 * @author Tim
	 * @param uiController Object of UIController
	 * @param cp           Object of CommandProcessor
	 */
	public CNC_Machine(UIController uiController, CommandProcessor cp) {
		this.coordinates = new ArrayList<Coordinates>();
		this.ui = uiController;
		drill = new Drill(this.coordinates);
		this.cp = cp;

	}

	/**
	 * Function to coordinate all. Reads commands, checks for numbering and sorts
	 * them. Then processes individual commands and writes them to the worklist and
	 * UiLog.
	 * 
	 * @author Tim
	 * @param commandJson Contains entered commands for milling.
	 */
	public void machineControl(JSONObject commandJson) {
		JSONArray commands = new JSONArray();
		try {
			commands = commandJson.getJSONArray("commands");
		} catch (JSONException e) {
			ExceptionHandler.logError(cp, "Corrupt JSONFile, can't load commands", "wait for new Entry");
			return;
		}
		// check for numbering
		if (commands.getJSONObject(0).getString("number") != null) {
			// sort array
			commands = cp.arraySort(commands);
		}

		// Processesing individual commands, writing them to the worklist and UiLog
		commands.forEach(item -> {
			JSONObject commandJSON = (JSONObject) item;
			boolean success = BefehlSwitch(commandJSON);
			if (success) {
				cp.writeListAndLog(commandJSON, ui);
			}
		});
	}

	/**
	 * Switch for the different commands and corresponding processing
	 * 
	 * @author Tim
	 * @param commandJSON Contains entered commands for execution.
	 * @return success - true -> calculation successful, logging
	 */
	private boolean BefehlSwitch(JSONObject commandJSON) {
		String commandType = commandJSON.getString("code").replaceAll("[0-9]", "");
		String commandNumber = commandJSON.getString("code").replaceAll("[A-Z]", "");
		boolean success = true;
		String[] hilf = commandNumber.split("");
		try {
			if (hilf.length > 2) {
				success = false;
				throw new WrongCommandException("Command doesn't exist");
			}
			if (commandType.equals("M")) {
				switch (commandNumber) {
				case "00":
					// Programmhalt;
					break;
				case "02":
					// ProgrammEnde;
					break;
				case "03":
					drill.setSpindelStatus(true);
					drill.setDrehrichtung("rechts");
					break;
				case "04":
					drill.setSpindelStatus(true);
					drill.setDrehrichtung("links");
					break;
				case "05":
					drill.setSpindelStatus(false);
					break;
				case "08":
					drill.setKühlmittel(true);
					break;
				case "09":
					drill.setKühlmittel(false);
					break;
				case "13":
					drill.setSpindelStatus(true);
					drill.setDrehrichtung("rechts");
					drill.setKühlmittel(true);
					break;
				case "14":
					drill.setSpindelStatus(true);
					drill.setDrehrichtung("links");
					drill.setKühlmittel(true);
					break;
				case "":
					success = false;
					throw new WrongCommandException("Command doesn't exist");
				}
				// Write M command to the command array (necessary for correct logging)
				drill.writeM();
			} else if (commandType.equals("G")) {
				JSONObject parameters = new JSONObject();
				parameters = (JSONObject) commandJSON.getJSONObject("parameters");
					switch (commandNumber) {
					case "00":
						drill.drawLine(parameters.getInt("x"), parameters.getInt("y"), false);
						break;
					case "01":
						drill.drawLine(parameters.getInt("x"), parameters.getInt("y"), true);
						break;
					case "02":
						drill.drawCircle(parameters.getInt("x"), parameters.getInt("y"), parameters.getInt("i"),
								parameters.getInt("j"), false);
						break;
					case "03":
						drill.drawCircle(parameters.getInt("x"), parameters.getInt("y"), parameters.getInt("i"),
								parameters.getInt("j"), true);
						break;
					case "28":
						drill.drawLine(0, 0, false);
						break;
					case "":
						success = false;
						throw new WrongCommandException("Command doesn't exist");
					}
			
			} else {
				success = false;
				throw new WrongCommandException("Command doesn't exist");
			}
		} catch (WrongCommandException e) {
			ExceptionHandler.logErrorTerminate(ui, cp, e.getMessage(), "Terminate");
		} catch (JSONException e) {
			ExceptionHandler.logErrorTerminate(ui, cp, e.getMessage(), "Terminate");
		} catch (OutOfWorksurfaceException e) {
			ExceptionHandler.logErrorTerminate(ui, cp, e.getMessage(), "Terminate");
		}
		return success;
	}

	/**
	 * Getter for Arraylist coordinates
	 * 
	 * @author Tim
	 * @return coordinates - ArrayList of calculated coordinates
	 */
	public ArrayList<Coordinates> getCoordinates() {
		return coordinates;
	}

}
