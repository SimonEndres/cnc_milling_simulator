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
	private static int counter = 0;
	public static JSONArray logArray = new JSONArray();
	private static long startTime;
	
	public static void setStartzeit() {
		startTime = System.currentTimeMillis();
	}
	
	public static void writeLog(JSONObject befehl) {
		counter += 10;
		long actZeit = System.currentTimeMillis() - startTime;
		JSONObject logCommand = new JSONObject();
		logCommand.put("number", "N" + counter);
		String command = new String(befehl.getString("code") + "  |  Runtime(in ms): " + actZeit);
		logCommand.put("command", command);
		logArray.put(logCommand);
	}
	
	public static void writeLog(String type,String source ,String information) {
		long actZeit = System.currentTimeMillis() - startTime;
		JSONObject logCommand = new JSONObject();
		String string = new String(source + ": " + information + "  |  Runtime(in ms): " + actZeit);
		logCommand.put(type, string);
		logArray.put(logCommand);
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
