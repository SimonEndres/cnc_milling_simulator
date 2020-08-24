package cnc_frase_testing;

import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONArray;

/**
 * 
 * Class responsible for storing log data in the file system
 * 
 * @author Tim
 *
 */
public class Logger {
	private static final Logger OBJ = new Logger();

	public static synchronized Logger getInstance() {
		return OBJ;
	}

	/**
	 * 
	 * Stores the passed JSONArray in the file system
	 * 
	 * @param logArray
	 * @author Tim
	 */
	public void logToFile(JSONArray logArray) {
		try {
			FileWriter file = new FileWriter("data//CNC_Fraese_Log.json");
			file.write(logArray.toString());
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}