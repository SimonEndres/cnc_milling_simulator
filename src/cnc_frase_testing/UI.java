
package cnc_frase_testing;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.Scene;

final class UI {

	final private Stage primaryStage;
	final private Scene mainScene;
	final private BorderPane mainSceneLayout;
	final private HBox bottomBox;
	
	private TextArea commandsToDo;
	private TextArea commandsDone;
	private Controller cnc_fraese;
	private CommandProcessor cp;
	private UI that;
	private ArrayList<String> uiLog;
	private int logCount = 0;

	UI(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.mainSceneLayout = new BorderPane();
		this.mainScene = new Scene(mainSceneLayout, 1000, 1000);
		this.bottomBox = new HBox();
		WorkSurface workSurface = new WorkSurface(840, 630);
		
		this.cp = new CommandProcessor();
		this.cnc_fraese = new Controller(this, cp);
		final FileChooser fileChooser = new FileChooser();
		this.commandsToDo = new TextArea();
		this.commandsDone = new TextArea();
		this.uiLog = new ArrayList<String>();
		
		
		commandsToDo.setEditable(false);

		
		commandsDone.setEditable(false);


		
		this.mainSceneLayout.setLeft(commandsToDo);
		this.mainSceneLayout.setCenter(workSurface);
		this.mainSceneLayout.setRight(commandsDone);
		
		this.primaryStage.setTitle("Drawing Operations Test");

		primaryStage.setScene(mainScene);
		primaryStage.show();

//		UI Bottom part
		Button startBtn = new Button("Start/Pause");
		HBox startBtnBox = new HBox(startBtn);
		that = this;
		startBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			
			public void handle(ActionEvent event) {
				// SimulateMill mill = new SimulateMill(cnc_fraese.coordinates,workSurface);
				
				// UI updaten
				Platform.runLater(new Runnable() {
					public void run() {
						SimulateMill myThread = new SimulateMill(cnc_fraese.coordinates, workSurface, cp, that);
						myThread.startDrawing();
					}
				});
			}
		});
		startBtnBox.setAlignment(Pos.CENTER_LEFT);
		HBox.setHgrow(startBtnBox, Priority.ALWAYS);
		HBox.setMargin(startBtnBox, new Insets(0, 20, 20, 20));
		HBox cancelBtnBox = new HBox(new Button("Cancel"));
		cancelBtnBox.setAlignment(Pos.CENTER_RIGHT);
		HBox.setHgrow(cancelBtnBox, Priority.ALWAYS);
		HBox.setMargin(cancelBtnBox, new Insets(0, 20, 20, 20));
		bottomBox.getChildren().addAll(startBtnBox, cancelBtnBox);
		this.mainSceneLayout.setBottom(bottomBox);

//		UI Top part
//		JSON-file contains customizable settings for the UI
		Button uploadBtn = new Button("Upload Settingsdatei");
		fileChooser.setTitle("Upload Settingsdatei");
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Json-Files", "*.json"));
		uploadBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				File file = fileChooser.showOpenDialog(primaryStage);
				if (file != null) {
					cnc_fraese.fraesen(cp.loadJson(file));
				}
			}
		});
		this.mainSceneLayout.setTop(uploadBtn);

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