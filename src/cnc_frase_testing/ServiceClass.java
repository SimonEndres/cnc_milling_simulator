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


public class ServiceClass {
	private static int counterWorkList = 0;
	public static JSONArray workList = new JSONArray();
	public static JSONArray logArray = new JSONArray();
	public static long startTime;
	private static int logCounter = 0;
	
	public static void setStartzeit() {
		startTime = System.currentTimeMillis();
	}
	
	public static void writeWorkList(JSONObject commandJSON) {
		counterWorkList += 10;
		String command;
		String code = commandJSON.getString("code");
		JSONObject logCommand = new JSONObject();
		logCommand.put("number", "N" + counterWorkList);
		if (code.equals("G01") || code.equals("G02")) {
			System.out.println("G");
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
		logCommand.put("command", command);
		workList.put(logCommand);
	}
	
	//BSPW für ERRORs, Abbruch
	public static void writeWorkList(String type,String source ,String information) {
		JSONObject logCommand = new JSONObject();
		String string = new String(source + ": " + information);
		logCommand.put(type, string);
		workList.put(logCommand);
	}
	
	public static String getWorkListCommand() {
		return (String) workList.get(logCounter);
	}
	
	public static void putLogArray() {
		long actZeit = System.currentTimeMillis() - startTime;
		JSONObject logElement = (JSONObject) workList.get(logCounter);
		String command = logElement.getString("command") + actZeit;
		JSONObject newLogElement = new JSONObject();
		newLogElement.put("command",command);
		logArray.put(newLogElement);
		logCounter++;
	}

	public static void logToFile() {
		try {
			FileWriter file = new FileWriter("data//CNC_Fraese_Log.txt");
			file.write(logArray.toString());
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void updateUiLog(JSONObject commandJSON,UI ui) {
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

	public static JSONArray arraySort(JSONArray jsonArr) {
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
	
	public static JSONObject loadJson(File file) {
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
