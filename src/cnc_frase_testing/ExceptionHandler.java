package cnc_frase_testing;

public class ExceptionHandler {
	
	public static void corruptFile() {
		ServiceClass.writeWorkList("ERROR", "Corrupt JSONFile, ", "can't load commands");
	}
	
	public static void wrongCode(String commandType, String commandNumber) {
		ServiceClass.writeWorkList("ERROR", commandType+commandNumber, "doesn't exists");
	}
	
	public static void wrongCommand(String commandType) {
		ServiceClass.writeWorkList("ERROR", commandType, "doesn't exists");
	}
}
