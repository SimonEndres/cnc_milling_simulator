package cnc_frase_testing;

 public class ExceptionHandler{

	public static void logError(CommandProcessor cp, String reason, String handling) {
		cp.logMessage("ERROR", reason, handling);
	}
}
