package cnc.exceptions;

/**
 * 
 * Exception raised when calculation of an angle is impossible.
 * 
 * @author Simon
 *
 */
public class UndefinedAngleException extends Exception{
	
	public UndefinedAngleException(String message) {
		super(message);
	}

}
