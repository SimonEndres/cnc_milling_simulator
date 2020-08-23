package cnc_frase_testing;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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


public class CommandProcessor {
	private int counterWorkList = 0;
	public JSONArray workList = new JSONArray();
	public JSONArray logArray = new JSONArray();
	public long startTime;
	private int logCounter = 0;
	private Logger logger= Logger.getInstance();
	
	public void setStartzeit() {
		this.startTime = System.currentTimeMillis();
	}
	
	public void writeWorkList(JSONObject commandJSON) {
		this.counterWorkList += 10;
		String command;
		String code = commandJSON.getString("code");
		JSONObject logCommand = new JSONObject();
		logCommand.put("number", "N" + this.counterWorkList);
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
		logCommand.put("command", command);
		workList.put(logCommand);
	}
	
	//BSPW für ERRORs, Abbruch
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
	
	//Wird benötigt, um aktuelle Zeiten zu schreiben
	public void logCommandsDone() {
		long actZeit = System.currentTimeMillis() - startTime;
		JSONObject logElement = (JSONObject) workList.get(logCounter);
		String command = logElement.getString("command") + actZeit;
		JSONObject newLogElement = new JSONObject();
		newLogElement.put("command",command);
		logArray.put(newLogElement);
		logCounter++;
	}

	public void logAll() {
		logger.logToFile(logArray);
	}
	
	public void updateUiLog(JSONObject commandJSON,UIController ui) {
		String command;
		String code = commandJSON.getString("code");
		if (code.equals("G01") || code.equals("G02")) {
			JSONObject parameters = new JSONObject();
			parameters = (JSONObject) commandJSON.getJSONObject("parameters");
			if (code.equals("G01")) {
				command = new String(counterWorkList + ": " + code + " X:" + parameters.getInt("x") + " Y:" + parameters.getInt("y") +  "  |  Runtime(in ms): ");
			} else {
				command = new String(counterWorkList + ": " + code + " X:" + parameters.getInt("x") + " Y:" + parameters.getInt("y") + " I:" + parameters.getInt("i") + " J:" + parameters.getInt("j") + "  |  Runtime(in ms): ");
			}
		} else {
			command = new String(counterWorkList + ": " + code + "  |  Runtime(in ms): ");
		}
		ui.setCommandsToDo(command);
	}

	public JSONArray arraySort(JSONArray jsonArr) {
		JSONArray sortedJsonArray = new JSONArray();

		List<JSONObject> jsonValues = new ArrayList<JSONObject>();
		for (int i = 0; i < jsonArr.length(); i++) {
			jsonValues.add(jsonArr.getJSONObject(i));
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

		for (int i = 0; i < jsonArr.length(); i++) {
			sortedJsonArray.put(jsonValues.get(i));
		}
		return sortedJsonArray;
	}
	
	public JSONObject loadJson(File file) {
		JSONObject json = null;
		try {
			String jsonData = new String(Files.readAllBytes(Paths.get(file.toURI())), "UTF-8");
			json = new JSONObject(jsonData);
		} catch (Exception e) {
			System.out.println(e);
		}
		return json;
	}
}
