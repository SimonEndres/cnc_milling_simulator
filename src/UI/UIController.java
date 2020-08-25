package UI;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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

/**
 * Class for controlling the user Interface of the cnc machine simulator
 * Ui design is done using fxml
 * 
 * @author Tim
 *
 */
public class UIController {

	private WorkSurface workSurface;
	private DrillPointer drillPointer;
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

	SimulateMill myThread = null;
	
	/**
	 * Constructor for UIControllerClass
	 * 
	 * @author Tim
	 */
	public UIController() {
		this.cp = new CommandProcessor();
		this.cnc_machine = new CNC_Machine(this, cp);
		this.uiLog = new ArrayList<String>();
		commandColl = FXCollections.observableArrayList("M00", "M02", "M03", "M04", "M05", "M08", "M09", "M13", "M14",
				"G00", "G01", "G02", "G03", "G28");
	}

	/**
	 * Initializing FXML setting items for combo box adding event Listeners to textfields, and
	 * and adding changelistener to Drillspeed Slider
	 * 
	 * @param stage
	 * @param workSurface
	 * @param drillPointer
	 * 
	 * @author Tim
	 */
	public void initFXML(Stage stage, WorkSurface workSurface, DrillPointer drillPointer) {
		this.stage = stage;
		this.workSurface = workSurface;
		this.drillPointer = drillPointer;
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

	/**
	 * Event method for clicking Button to Upload Commands
	 * @param event
	 */
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
	
	/**
	 * Event method for choosing g-code in dropdown
	 * @param event
	 * @author Tim
	 */
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

	}
	
	/**
	 * Event method for changing drillspeed
	 * @param event
	 * @author Tim
	 */
	void changeDrillspeed() {
		this.speed = (int) drillSpeed.getValue();
		System.out.println(speed);
	}

	/**
	 * Event method for pressing start/stop button
	 * if there is no thread for simulating the milling a thread is started and drawing is started
	 * if thread is running and counter is running so the machine is drawing the machine is stopped.
	 * else if the counter is not running this method starts it.
	 * 
	 * This functionality makes the button a toggle for drawing
	 * @param event
	 * @author Jonas und Tim
	 */
	@FXML
	void onPressStart(ActionEvent event) {
		UIController that = this;
		if (myThread == null) {
			buttTerminate.setDisable(false);
			Platform.runLater(new Runnable() {
				public void run() {
					myThread = new SimulateMill(cnc_machine.getCoordinates(), workSurface, drillPointer, cp, that);
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

	/**
	 * Resetting UI when terminated
	 * 
	 * @author Tim
	 * @param event
	 */
	@FXML
	public void onPressTerminate(ActionEvent event) {
		myThread.terminate();
		myThread = null;
		buttSP.setText("Start");
		buttSP.setDisable(true);
		buttTerminate.setDisable(true);
		commandsToDo.clear();
		commandsDone.clear();
		uiLog.clear();
		logCount = 0;
		showMessage("Process terminated successfully");
	}

	/**
	 * Method to open Logfile
	 * @param event
	 * @author Tim
	 */
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

	/**
	 * Method for changed input. Determines validity of input and enables submit button
	 * @param field
	 * @param newText
	 * 
	 * @author Tim
	 */
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
				if (!tfX.getText().equals("") && !tfY.getText().equals("") && !tfI.getText().equals("")
						&& !tfJ.getText().equals("")) {
					buttSubmit.setDisable(false);
				} else {
					buttSubmit.setDisable(true);
				}
			}
		} else {
			buttSubmit.setDisable(true);
		}
	}

	/**
	 * Method to format inputs only allowing integer values
	 * @param field
	 * @param newText
	 * @author Tim
	 */
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

	/**
	 * Method to set commands which are toDo
	 * @param text
	 * @author Tim
	 */
	public void setCommandsToDo(String text) {
		uiLog.add(text);
		this.commandsToDo.appendText(text + " - " + "\n");
	}
	
	/**
	 * Method to update ToDocommands on Ui
	 * @param text
	 * @author Tim
	 */
	public void updateCommandsToDo() {
		this.commandsToDo.clear();
		for (int i = logCount; i < uiLog.size(); i++) {
			this.commandsToDo.appendText(uiLog.get(i) + "\n");
		}
	}
	/**
	 * Method to set Commands done also displaying time taken
	 * @param text
	 * @author Tim
	 */
	public void setCommandsDone() {
		long actZeit = System.currentTimeMillis() - cp.startTime;
		this.commandsDone.appendText(uiLog.get(logCount) + actZeit + "\n");
		logCount++;
	}

	/**
	 * Method to show message on popups
	 * @param text
	 * @author Tim
	 */
	public void showMessage(String message) {
		Alert al = new Alert(AlertType.INFORMATION);
		al.setContentText(message);
		al.show();
	}

	/**
	 * Method to show error message on popups
	 * @param text
	 * @author Tim
	 */
	public void showError(String message) {
		Alert al = new Alert(AlertType.ERROR);
		al.setContentText(message);
		al.show();
	}

	/**
	 * Method to get the speed
	 * @param text
	 * @author Tim
	 */
	public int getSpeed() {
		return speed;
	}

}