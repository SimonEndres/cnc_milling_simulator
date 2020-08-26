package cnc_frase_testing;

import java.io.File;

import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import UI.UIController;

/**
 * The class is used to provide several functions:
 * Sort commandJson, store commands in workList and uiLog and also makes them available for the logger
 * to write them into the logFile
 * 
 * @author Tim, Jonas
 *
 */
public class CommandProcessor {
	
	/**
	 * JSONArray contains all commands with parameters, used for saving calculated commands
	 */
	public JSONArray workList;
	/**
	 * JSONArray contains all commands with parameters and runtime, that were executed successfully and will later be logged by the logger
	 */
	public JSONArray logArray;
	private int counterWorkList;
	public long startTime;
	private int logCounter;
	private Logger logger;
	
	/**
	 * Constructor to initialize Arrays
	 * 
	 * @author Tim
	 */
	public CommandProcessor () {
		this.workList = new JSONArray();
		this.logArray = new JSONArray();
		this.counterWorkList = 0;
		this.logCounter = 0;
		this.logger = Logger.getInstance();
	}
	/**
	 * Sets start time for runtime calculation
	 * @author Jonas,Tim
	 */
	public void setStartTime() {
		this.startTime = System.currentTimeMillis();
	}
	/**
	 * Adds calculated commands with parameters to workList and uiLog
	 * 
	 * @author Jonas, Tim
	 * @param commandJSON	Contains entered commands for milling.
	 * @param ui			Object of UIController for adding commands to uiLog.
	 */
	public void writeListAndLog(JSONObject commandJSON, UIController ui) {
		this.counterWorkList += 10;
		String command;
		String code = commandJSON.getString("code");
		JSONObject logCommand = new JSONObject();
		if (code.equals("G01") || code.equals("G02")) {
			System.out.println("G");
			JSONObject parameters = new JSONObject();
			parameters = (JSONObject) commandJSON.getJSONObject("parameters");
			if (code.equals("G01")) {
				command = new String(this.counterWorkList + ": " + code + " X:" + parameters.getInt("x") + " Y:" + parameters.getInt("y") +  "  |  Runtime(in ms): ");
			} else {
				command = new String(this.counterWorkList + ": " + code + " X:" + parameters.getInt("x") + " Y:" + parameters.getInt("y") + " I:" + parameters.getInt("i") + " J:" + parameters.getInt("j") + "  |  Runtime(in ms): ");
			}
		} else {
			command = new String(this.counterWorkList + ": " + code + "  |  Runtime(in ms): ");
		}
		ui.setCommandsToDo(command);
		logCommand.put("command", command);
		workList.put(logCommand);
	}
	
	/**
	 * Adds a Message to logArray with runtime in ms, which is later logged by the logger
	 * 
	 * @author Jonas, Tim
	 * @param type		Message type (for example Error)
	 * @param reason	Reason for the message
	 * @param handling	Handling of the message
	 */
	public void logMessage(String type,String reason ,String handling) {
		JSONObject logCommand = new JSONObject();
		String string = new String(reason + ": " + handling+ "  |  Runtime(in ms): ");
		logCommand.put(type, string);
		long actZeit = System.currentTimeMillis() - startTime;
		String logMsg = logCommand.getString(type) + actZeit;
		JSONObject newLogElement = new JSONObject();
		newLogElement.put(type,logMsg);
		logArray.put(newLogElement);
	}
	
	/**
	 * Adds all successfully executed commands of workList with current runtime to logArray
	 * 
	 * @author Jonas, Tim
	 */
	public void logCommandsDone() {
		long actZeit = System.currentTimeMillis() - startTime;
		JSONObject logElement = (JSONObject) workList.get(logCounter);
		String command = logElement.getString("command") + actZeit;
		JSONObject newLogElement = new JSONObject();
		newLogElement.put("command",command);
		logArray.put(newLogElement);
		logCounter++;
	}

	/**
	 * Triggers logger to write all commands of logArray into the logFile
	 * 
	 * @author Jonas, Tim
	 */
	public void logAll() {
		logger.logToFile(logArray);
	}

	/**
	 * Method to sort the commands of commandJson by number
	 * 
	 * @author Tim
	 * @param unsJsonArray			unsorted Json Array
	 * @return sortedJsonArray	- Returns a sortet Json Array
	 */
	public JSONArray arraySort(JSONArray unsJsonArray) {
		JSONArray sortedJsonArray = new JSONArray();

		List<JSONObject> jsonValues = new ArrayList<JSONObject>();
		for (int i = 0; i < unsJsonArray.length(); i++) {
			jsonValues.add(unsJsonArray.getJSONObject(i));
		}
		Collections.sort(jsonValues, new Comparator<JSONObject>() {
			private static final String Sort_Key = "number";

			@Override
			public int compare(JSONObject a, JSONObject b) {
				String valA = new String();
				String valB = new String();

				try {
					valA = (String) a.get(Sort_Key);
					valB = (String) b.get(Sort_Key);
				} catch (JSONException e) {
					e.printStackTrace();
				}

				return valA.compareTo(valB);
			}
		});
		for (int i = 0; i < unsJsonArray.length(); i++) {
			sortedJsonArray.put(jsonValues.get(i));
		}
		return sortedJsonArray;
	}
	
	/**
	 * Load commands for execution from file into JsonObject
	 * 
	 * @author Tim
	 * @param file			File containing commands to execute
	 * @return commandJson	- Contains entered commands for milling.
	 */
	public JSONObject loadJson(File file) {
		JSONObject commandJson = null;
		try {
			String jsonData = new String(Files.readAllBytes(Paths.get(file.toURI())), "UTF-8");
			commandJson = new JSONObject(jsonData);
		} catch (Exception e) {
			System.out.println(e);
		}
		return commandJson;
	}
	
	public void resetCpCounter() {
		counterWorkList = 0;
		logCounter = 0;
	}
}
