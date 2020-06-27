package cnc_frase_testing;

public class ExceptionHandler {
	
	public static void corruptFile() {
		ServiceClass.writeLog("ERROR", "Corrupt JSONFile, ", "can't load commands");
	}
	
	public static void wrongCode(String commandType, String commandNumber) {
		ServiceClass.writeLog("ERROR", commandType+commandNumber, "doesn't exists");
	}
	
	public static void wrongCommand(String commandType) {
		ServiceClass.writeLog("ERROR", commandType, "doesn't exists");
	}
}
