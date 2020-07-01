package UI;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import cnc_frase_testing.CNC_Machine;
import cnc_frase_testing.CommandProcessor;
import cnc_frase_testing.SimulateMill;
import cnc_frase_testing.WorkSurface;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class UIController {

	private WorkSurface workSurface;
	private CommandProcessor cp;
	private CNC_Machine cnc_machine;
	private int logCount = 0;
	private TableView commandsToDo;
	private TableView commandsDone;
	private Stage stage;
	private Scene scene;
	final FileChooser fileChooser = new FileChooser();
	
	@FXML private Button idUpload;

	public UIController() {
		this.cp = new CommandProcessor();
		this.cnc_machine = new CNC_Machine(this, cp);
		
	}
	
	public void initFXML(Stage stage, WorkSurface workSurface) {
		this.stage = stage;
		this.workSurface = workSurface;
		this.scene = stage.getScene();
		this.commandsToDo = (TableView) scene.lookup("#TableToDo");
		this.commandsDone = (TableView) scene.lookup("#TableDone");
	}


	@FXML
	void onPressUploadSettings(ActionEvent event) {
		File file = fileChooser.showOpenDialog(stage);
		if (file != null) {
			cnc_machine.fraesen(cp.loadJson(file));
			scene.lookup("#idStartPause").setDisable(false);
		}
		
	}

	@FXML
	void onChoosedCode(ActionEvent event) {

	}

	@FXML
	void onPressSubmit(ActionEvent event) {

	}

	@FXML
	void onPressStart(ActionEvent event) {
		UIController that = this;

		Platform.runLater(new Runnable() {
			public void run() {
				SimulateMill myThread = new SimulateMill(cnc_machine.getCoordinates(), workSurface, cp, that);
				myThread.startDrawing();
			}
		});
	}

	@FXML
	void onPressTerminate(ActionEvent event) {

	}

	public void setCommandsToDo(String text) {
		commandsToDo.getItems().add(0, text);
//		uiLog.add(text);
//		this.commandsToDo.appendText(text + " - " + "\n");
	}

	public void updateCommandsToDo() {
//		this.commandsToDo.clear();
//		for (int i = logCount; i<uiLog.size(); i++) {
//			this.commandsToDo.appendText(uiLog.get(i) + "\n");
//		}
	}

	public void setCommandsDone() {
//		long actZeit = System.currentTimeMillis() - cp.startTime;
//		this.commandsDone.appendText(uiLog.get(logCount) + actZeit + "\n");
//		logCount++;
	}
}