package UI;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import cnc_frase_testing.CNC_Machine;
import cnc_frase_testing.CommandProcessor;
import cnc_frase_testing.SimulateMill;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class UIController {

	private WorkSurface workSurface;
	private CommandProcessor cp;
	private CNC_Machine cnc_machine;
	private ArrayList<String> uiLog;
	private int logCount = 0;
	
	private Stage stage;
	private Scene scene;
	private ObservableList<String> commandColl;
	final FileChooser fileChooser = new FileChooser();
	
	@FXML private ComboBox<String> comboBox;
	@FXML private TextArea commandsToDo;
	@FXML private TextArea commandsDone;
	
	public UIController() {
		this.cp = new CommandProcessor();
		this.cnc_machine = new CNC_Machine(this, cp);
		this.uiLog = new ArrayList<String>();
		commandColl = FXCollections.observableArrayList(
				"M00",
				"M02",
				"M03",
				"M04",
				"M05",
				"M08",
				"M09",
				"M13",
				"M14",
				"G00",
				"G01",
				"G02",
				"G03",
				"G28"
				);
	}
	
	public void initFXML(Stage stage, WorkSurface workSurface) {
		this.stage = stage;
		this.workSurface = workSurface;
		this.scene = stage.getScene();
		comboBox.setItems(commandColl);
	}


	@FXML
	void onPressUploadSettings(ActionEvent event) {
		File file = fileChooser.showOpenDialog(stage);
		if (file != null) {
			cnc_machine.fraesen(cp.loadJson(file));
			scene.lookup("#StartPause").setDisable(false);
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
		uiLog.add(text);
		this.commandsToDo.appendText(text + " - " + "\n");
	}

	public void updateCommandsToDo() {
		this.commandsToDo.clear();
		for (int i = logCount; i<uiLog.size(); i++) {
		this.commandsToDo.appendText(uiLog.get(i) + "\n");
		}
	}

	public void setCommandsDone() {
		long actZeit = System.currentTimeMillis() - cp.startTime;
		this.commandsDone.appendText(uiLog.get(logCount) + actZeit + "\n");
		logCount++;
	}
}