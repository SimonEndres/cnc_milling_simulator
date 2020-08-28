package cnc_frase_testing;

import UI.UIController;

public class ExceptionHandler{

	public static void handleErrorByTerminating(UIController ui,CommandProcessor cp, String reason, String handling) {
		logError(cp, reason, handling);
		ui.showError(reason + handling);
		ui.onPressTerminate(null);
	}
	public static void handleErrorByMessage(UIController ui,CommandProcessor cp,String reason, String handling) {
		logError(cp, reason, handling);
		ui.showError(reason + handling);
	}
	public static void logError(CommandProcessor cp, String reason, String handling) {
		cp.logMessage("ERROR", reason, handling);
	}
}
