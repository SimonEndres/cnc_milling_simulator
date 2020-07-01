package UI;

import java.util.ArrayList;

import cnc_frase_testing.CNC_Machine;
import cnc_frase_testing.CommandProcessor;
import cnc_frase_testing.SimulateMill;
import cnc_frase_testing.WorkSurface;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

public class UIController {
	
	private WorkSurface workSurface;
	private CommandProcessor cp;
	private CNC_Machine cnc_machine;
	private ArrayList<String> uiLog;
	private int logCount = 0;
	
	public UIController() {
		this.cp = new CommandProcessor();
		//this.cnc_machine = new CNC_Machine(this, cp);
		final FileChooser fileChooser = new FileChooser();
		this.uiLog = new ArrayList<String>();
		this.workSurface = Main.getWorkSurface();
	}
    @FXML
    void onChoosedCode(ActionEvent event) {
    	
    }
    
    @FXML
    void onPressStart(ActionEvent event) {
    	UIController that = this;
    	
    	Platform.runLater(new Runnable() {
			public void run() {
				//SimulateMill myThread = new SimulateMill(cnc_machine.getCoordinates(), workSurface, cp, that);
				//myThread.startDrawing();
			}
		});
    }

    @FXML
    void onPressSubmit(ActionEvent event) {

    }

    @FXML
    void onPressTerminate(ActionEvent event) {

    }

    @FXML
    void onPressUploadSettings(ActionEvent event) {
    	System.out.println(workSurface);
    }

}