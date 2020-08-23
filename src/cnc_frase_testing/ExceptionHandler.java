package cnc_frase_testing;

import UI.UIController;

public class ExceptionHandler{

	public static void logErrorTerminate(UIController ui,CommandProcessor cp, String reason, String handling) {
		logError(cp, reason, handling);
		ui.onPressTerminate(null);
	}
	public static void logError(CommandProcessor cp, String reason, String handling) {
		cp.logMessage("ERROR", reason, handling);
	}
}
