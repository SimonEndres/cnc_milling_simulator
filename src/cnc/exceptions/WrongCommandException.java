package cnc.exceptions;

/**
 * 
 * Exception raised if an user uploads a json-file containing wrong commands.
 * 
 * @author Tim
 *
 */
public class WrongCommandException extends Exception{
	
	public WrongCommandException(String message) {
		super(message);
	}
}
