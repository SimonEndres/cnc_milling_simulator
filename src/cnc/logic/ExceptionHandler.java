package cnc.logic;

import cnc.ui.UIController;
/**
 * 
 * Class that includes handling of occured errors. All Errors get logged automatically.
 * 
 * @author Tim, Jonas, Simon
 *
 */
public class ExceptionHandler{

	/**
	 * 
	 * Method to handle Errors by terminating process
	 * 
	 * @author Tim, Simon
	 * @param ui
	 * @param cp
	 * @param reason
	 * @param handling
	 */
	public static void handleErrorByTerminating(UIController ui,CommandProcessor cp, String reason, String handling) {
		logError(cp, reason, handling);
		ui.terminate();
		ui.showError(reason + "- Handling: " + handling);
	}
	
	/**
	 * 
	 * Method to handle Errors showing a message to the user
	 * 
	 * @author Tim, Simon
	 * @param ui
	 * @param cp
	 * @param reason
	 * @param handling
	 */
	public static void handleErrorByMessage(UIController ui,CommandProcessor cp,String reason, String handling) {
		logError(cp, reason, handling);
		ui.showError(reason + "- Handling: " + handling);
	}
	
	/**
	 * 
	 * Method to log errors.
	 * 
	 * @author Tim
	 * @param cp
	 * @param reason
	 * @param handling
	 */
	public static void logError(CommandProcessor cp, String reason, String handling) {
		cp.logMessage("ERROR", reason, handling);
	}
}
