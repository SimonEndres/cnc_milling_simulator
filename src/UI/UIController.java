package UI;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import cnc_frase_testing.CNC_Machine;
import cnc_frase_testing.CommandProcessor;
import cnc_frase_testing.ExceptionHandler;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Border;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Class for controlling the user Interface of the cnc machine simulator Ui
 * design is done using fxml
 * 
 * @author Tim
 *
 */
public class UIController {

	private WorkSurface workSurface;
	private DrillPointer drillPointer;
	private CoolingSimulator coolingSimulater;
	private CommandProcessor cp;
	private CNC_Machine cnc_machine;
	private HomePoint homePoint;
	private StackPane workSurfaceGroup;
	private ArrayList<String> uiLog;
	private int logCount;
	private Stage stage;
	private int numVar;
	private int millSpeed;
	private int driveSpeed;
	private MediaPlayer mediaPlayer;

	private ObservableList<String> commandColl;
	private ObservableList<String> settingsColl;
	final FileChooser fileChooser = new FileChooser();

	@FXML
	private Button buttUplCom;
	@FXML
	private ComboBox<String> comboBox;
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
	private Slider slidMillSpeed;
	@FXML
	private Slider slidDriveSpeed;
	@FXML 
	private Label currSpeed;
	@FXML
	private TextArea commandsToDo;
	@FXML
	private TextArea commandsDone;
	@FXML
	private Label spinStat;
	@FXML
	private Label rotDir;
	@FXML
	private Label currPosition;
	@FXML
	private Circle coolStat;
	@FXML
	private Button buttSP;
	@FXML
	private Button buttTerminate;
	@FXML
	private Button buttRes;
	@FXML
	private ComboBox<String> comboSett;
	@FXML
	private ColorPicker colorPic;
	@FXML
	private Button buttAudio;

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
		this.logCount = 0;
		this.millSpeed = 2;
		this.driveSpeed = 4;
		
		mediaPlayer = new MediaPlayer(new Media(new File("audio//machine_3.wav").toURI().toString()));
		mediaPlayer.setVolume(0.5);
		mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
		
		commandColl = FXCollections.observableArrayList("M00", "M02", "M03", "M04", "M05", "M08", "M09", "M13", "M14",
				"G00", "G01", "G02", "G03", "G28");
		settingsColl = FXCollections.observableArrayList("Drill", "Homepoint", "Work surface");
	}

	/**
	 * Initializing FXML setting items for combo box adding event Listeners to
	 * textfields, and and adding changelistener to Drillspeed Slider
	 * 
	 * @param stage
	 * @param workSurface
	 * @param drillPointer
	 * 
	 * @author Tim
	 */
	public void initFXML(Stage stage, WorkSurface workSurface, DrillPointer drillPointer,
			CoolingSimulator coolingSimulater, HomePoint homePoint, StackPane workSurfaceGroup) {
		this.stage = stage;
		this.workSurface = workSurface;
		this.drillPointer = drillPointer;
		this.coolingSimulater = coolingSimulater;
		this.homePoint = homePoint;
		this.workSurfaceGroup = workSurfaceGroup;
		changeAudio(true);
		workSurfaceGroup.setStyle("-fx-border-color: #cccccc");
		comboBox.setItems(commandColl);
		comboSett.setItems(settingsColl);
		tfX.textProperty().addListener((obs, oldText, newText) -> onInputChanged('X', newText));
		tfY.textProperty().addListener((obs, oldText, newText) -> onInputChanged('Y', newText));
		tfI.textProperty().addListener((obs, oldText, newText) -> onInputChanged('I', newText));
		tfJ.textProperty().addListener((obs, oldText, newText) -> onInputChanged('J', newText));
		slidMillSpeed.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number previous, Number now) {
				if (!slidMillSpeed.isValueChanging() || now.doubleValue() == 1 || now.doubleValue() == 3) {
					// This only fires when we're done or when the slider is dragged to its max/min.
					changeMillSpeed();
				}
			}
		});
		slidDriveSpeed.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number previous, Number now) {
				if (!slidDriveSpeed.isValueChanging() || now.doubleValue() == 4 || now.doubleValue() == 8) {
					// This only fires when we're done or when the slider is dragged to its max/min.
					changeDriveSpeed();
				}
			}
		});
		
	}

	/**
	 * Event method for clicking Button to Upload Commands
	 * 
	 * @param event
	 */
	@FXML
	void onPressUploadSettings(ActionEvent event) {
		File file = fileChooser.showOpenDialog(stage);
		if (file != null) {
			buttSP.setDisable(false);
			cnc_machine.machineControl(cp.loadJson(file));
		}
	}

	/**
	 * Event method for choosing g-code in dropdown (For manual entry)
	 * 
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

	/**
	 * Submit for manual entry - start process of checking and calculating command
	 * 
	 * @author Tim
	 * @param event
	 */
	@FXML
	void onPressSubmit(ActionEvent event) {
		JSONObject newJson = new JSONObject();
		JSONArray jsonArr = new JSONArray();
		JSONObject newCommand = new JSONObject();
		JSONObject parameters = new JSONObject();
		newCommand.put("number", "");
		newCommand.put("code", comboBox.getValue());
		try {
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
			// Enable Start
			buttSP.setDisable(false);
			cnc_machine.machineControl(newJson);
			// Clear textfields
			tfX.setText("");
			tfY.setText("");
			tfI.setText("");
			tfJ.setText("");
			buttSubmit.setDisable(true);
		}catch (NumberFormatException e) {
			ExceptionHandler.handleErrorByMessage(this, cp, e.getMessage(), "Correct input");
		}
	}
	
	/**
	 * Event method for changing driving speed of the drill (when it's milling)
	 * 
	 * @param event
	 * @author Tim, Simon
	 */
	private void changeMillSpeed() {
		this.millSpeed = (int) slidMillSpeed.getValue();
	}
	
	/**
	 * Event method for changing driving speed of the drill (when it isn't milling)
	 * 
	 * @param event
	 * @author Tim
	 */
	private void changeDriveSpeed() {
		this.driveSpeed = (int) slidDriveSpeed.getValue();
	}

	/**
	 * Event method for pressing start/stop button if there is no thread for
	 * simulating the milling a thread is started and drawing is started if thread
	 * is running and counter is running so the machine is drawing the machine is
	 * stopped. else if the counter is not running this method starts it.
	 * 
	 * This functionality makes the button a toggle for drawing
	 * 
	 * @param event
	 * @author Jonas und Tim
	 */
	@FXML
	public void onPressStartStop(ActionEvent event) {
		buttTerminate.setDisable(false);
		UIController that = this;
		if (myThread == null) {
			buttTerminate.setDisable(false);
			Platform.runLater(new Runnable() {
				public void run() {
					myThread = new SimulateMill(cnc_machine.getCoordinates(), workSurface, drillPointer,
							coolingSimulater, cp, that);
					myThread.startDrawing();
				}
			});
			mediaPlayer.play();
			buttSP.setText("Stop");
			setSpinStat("true");
		} else if (myThread.isRunning()) {
			myThread.pause();
			myThread.setRunning(false);
			buttSP.setText("Start");
			mediaPlayer.pause();
			setCurrSpeed(0);
			setSpinStat("false");
		} else {
			myThread.unpause();
			myThread.setRunning(true);
			buttSP.setText("Stop");
			mediaPlayer.play();
			setSpinStat("true");
		}
	}

	/**
	 * Terminating UI by User (Button)
	 * 
	 * @author Tim
	 * @param event
	 */
	@FXML
	public void onPressTerminate(ActionEvent event) {
		terminate();
		showMessage("Process successfully terminated");
	}

	/**
	 * Terminating UI by Error
	 * 
	 * @author Tim
	 * @param event
	 */
	public void terminate() {
		if (myThread != null) {
			myThread.pause();
		}
		mediaPlayer.pause();
		setSpinStat("false");
		buttSP.setText("Start");
		setCurrSpeed(0);
		buttSP.setDisable(true);
		buttTerminate.setDisable(true);
		buttUplCom.setDisable(true);
		comboBox.setDisable(true);
		buttRes.setDisable(false);
		commandsToDo.clear();
		cp.logMessage("Terminate", "Process terminated", "reset or close");
		try {
			cp.logAll();
		} catch (Exception e) {
			ExceptionHandler.handleErrorByMessage(this, cp, "Could not write log", "retry");
		}
	}

	/**
	 * Resetting UI by User (Button) - like changing the work surface
	 * 
	 * @author Tim
	 * @param event
	 */
	@FXML
	public void onPressReset(ActionEvent event) {
		if (myThread != null) {
			myThread.reset();
		}
		myThread = null;
		setSpinStat("false");
		buttSP.setText("Start");
		buttSP.setDisable(true);
		buttTerminate.setDisable(true);
		buttRes.setDisable(true);
		buttUplCom.setDisable(false);
		comboBox.setDisable(false);
		commandsDone.clear();
		uiLog.clear();
		logCount = 0;
		setRotDir("right");
		setCoolStat(false);
		setPosition("0 / 0");
		showMessage("Worksurface successfully reset");
	}

	/**
	 * Enable changing color of Drill, Homepoint and work surface
	 * 
	 * @author Jonas, Tim
	 * @param event
	 */
	@FXML
	void onColorChange(ActionEvent event) {
		String value = comboSett.getValue();
		if (value == null) {
			this.showError("No setting selected use dropdown");
		} else if (value.equals("Drill")) {
			setDrillColor();
		} else if (value.equals("Homepoint")) {
			setHomeColor();
		} else {
			setWorkColor();
		}
	}

	/**
	 * Method to open Logfile
	 * 
	 * @param event
	 * @author Simon
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
	
	@FXML
	void onPressAudio(ActionEvent event) {
		changeAudio(false);
	}

	/**
	 * Method triggered by process end. Enables Reset.
	 * 
	 * @author Tim
	 */
	public void millEnd() {
		myThread.pause();
		myThread.setRunning(false);
		mediaPlayer.pause();
		setSpinStat("false");
		buttRes.setDisable(false);
		buttSP.setText("Start");
		buttSP.setDisable(true);
		buttTerminate.setDisable(true);
	}

	/**
	 * Method for changed input. Determines validity of input and enables submit
	 * button
	 * 
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
	 * 
	 * @param field   - defines changed inputfield
	 * @param newText - entered Text
	 * @author Tim
	 */
	private void numberFormatter(char field, String newText) {
		String value = newText;
		value = value.replaceAll("\\b-?[^0-9]", "");
		switch (field) {
		case 'X':
			tfX.setText(value);
			break;
		case 'Y':
			tfY.setText(value);
			break;
		case 'I':
			tfI.setText(value);
			break;
		case 'J':
			tfJ.setText(value);
			break;
		default:
			break;
		}
	}

	/**
	 * Method to set commands that must be processed (To Do)
	 * 
	 * @param text - entered Command (with parameters)
	 * @author Tim
	 */
	public void setCommandsToDo(String text) {
		uiLog.add(text);
		this.commandsToDo.appendText(text + " - " + "\n");
	}

	/**
	 * Method to update To Do commands on UI
	 * 
	 * @author Tim
	 */
	public void updateCommandsToDo() {
		this.commandsToDo.clear();
		for (int i = logCount; i < uiLog.size(); i++) {
			this.commandsToDo.appendText(uiLog.get(i) + "\n");
		}
	}

	/**
	 * Method to set commands done, also displaying runtime
	 * 
	 * @author Tim
	 */
	public void setCommandsDone() {
		long actZeit = System.currentTimeMillis() - cp.startTime;
		this.commandsDone.appendText(uiLog.get(logCount) + actZeit + "\n");
		logCount++;
	}

	/**
	 * Method to show message on a dialog
	 * 
	 * @param message - Message which is shown
	 * @author Tim
	 */
	public void showMessage(String message) {
		Alert al = new Alert(AlertType.INFORMATION);
		al.setContentText(message);
		al.show();
	}

	/**
	 * Method to show error message on a dialog
	 * 
	 * @param message - Message which is shown
	 * @author Tim
	 */
	public void showError(String message) {
		Alert al = new Alert(AlertType.ERROR);
		al.setContentText(message);
		al.show();
	}

	/**
	 * Method to get the speed of the drill (speed while milling)
	 * 
	 * @author Tim
	 */
	public int getMillSpeed() {
		return millSpeed;
	}
	
	/**
	 * Method to get the speed of the drill (speed while not milling)
	 * 
	 * @author Tim
	 */
	public int getDriveSpeed() {
		return driveSpeed;
	}
	
	public void setCurrSpeed(int speed) {
		currSpeed.setText(speed + " m/min");
	}

	/**
	 * Update Drill Information spindle status
	 * 
	 * @author Tim
	 * @param status - spindle status
	 */
	public void setSpinStat(String status) {
		spinStat.setText(status);
	}

	/**
	 * Update Drill Information rotation direction
	 * 
	 * @author Tim
	 * @param direction - new rotation direction
	 */
	public void setRotDir(String direction) {
		rotDir.setText(direction);
	}

	/**
	 * Update Drill Information cooling status
	 * 
	 * @author Tim
	 * @param status - new cooling status
	 */
	public void setCoolStat(boolean status) {
		if (status) {
			coolStat.setFill(Color.SKYBLUE);
		} else {
			coolStat.setFill(Color.RED);
		}
	}

	/**
	 * Update Drill Information current position (as coordinates)
	 * 
	 * @author Tim
	 * @param newPos - new current position
	 */
	public void setPosition(String newPos) {
		currPosition.setText(newPos);
	}

	/**
	 * Changes Color of Homepoint
	 * 
	 * @author Jonas, Tim
	 */
	public void setHomeColor() {
		homePoint.setColor(colorPic.getValue());
	}

	/**
	 * Changes Color of Work surface
	 * 
	 * @author Jonas, Tim
	 */
	public void setWorkColor() {
		workSurfaceGroup.setStyle("-fx-border-color: #cccccc;-fx-background-color: " + colorPic.getValue().toString().replaceAll("0x", "#"));
	}

	/**
	 * Changes Color of Drill
	 * 
	 * @author Jonas, Tim
	 */
	public void setDrillColor() {
		if (myThread != null) {
			drillPointer.setColor(colorPic.getValue(), myThread.getCurrentCoordinate().getX() + 420,
					(-myThread.getCurrentCoordinate().getY()) + 315);
		} else
			drillPointer.setColor(colorPic.getValue(), 420, 315);
	}
	
	/**
	 * Change system audio
	 * @author Simon, Tim
	 * @param first		- true for initial activation of the system audio
	 */
	public void changeAudio(boolean first) {
		String source;
		if (mediaPlayer.isMute() || first) {
			source = "img//SoundOn.png";
			mediaPlayer.setMute(false);
		} else {
			source = "img//SoundOff.png";
			mediaPlayer.setMute(true);
		}
		try (InputStream is = new FileInputStream(source);){
			Image image = new Image(is);
			buttAudio.setGraphic(new ImageView(image));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}