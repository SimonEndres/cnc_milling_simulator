package cnc_frase_testing;

public class ExceptionHandler {
	
	public static void wrongCode(String befehlsart, String befehlsnummer) {
		ServiceClass.writeLog("ERROR", befehlsart+befehlsnummer, "doesn't exists");
	}
	
	public static void wrongCommand(String befehlsart) {
		ServiceClass.writeLog("ERROR", befehlsart, "doesn't exists");
	}
}
