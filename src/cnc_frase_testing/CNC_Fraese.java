package cnc_frase_testing;
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
		bohrer.drawCircle(200, 200, 50, 50);
	}
	public void fraesen (JSONObject befehlsJson) {
		JSONArray befehlsArray = new JSONArray();
		befehlsArray = (JSONArray) befehlsJson.getJSONArray("Befehle");
		//Auslesen der Befehle aus der JSON
		for(int count = 0; count < befehlsArray.length(); count++) {
			JSONObject befehl = befehlsArray.getJSONObject(count);
			String befehlsart = befehl.getString("Befehlsart");
			if (befehlsart.equals("M")){
				//Befehlsart ist M -> Aufruf der Bohrerfunktion entsprechend der Nummer
				switch (befehl.getString("Nummer")) {
					case "00":
						//bohrer.M00;
						//Für Log: Finish:
						//drawingBoard.Log(count,befehl.getString("Befehlsart"),befehl.getString("Nummer"));
						break;
					case "01":
						//bohrer.M01;
						break;
					case "02":
						//bohrer.M02;
						break;
					case "03":
						//bohrer.M03;
						System.out.println("M03");
						break;
					case "04":
						//bohrer.M04;
						break;
					case "05":
						//bohrer.M05;
						break;
					case "06":
						//bohrer.M06;
						break;
					case "07":
						//bohrer.M07;
						break;
					case "08":
						//bohrer.M08;
						//Problem: 08, 09 als Int out of Range... deshalb String
						break;
					case "09":
						//bohrer.M09;
						break;
					case "10":
						//bohrer.M10;
						break;
					case "11":
						//bohrer.M11;
						break;
					case "12":
						//bohrer.M12;
						break;
					case "13":
						//bohrer.M13;
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
					//bohrer.G00(befehl.getInt("XKoordinate"),befehl.getInt("YKoordinate"));
					break;
				case "01":
					//bohrer.G01(befehl.getInt("XKoordinate"),befehl.getInt("YKoordinate"));
					System.out.println("G01");
					break;
				case "02":
					//bohrer.G02(befehl.getInt("XKoordinate"),befehl.getInt("YKoordinate"));
					break;
				case "03":
					//bohrer.G03(befehl.getInt("XKoordinate"),befehl.getInt("YKoordinate"));
					break;
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
		};
	}
}
