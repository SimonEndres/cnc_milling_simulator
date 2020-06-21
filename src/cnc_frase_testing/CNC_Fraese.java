package cnc_frase_testing;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.application.Application;
import javafx.stage.Stage;

public class CNC_Fraese {
	private DrawingBoard drawingBoard;
	private Bohrer bohrer;

	CNC_Fraese(Stage primaryStage){
		drawingBoard = new DrawingBoard(primaryStage,this);
		bohrer = new Bohrer(drawingBoard);
		bohrer.drawLine(100, 100, true);
		bohrer.drawCircle(200, 200, 50, 50, false);
	}
	
	public void fraesen (JSONObject befehlsJson) {
		JSONArray befehlsArray = new JSONArray();
		befehlsArray = (JSONArray) befehlsJson.getJSONArray("Befehle");
		//Auslesen der Befehle aus der JSON
		for(int count = 0; count < befehlsArray.length(); count++) {
			JSONObject befehl = befehlsArray.getJSONObject(count);
			String befehlsart = befehl.getString("Befehlsart");
			BefehlSwitch(befehl,befehlsart);
//			try {
//				File file = new File ("C:\\Program Files\\CNC_Fraese_Log.txt");
//				if (!file.exists()) {
//					file.createNewFile();
//				}
//				BufferedWriter logOutput = new BufferedWriter(new FileWriter(file));
//				logOutput.write(count + ": " + befehlsart);
//				logOutput.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
		}
	}
	
	private void BefehlSwitch (JSONObject befehl, String befehlsart) {
		if (befehlsart.equals("M")){
			//Befehlsart ist M -> Aufruf der Bohrerfunktion entsprechend der Nummer
			switch (befehl.getString("Nummer")) {
				case "00":
					//Programmhalt;
					break;
				case "02":
					//ProgrammEnde;
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
					//Problem: 08, 09 als Int out of Range... deshalb String
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
					//bohrer.M14;
					break;
				case "":
					System.out.println("Befehlsnummer existiert nicht für M");
					break;
			}
		} else if (befehlsart.equals("G")){
			//Befehlsart ist G -> Aufruf der Bohrerfunktion entsprechend der Nummer
			switch (befehl.getString("Nummer")) {
			case "00":
				bohrer.drawLine(befehl.getInt("XKoordinate"), befehl.getInt("YKoordinate"), false);
				break;
			case "01":
				bohrer.drawLine(befehl.getInt("XKoordinate"),befehl.getInt("YKoordinate"), true);
				System.out.println("G01");
				break;
			case "02":
				bohrer.drawCircle(befehl.getInt("XKoordinate"),befehl.getInt("YKoordinate"),befehl.getInt("I"),befehl.getInt("J"), true);//boolean muss noch aufgenommen werden
				break;
			case "03":
				//bohrer.drawCircleLinks(befehl.getInt("XKoordinate"),befehl.getInt("YKoordinate"),befehl.getInt("I"),befehl.getInt("J"));					break;
			case "28":
				//bohrer.G28(befehl.getInt("XKoordinate"),befehl.getInt("YKoordinate"));
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
