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

	// Zuständig Befehlsabarbeitung
	public void fraesen(JSONObject befehlsJson) {
		JSONArray commands = new JSONArray();
		commands = befehlsJson.getJSONArray("commands");

		// Prüfen, ob nummerriert oder nicht
		if (commands.getJSONObject(0).getString("number") != null) {
			//Wenn ja -> sortieren
			commands = ServiceClass.arraySort(commands);
		};
		//für Logzeiten
		ServiceClass.setStartzeit();
		
		//Abarbeitung der einzelnen Befehle + log
		commands.forEach(item -> {
			JSONObject command = (JSONObject) item;
			boolean success = BefehlSwitch(command);
			if (success) {
				ServiceClass.writeLog(command);
			}
			
		});
		//Speicherung des Logs im File
		ServiceClass.logToFile();
	}

	private boolean BefehlSwitch(JSONObject command) {
		boolean success = true;
		String commandType = command.getString("code").replaceAll("[0-9]", "");
		String commandNumber = command.getString("code").replaceAll("[A-Z]", "");
		
		String [] hilf = commandNumber.split("");
		if (hilf.length>2) {
			ExceptionHandler.wrongCode(commandType,commandNumber);
			success = false;
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
				ExceptionHandler.wrongCode(commandType,commandNumber);
				success = false;
				break;
			}
		} else if (commandType.equals("G")) {
			JSONObject parameters = new JSONObject();
			parameters = (JSONObject) command.getJSONObject("parameters");
			switch (commandNumber) {
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
				ExceptionHandler.wrongCode(commandType,commandNumber);
				success = false;
				break;
			}
		} else {
			ExceptionHandler.wrongCommand(commandType);
			success = false;
		}
		return success;
	}
}
