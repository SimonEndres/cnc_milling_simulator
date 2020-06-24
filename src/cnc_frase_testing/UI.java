
package cnc_frase_testing;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;

import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.Scene;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

final class UI {

	final private Stage primaryStage;
	final private Scene mainScene;
	final private BorderPane mainSceneLayout;
	final private HBox bottomBox;
	private JSONObject json;
	private Desktop desktop = Desktop.getDesktop();

	private Controller cnc_fraese;

	UI(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.mainSceneLayout = new BorderPane();
		this.mainScene = new Scene(mainSceneLayout, 1000, 1000);
		this.bottomBox = new HBox();
		WorkSurface workSurface = new WorkSurface(840, 630);
		this.cnc_fraese = new Controller(this);
		final FileChooser fileChooser = new FileChooser();

		this.mainSceneLayout.setCenter(workSurface);

		this.primaryStage.setTitle("Drawing Operations Test");

		primaryStage.setScene(mainScene);
		primaryStage.show();

//		UI Bottom part
		Button startBtn = new Button("Start/Pause");
		HBox startBtnBox = new HBox(startBtn);

		startBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// SimulateMill mill = new SimulateMill(cnc_fraese.coordinates,workSurface);

				// UI updaten
				Platform.runLater(new Runnable() {
					public void run() {
						SimulateMill myThread = new SimulateMill(cnc_fraese.coordinates, workSurface);
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

		uploadBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				File file = fileChooser.showOpenDialog(primaryStage);
				if (file != null) {
					openFile(file);
					List<File> files = Arrays.asList(file);
					System.out.println("Geklappt");
				}
			}
		});
		fileChooser.setTitle("Upload Settingsdatei");
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Json-Files", "*.json"));
		uploadBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				File file = fileChooser.showOpenDialog(primaryStage);
				if (file != null) {
					cnc_fraese.fraesen(loadJson(file));
				}
			}
		});
		this.mainSceneLayout.setTop(uploadBtn);

	}

	private JSONObject loadJson(File file) {
		JSONObject json = null;
		try {
			String jsonData = new String(Files.readAllBytes(Paths.get(file.toURI())), "UTF-8");
			json = new JSONObject(jsonData);
		} catch (Exception e) {
			System.out.println(e);
		}
		return json;
	}

	private void openFile(File file) {
		try {
			this.desktop.open(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}