package cnc_frase_testing;

import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONArray;

public class Logger {
    private static final Logger OBJ = new Logger();
     
      private Logger() {
          System.out.println("Objekt gebildet...");
      }
          
      public static synchronized Logger getInstance() {
        return OBJ;
      }
      public void logToFile(JSONArray logArray) {
  		try {
  			FileWriter file = new FileWriter("data//CNC_Fraese_Log.txt");
  			file.write(logArray.toString());
  			file.close();
  		} catch (IOException e) {
  			e.printStackTrace();
  		}
  	}
}