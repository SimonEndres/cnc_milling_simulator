package cnc_frase_testing;
import org.json.JSONObject;

import javafx.application.Application;
import javafx.stage.Stage;

public class CNC_Fraese {
	private DrawingBoard drawingBoard;
	private Bohrer bohrer;

	CNC_Fraese(Stage primaryStage){
		drawingBoard = new DrawingBoard(primaryStage);
		bohrer = new Bohrer(drawingBoard);
		this.fraesen();
	}
	public void fraesen () {
		bohrer.drawLine(1,200,1,200);
	}
}
