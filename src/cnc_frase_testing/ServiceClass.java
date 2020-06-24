package cnc_frase_testing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
	public static void writeLog(JSONObject befehl, long startzeit){
		counter += 10;
		long endzeit = System.currentTimeMillis() - startzeit;
		JSONObject logBefehl = new JSONObject();
		logBefehl.put("number", "N"+counter);
		String command = new String (befehl.getString("code") + "  -  Runtime(in ms): " + endzeit);
		logBefehl.put("command", command);
		logArray.put(logBefehl);
	}
	
	public static void logToFile(){
		try {
	         FileWriter file = new FileWriter("data//CNC_Fraese_Log.txt");
	         file.write(logArray.toString());
	         file.close();
	      } catch (IOException e) {
	         // TODO Auto-generated catch block
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
}
