package UI;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import cnc_frase_testing.CNC_Machine;
import cnc_frase_testing.CommandProcessor;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class UIController {

	private WorkSurface workSurface;
	private DrillPointer drillPointer;
	private CoolingSimulator coolingSimulater;
	private CommandProcessor cp;
	private CNC_Machine cnc_machine;
	private ArrayList<String> uiLog;
	private int logCount = 0;
	private Stage stage;
	private int numVar;
	private int speed = 4;

	private ObservableList<String> commandColl;
	final FileChooser fileChooser = new FileChooser();

	@FXML
	private Button buttUplCom;
	@FXML
	private ComboBox<String> comboBox;
	@FXML
	private TextArea commandsToDo;
	@FXML
	private TextArea commandsDone;
	@FXML
	private TextField tfX;
	@FXML
	private TextField tfY;
	@FXML
	private TextField tfI;
	@FXML
	private TextField tfJ;
	@FXML
	private Button buttSubmit;
	@FXML
	private Button buttSP;
	@FXML
	private Button buttTerminate;
	@FXML
	private Slider drillSpeed;
	@FXML
	private Button buttRes;

	// test
	SimulateMill myThread = null;

	public UIController() {
		this.cp = new CommandProcessor();
		this.cnc_machine = new CNC_Machine(this, cp);
		this.uiLog = new ArrayList<String>();
		commandColl = FXCollections.observableArrayList("M00", "M02", "M03", "M04", "M05", "M08", "M09", "M13", "M14",
				"G00", "G01", "G02", "G03", "G28");
	}

	public void initFXML(Stage stage, WorkSurface workSurface, DrillPointer drillPointer, CoolingSimulator coolingSimulater) {
		this.stage = stage;
		this.workSurface = workSurface;
		this.drillPointer = drillPointer;
		this.coolingSimulater = coolingSimulater;
		comboBox.setItems(commandColl);
		tfX.textProperty().addListener((obs, oldText, newText) -> onInputChanged('X', newText));
		tfY.textProperty().addListener((obs, oldText, newText) -> onInputChanged('Y', newText));
		tfI.textProperty().addListener((obs, oldText, newText) -> onInputChanged('I', newText));
		tfJ.textProperty().addListener((obs, oldText, newText) -> onInputChanged('J', newText));
		drillSpeed.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
	        public void changed(ObservableValue<? extends Number> observableValue, Number previous, Number now) {
	            if (!drillSpeed.isValueChanging() || now.doubleValue() == 4 || now.doubleValue() == 8) {
	                // This only fires when we're done or when the slider is dragged to its max/min.
	            	changeDrillspeed();
	            }
			}
		});
	}

	@FXML
	void onPressUploadSettings(ActionEvent event) {
		File file = fileChooser.showOpenDialog(stage);
		if (file != null) {
			cnc_machine.machineControl(cp.loadJson(file));
			buttSP.setDisable(false);
		}
	}
	
	@FXML
	void doTest(ActionEvent event) {
		System.out.println(drillSpeed.getValue());
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
				numVar = 2; // X and Y needed
				tfX.setDisable(false);
				tfY.setDisable(false);
				if (commandNumber.equals("02") || commandNumber.equals("03")) {
					numVar = 4; // X,Y,I,J needed
					tfI.setDisable(false);
					tfJ.setDisable(false);
				}
			}
		} else {
			// Case M code enable submit
			buttSubmit.setDisable(false);
		}
		// To proof if submit should be disabled/enabled after changing Code
		onInputChanged('p', "proof");
	}

	@FXML
	void onPressSubmit(ActionEvent event) {
		JSONObject newJson = new JSONObject();
		JSONArray jsonArr = new JSONArray();
		JSONObject newCommand = new JSONObject();
		JSONObject parameters = new JSONObject();
		newCommand.put("number", "");
		newCommand.put("code", comboBox.getValue());
		if (numVar >= 2) {
			parameters.put("x", Integer.parseInt(tfX.getText()));
			parameters.put("y", Integer.parseInt(tfY.getText()));
			if (numVar == 4) {
				parameters.put("i", Integer.parseInt(tfI.getText()));
				parameters.put("j", Integer.parseInt(tfJ.getText()));
			}
		}
		newCommand.put("parameters", parameters);
		jsonArr.put(newCommand);
		newJson.put("commands", jsonArr);
		cnc_machine.machineControl(newJson);
		//Enable Start
		buttSP.setDisable(false);
		//Clear textfields
		tfX.setText("");
		tfY.setText("");
		tfI.setText("");
		tfJ.setText("");
		buttSubmit.setDisable(true);
	}

	void changeDrillspeed() {
		this.speed = (int) drillSpeed.getValue();
		System.out.println(speed);
	}

	@FXML
	public void onPressStartStop(ActionEvent event) {
		buttTerminate.setDisable(false);
		UIController that = this;
		if (myThread == null) {
			buttTerminate.setDisable(false);
			Platform.runLater(new Runnable() {
				public void run() {
					myThread = new SimulateMill(cnc_machine.getCoordinates(), workSurface, drillPointer, coolingSimulater, cp, that);
					myThread.startDrawing();
				}
			});
			buttSP.setText("Stop");
		} else if (myThread.isRunning()) {
			myThread.pause();
			myThread.setRunning(false);
			buttSP.setText("Start");
		} else {
			myThread.unpause();
			myThread.setRunning(true);
			buttSP.setText("Stop");
		}
	}

	@FXML
	public void onPressTerminate(ActionEvent event) {
		myThread.pause();
		buttSP.setText("Start");
		buttSP.setDisable(true);
		buttTerminate.setDisable(true);
		buttUplCom.setDisable(true);
		comboBox.setDisable(true);
		buttRes.setDisable(false);
		commandsToDo.clear();
		cp.logMessage("Terminate", "Process terminated", "reset or close");
		cp.logAll();
		showMessage("Process successfully terminated");
	}
	
	@FXML
	public void onPressReset(ActionEvent event) {
		myThread.reset();
		myThread = null;
		buttSP.setText("Start");
		buttSP.setDisable(true);
		buttTerminate.setDisable(true);
		buttRes.setDisable(true);
		buttUplCom.setDisable(false);
		comboBox.setDisable(false);
		commandsDone.clear();
		uiLog.clear();
		logCount = 0;
		showMessage("Worksurface successfully reset");
	}
	@FXML
	void onPressLog(ActionEvent event) {
		File log = new File("data//CNC_Fraese_Log.json");
		Desktop desktop = Desktop.getDesktop();
		try {
			desktop.open(log);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void millEnd() {
		buttRes.setDisable(false);
		buttSP.setText("Start");
		buttSP.setDisable(true);
		buttTerminate.setDisable(true);
	}

	private void onInputChanged(char field, String newText) {
		if (!newText.equals("")) {
			// Format input value
			numberFormatter(field, newText);
			if (numVar == 2) {
				// Input X and Y needed for submit
				if (!tfX.getText().equals("") && !tfY.getText().equals("")) {
					buttSubmit.setDisable(false);
				} else {
					buttSubmit.setDisable(true);
				}
			}
			if (numVar == 4) {
				// Input X,Y,I,J needed for submit
				if (!tfX.getText().equals("") && !tfY.getText().equals("") && !tfI.getText().equals("") && !tfJ.getText().equals("")) {
					buttSubmit.setDisable(false);
				} else {
					buttSubmit.setDisable(true);
				}
			}
		} else {
			buttSubmit.setDisable(true);
		}
	}

	private void numberFormatter(char field, String newText) {
		String value;
		switch (field) {
		case 'X':
			value = tfX.getText();
			value = value.replaceAll("\\b-?[^0-9]", "");
			tfX.setText(value);
			break;
		case 'Y':
			value = tfY.getText();
			value = value.replaceAll("\\b-?[^0-9]", "");
			tfY.setText(value);
			break;
		case 'I':
			value = tfI.getText();
			value = value.replaceAll("\\b-?[^0-9]", "");
			tfI.setText(value);
			break;
		case 'J':
			value = tfJ.getText();
			value = value.replaceAll("\\b-?[^0-9]", "");
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
		for (int i = logCount; i < uiLog.size(); i++) {
			this.commandsToDo.appendText(uiLog.get(i) + "\n");
		}
	}

	public void setCommandsDone() {
		long actZeit = System.currentTimeMillis() - cp.startTime;
		this.commandsDone.appendText(uiLog.get(logCount) + actZeit + "\n");
		logCount++;
	}

	public void showMessage(String message) {
		Alert al = new Alert(AlertType.INFORMATION);
		al.setContentText(message);
		al.show();
	}

	public void showError(String message) {
		Alert al = new Alert(AlertType.ERROR);
		al.setContentText(message);
		al.show();
	}

	public int getSpeed() {
		return speed;
	}

}