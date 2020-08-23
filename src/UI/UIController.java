package UI;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import cnc_frase_testing.CNC_Machine;
import cnc_frase_testing.CommandProcessor;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class UIController {

	private WorkSurface workSurface;
	private DrillPointer drillPointer;
	private CommandProcessor cp;
	private CNC_Machine cnc_machine;
	private ArrayList<String> uiLog;
	private int logCount = 0;
	private Stage stage;
	private Scene scene;
	private int numVar;
	private ObservableList<String> commandColl;
	final FileChooser fileChooser = new FileChooser();
	
	@FXML private ComboBox<String> comboBox;
	@FXML private TextArea commandsToDo;
	@FXML private TextArea commandsDone;
	@FXML private TextField tfX;
	@FXML private TextField tfY;
	@FXML private TextField tfI;
	@FXML private TextField tfJ;
	@FXML private Button buttSubmit;
	
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
	
	public void initFXML(Stage stage, WorkSurface workSurface, DrillPointer drillPointer) {
		this.stage = stage;
		this.workSurface = workSurface;
		this.drillPointer = drillPointer;
		this.scene = stage.getScene();
		comboBox.setItems(commandColl);
		tfX.textProperty().addListener((obs, oldText, newText) -> onInputChanged('X',newText));
		tfY.textProperty().addListener((obs, oldText, newText) -> onInputChanged('Y',newText));
		tfI.textProperty().addListener((obs, oldText, newText) -> onInputChanged('I',newText));
		tfJ.textProperty().addListener((obs, oldText, newText) -> onInputChanged('J',newText));
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
		numVar = 0;
		tfX.setDisable(true);
		tfY.setDisable(true);
		tfI.setDisable(true);
		tfJ.setDisable(true);
		String commandType = comboBox.getValue().replaceAll("[0-9]", "");
		String commandNumber = comboBox.getValue().replaceAll("[A-Z]", "");
		if (commandType.equals("G")) {
			if (!commandNumber.equals("28")) {
				numVar = 2; //X and Y needed
				tfX.setDisable(false);
				tfY.setDisable(false);
				if (commandNumber.equals("02") || commandNumber.equals("03")) {
					numVar = 4; //X,Y,I,J needed
					tfI.setDisable(false);
					tfJ.setDisable(false);
				}
			}
		}
		//onInputChanged('i', "init");
	}
	
	@FXML
	void onPressSubmit(ActionEvent event) {
		
	}

	@FXML
	void onPressStart(ActionEvent event) {
		UIController that = this;

		Platform.runLater(new Runnable() {
			public void run() {
				SimulateMill myThread = new SimulateMill(cnc_machine.getCoordinates(), workSurface, drillPointer, cp, that);
				myThread.startDrawing();
			}
		});
	}

	@FXML
	void onPressTerminate(ActionEvent event) {
		
	}

	private void onInputChanged(char field, String newText) {
		if (!newText.equals("")) {
			//Format input value
			numberFormatter(field,newText);
			if (numVar == 2) {
				//Input X and Y needed for submit
				if (!tfX.getText().equals("") && !tfY.getText().equals("")) {
					buttSubmit.setDisable(false);
				}
			}
			if (numVar == 4) {
				//Input X,Y,I,J needed for submit
				if (!tfX.getText().equals("") && !tfY.getText().equals("") && !tfI.getText().equals("") && !tfJ.getText().equals("")) {
					buttSubmit.setDisable(false);
				}
			}
		} else {
			buttSubmit.setDisable(true);
		}
	}
	
	private void numberFormatter(char field,String newText) {
		String value;
		switch (field) {
		case 'X':
			value = tfX.getText();
			value = value.replaceAll("\\b-?[^0-9]", "");
			tfX.setText(value);
			break;
		case 'Y':
			value = tfY.getText();
			value = value.replaceAll("[^0-9]", "");
			tfY.setText(value);
			break;
		case 'I':
			value = tfI.getText();
			value = value.replaceAll("[^0-9]", "");
			tfI.setText(value);
			break;
		case 'J':
			value = tfJ.getText();
			value = value.replaceAll("[^0-9]", "");
			tfJ.setText(value);
			break;
		default:
			break;
		}
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