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


public class Controller {
	
	private UI drawingBoard;
	private Bohrer bohrer;
	protected ArrayList<Coordinates> coordinates;
	
	
	Controller(UI drawingboard) {
		this.coordinates = new ArrayList<Coordinates>();
		coordinates.add(new Coordinates(2, 4, true));
		drawingBoard = drawingboard;
		bohrer = new Bohrer(this.coordinates);
	}

	// Zuständig Befehlsabarbeitung
	public void fraesen(JSONObject befehlsJson) {
		JSONArray commands = new JSONArray();
		commands = befehlsJson.getJSONArray("commands");

		// Prüfen, ob nummerriert oder nicht
		if (commands.getJSONObject(0).getString("number") != null) {
			//Wenn ja -> sortieren
			commands = ServiceClass.arraySort(commands);
		};

		commands.forEach(item -> {
			JSONObject befehl = (JSONObject) item;
			long startzeit = System.currentTimeMillis();
			BefehlSwitch(befehl);
			ServiceClass.writeLog(befehl,startzeit);
		});
		ServiceClass.logToFile();
	}

	private void BefehlSwitch(JSONObject befehl) {
		String[] hilf = befehl.getString("code").split("");
		String befehlsart = hilf[0];
		String befehlsNummer = hilf[1] + hilf[2];
		if (befehlsart.equals("M")) {
			// Befehlsart ist M -> Aufruf der Bohrerfunktion entsprechend der Nummer
			switch (befehlsNummer) {
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
			JSONObject parameters = new JSONObject();
			parameters = (JSONObject) befehl.getJSONObject("parameters");
			switch (befehlsNummer) {
			case "00":
				bohrer.drawLine(parameters.getInt("x"), parameters.getInt("y"), false);
				System.out.println("G00");
				break;
			case "01":
				bohrer.drawLine(parameters.getInt("x"), parameters.getInt("y"), true);
				System.out.println("G01");
				break;
			case "02":
				bohrer.drawCircle(parameters.getInt("x"), parameters.getInt("y"), parameters.getInt("i"), parameters.getInt("j"), false);
				System.out.println("G02");
				break;
			case "03":
				bohrer.drawCircle(parameters.getInt("x"), parameters.getInt("y"), parameters.getInt("i"), parameters.getInt("j"), true);
				System.out.println("G03");
				break;
			case "28":
				bohrer.drawLine(0, 0, false);
				System.out.println("G28");
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
