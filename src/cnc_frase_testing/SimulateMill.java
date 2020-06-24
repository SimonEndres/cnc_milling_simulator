package cnc_frase_testing;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class SimulateMill extends Thread{
	
	protected ArrayList<Coordinates> coordinates;
	private WorkSurface workSurface;
	
	public SimulateMill(ArrayList<Coordinates> coordinates, WorkSurface workSurface) {
		this.coordinates = coordinates;
		this.workSurface = workSurface;
		System.out.println("beep");
	}
	public void run() {
		 
        while (!this.isInterrupted()) {
             
            // UI updaten
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    // entsprechende UI Komponente updaten
                	workSurface.drawPoint(0, 0, true);
                }
            });
 
            // Thread schlafen
            try {
                // fuer 3 Sekunden
                sleep(TimeUnit.SECONDS.toMillis(3));
            } catch (InterruptedException ex) {
                
            }
      }
	//zum zeichnen eines Punktes auf der Oberfläche
//		void drawPoint(int x, int y, boolean fraesen) {
//			Circle bohrkopf = new Circle();
//			bohrkopf.setCenterX((float) x);
//			bohrkopf.setCenterY((float) y);
//			bohrkopf.setRadius(2.0f);
//	
//			bohrkopf.setFill(Color.ORANGE);
//			if (fraesen)
//				cutLine.getChildren().add(bohrkopf);
//		}


	}
}
