package cnc_frase_testing;

import UI.UIController;
/**
 * 
 * Class that includes handling of occured errors.
 * 
 * @author Tim, Jonas, Simon
 *
 */
public class ExceptionHandler{

	public static void handleErrorByTerminating(UIController ui,CommandProcessor cp, String reason, String handling) {
		logError(cp, reason, handling);
		ui.terminate();
		ui.showError(reason + ": " + handling);
	}
	public static void handleErrorByMessage(UIController ui,CommandProcessor cp,String reason, String handling) {
		logError(cp, reason, handling);
		ui.showError(reason + ": " + handling);
	}
	public static void logError(CommandProcessor cp, String reason, String handling) {
		cp.logMessage("ERROR", reason, handling);
	}
}
