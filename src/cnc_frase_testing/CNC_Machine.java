package cnc_frase_testing;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Exceptions.WrongCommandException;
import UI.UIController;

public class CNC_Machine {

	private UIController ui;
	private Drill drill;
	private CommandProcessor cp;
	private ArrayList<Coordinates> coordinates;

	public CNC_Machine(UIController uiController, CommandProcessor cp) {
		this.coordinates = new ArrayList<Coordinates>();
		this.ui = uiController;
		drill = new Drill(this.coordinates);
		this.cp = cp;

	}

	// Zuständig Befehlsabarbeitung
	public void fraesen(JSONObject befehlsJson) {
		JSONArray commands = new JSONArray();
		try {
			commands = befehlsJson.getJSONArray("commands");
		} catch (JSONException e) {
			ExceptionHandler.logError(cp, "Corrupt JSONFile, can't load commands", "wait for new Entry");
			return;
		}
		// Prüfen, ob nummerriert oder nicht
		if (commands.getJSONObject(0).getString("number") != null) {
			// Wenn ja -> sortieren
			commands = cp.arraySort(commands);
		}
		;

		// Abarbeitung der einzelnen Befehle + log
		commands.forEach(item -> {
			JSONObject commandJSON = (JSONObject) item;
			boolean success = BefehlSwitch(commandJSON);
			if (success) {
				cp.writeWorkList(commandJSON);
				cp.updateUiLog(commandJSON, ui);
			}
		});
	}

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
			ExceptionHandler.logError(cp, e.getMessage(), "Skip command");
		}
		return success;
	}

	public ArrayList<Coordinates> getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(ArrayList<Coordinates> coordinates) {
		this.coordinates = coordinates;
	}

}
