package cnc.exceptions;

/**
 * 
 * Exception raises if a calculated point or the target point of draw line method is outside the worksurface.
 * 
 * @author Simon
 *
 */
public class OutOfWorksurfaceException extends Exception {

	public OutOfWorksurfaceException(String message) {
		super(message);
	}
}
