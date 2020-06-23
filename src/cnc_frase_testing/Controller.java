package cnc_frase_testing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javafx.application.Application;
import javafx.stage.Stage;

public class Controller {
	private DrawingBoard drawingBoard;
	private Bohrer bohrer;
	private BufferedWriter logOutput;

	Controller(DrawingBoard drawingboard) {
		drawingBoard = drawingboard;
		bohrer = new Bohrer(drawingBoard);
		bohrer.drawLine(100, 0, true);
		// bohrer.drawCircle(100, 100, 0, 50, false);
		bohrer.drawLine(0, 100, true);
		// bohrer.drawCircle(0, 0, 0, -50, false);
	}

	// Zuständig Befehlsabarbeitung
	public void fraesen(JSONObject befehlsJson) throws IOException {
		JSONArray befehlsArray = new JSONArray();
		befehlsArray = befehlsJson.getJSONArray("commands");
//		//Auslesen der Befehle aus der JSON
//		for(int count = 0; count < befehlsArray.length(); count++) {
//			JSONObject befehl = befehlsArray.getJSONObject(count);
//			String befehlsart = befehl.getString("Befehlsart");
//			BefehlSwitch(befehl,befehlsart);
//			writeLog(befehl,count,System.currentTimeMillis());
//			System.out.println("( "+ bohrer.positionOLD[0] + " / " + bohrer.positionOLD[1] + " )");
//		}
//		logOutput.close();

		// Prüfen, ob nummerriert oder nicht
		if (befehlsArray.getJSONObject(0).getString("number") != null) {
			//Wenn ja -> sortieren
			befehlsArray = arraySort(befehlsArray);
		};
		befehlsArray.forEach(item -> {
			JSONObject befehl = (JSONObject) item;
			
		});
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

	private void writeLog(JSONObject befehl, int count, long startzeit) throws IOException {
		if (count == 0) {
			File file = new File("data//CNC_Fraese_Log.txt");
			if (!file.exists()) {
				file.createNewFile();
			}
			logOutput = new BufferedWriter(new FileWriter(file));
		}
		long endzeit = System.currentTimeMillis() - startzeit;
		logOutput.write("LOG" + count + ": " + befehl.getString("Befehlsart") + befehl.getString("Nummer")
				+ "  -  Laufzeit(in ms): " + endzeit + "\n");
		System.out.println(endzeit);
	}

	private void BefehlSwitch(JSONObject befehl, String befehlsart) {
		if (befehlsart.equals("M")) {
			// Befehlsart ist M -> Aufruf der Bohrerfunktion entsprechend der Nummer
			switch (befehl.getString("Nummer")) {
			case "00":
				// Programmhalt;
				break;
			case "02":
				// ProgrammEnde;
				break;
			case "03":
				bohrer.setSpindelStatus(true);
				bohrer.setDrehrichtung("rechts");
				break;
			case "04":
				bohrer.setSpindelStatus(true);
				bohrer.setDrehrichtung("links");
				break;
			case "05":
				bohrer.setSpindelStatus(false);
				break;
			case "08":
				bohrer.setKühlmittel(true);
				// Problem: 08, 09 als Int out of Range... deshalb String
				break;
			case "09":
				bohrer.setKühlmittel(false);
				break;
			case "13":
				bohrer.setSpindelStatus(true);
				bohrer.setDrehrichtung("rechts");
				bohrer.setKühlmittel(true);
				break;
			case "14":
				bohrer.setSpindelStatus(true);
				bohrer.setDrehrichtung("links");
				bohrer.setKühlmittel(true);
				break;
			case "":
				System.out.println("Befehlsnummer existiert nicht für M");
				break;
			}
		} else if (befehlsart.equals("G")) {
			// Befehlsart ist G -> Aufruf der Bohrerfunktion entsprechend der Nummer
			switch (befehl.getString("Nummer")) {
			case "00":
				bohrer.drawLine(befehl.getInt("XKoordinate"), befehl.getInt("YKoordinate"), false);
				break;
			case "01":
				bohrer.drawLine(befehl.getInt("XKoordinate"), befehl.getInt("YKoordinate"), true);
				break;
			case "02":
				bohrer.drawCircle(befehl.getInt("XKoordinate"), befehl.getInt("YKoordinate"), befehl.getInt("I"),
						befehl.getInt("J"), false);// boolean muss noch aufgenommen werden
				break;
			case "03":
				bohrer.drawCircle(befehl.getInt("XKoordinate"), befehl.getInt("YKoordinate"), befehl.getInt("I"),
						befehl.getInt("J"), true);
				break;
			case "28":
				bohrer.drawLine(0, 0, false);
				break;
			case "":
				System.out.println("Befehlsnummer existiert nicht für G");
				break;
			}
		} else {
			System.out.println("Der eingegebene Befehl existiert nicht");
		}
	}
}
