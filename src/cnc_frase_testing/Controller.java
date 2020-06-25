package cnc_frase_testing;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class Controller {
	
	private UI drawingBoard;
	private Bohrer bohrer;
	protected ArrayList<Coordinates> coordinates;
	
	
	Controller(UI drawingboard) {
		this.coordinates = new ArrayList<Coordinates>();
		drawingBoard = drawingboard;
		bohrer = new Bohrer(this.coordinates);
	}

	// Zust�ndig Befehlsabarbeitung
	public void fraesen(JSONObject befehlsJson) {
		JSONArray commands = new JSONArray();
		commands = befehlsJson.getJSONArray("commands");

		// Pr�fen, ob nummerriert oder nicht
		if (commands.getJSONObject(0).getString("number") != null) {
			//Wenn ja -> sortieren
			commands = ServiceClass.arraySort(commands);
		};
		//f�r Logzeiten
		ServiceClass.setStartzeit();
		
		//Abarbeitung der einzelnen Befehle + log
		commands.forEach(item -> {
			JSONObject befehl = (JSONObject) item;
			boolean success = BefehlSwitch(befehl);
			if (success) {
				ServiceClass.writeLog(befehl);
			}
			
		});
		ServiceClass.logToFile();
	}

	private boolean BefehlSwitch(JSONObject befehl) {
		String befehlsart = befehl.getString("code").replaceAll("[0-9]", "");
		String befehlsNummer = befehl.getString("code").replaceAll("[A-Z]", "");
		boolean success = true;
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
				bohrer.setK�hlmittel(true);
				break;
			case "09":
				bohrer.setK�hlmittel(false);
				break;
			case "13":
				bohrer.setSpindelStatus(true);
				bohrer.setDrehrichtung("rechts");
				bohrer.setK�hlmittel(true);
				break;
			case "14":
				bohrer.setSpindelStatus(true);
				bohrer.setDrehrichtung("links");
				bohrer.setK�hlmittel(true);
				break;
			case "":
				ExceptionHandler.wrongCode(befehlsart,befehlsNummer);
				success = false;
				break;
			}
		} else if (befehlsart.equals("G")) {
			// Befehlsart ist G -> Aufruf der Bohrerfunktion entsprechend der Nummer
			JSONObject parameters = new JSONObject();
			parameters = (JSONObject) befehl.getJSONObject("parameters");
			switch (befehlsNummer) {
			case "00":
				bohrer.drawLine(parameters.getInt("x"), parameters.getInt("y"), false);
				break;
			case "01":
				bohrer.drawLine(parameters.getInt("x"), parameters.getInt("y"), true);
				break;
			case "02":
				bohrer.drawCircle(parameters.getInt("x"), parameters.getInt("y"), parameters.getInt("i"), parameters.getInt("j"), false);
				break;
			case "03":
				bohrer.drawCircle(parameters.getInt("x"), parameters.getInt("y"), parameters.getInt("i"), parameters.getInt("j"), true);
				break;
			case "28":
				bohrer.drawLine(0, 0, false);
				break;
			case "":
				ExceptionHandler.wrongCode(befehlsart,befehlsNummer);
				success = false;
				break;
			}
		} else {
			ExceptionHandler.wrongCommand(befehlsart);
			success = false;
		}
		return success;
	}
}
