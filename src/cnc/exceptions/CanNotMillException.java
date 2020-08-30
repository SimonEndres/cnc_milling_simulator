package cnc.exceptions;
/**
 * 
 * Exception raises if mill is impossible due to missing M commands
 * 
 * @author Simon
 *
 */
public class CanNotMillException extends Exception {

	public CanNotMillException(String message) {
		super(message);
	}
}

