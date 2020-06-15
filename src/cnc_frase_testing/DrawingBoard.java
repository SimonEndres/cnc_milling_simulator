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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

final class DrawingBoard {
	
	final private Stage primaryStage;
	final private Scene mainScene;
	final private BorderPane mainSceneLayout;
	final private Group cutLine;
	final private HBox bottomBox;
	private JSONObject json; 
	private Desktop desktop = Desktop.getDesktop();

	DrawingBoard(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.mainSceneLayout = new BorderPane();
		this.mainScene = new Scene(mainSceneLayout, 600, 600);
		this.cutLine = new Group();
		this.bottomBox = new HBox();
		final FileChooser fileChooser = new FileChooser();

		this.mainSceneLayout.setCenter(cutLine);

		this.primaryStage.setTitle("Drawing Operations Test");

		primaryStage.setScene(mainScene);
		primaryStage.show();

//		UI Bottom part
		HBox startBtnBox = new HBox(new Button("Start/Pause"));
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
		
		uploadBtn.setOnAction(new EventHandler<ActionEvent>(){
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
				loadJson(file);
			}
		});
		this.mainSceneLayout.setTop(uploadBtn);
		
	}
	
	private void loadJson(File file) {
		try {
			String jsonData = new String(Files.readAllBytes(Paths.get(file.toURI())), "UTF-8");
			JSONObject json = new JSONObject(jsonData);
			this.json = json;
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	void drawCircle(int x, int y) {
		Circle bohrkopf = new Circle();
		bohrkopf.setCenterX((float) x);
		bohrkopf.setCenterY((float) y);
		bohrkopf.setRadius(2.0f);

		bohrkopf.setFill(Color.ORANGE);
		cutLine.getChildren().add(bohrkopf);
	}

	private void openFile(File file) {
		try {
			this.desktop.open(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}