package cnc.logic;

import java.io.FileWriter;

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
	public void logToFile(JSONArray logArray) throws Exception{
		FileWriter file = new FileWriter("data//cnc_simulator_log.json");
		file.write(logArray.toString());
		file.close();		
	}
}