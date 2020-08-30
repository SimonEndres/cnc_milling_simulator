package cnc.logic;

/**
 * Class used to store coordinates and regarding information needed for the UI.
 * 
 * @author Simon
 *
 */
public class Coordinates {

	private int x, y;
	private boolean mill;
	private boolean cooling;
	private boolean commandFinished;
	private String rotation;
	private int pauseStop;
	private boolean spindleStatus;


	public Coordinates(int x, int y, boolean mill, boolean cooling, String rotation, int pauseStop, boolean spindleStatus) {
		this.x = x;
		this.y = y;
		this.mill = mill;
		this.cooling = cooling;
		this.rotation = rotation;
		this.pauseStop = pauseStop;
		this.spindleStatus = spindleStatus;
	}

	public Coordinates(int x, int y, boolean mill, boolean cooling, boolean commandFinished, String rotation, int pauseStop, boolean spindleStatus) {
		this.x = x;
		this.y = y;
		this.mill = mill;
		this.cooling = cooling;
		this.commandFinished = commandFinished;
		this.rotation = rotation;
		this.pauseStop = pauseStop;
		this.spindleStatus = spindleStatus;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean isMill() {
		return mill;
	}

	public void setMill(boolean mill) {
		this.mill = mill;
	}

	public boolean isEnd() {
		return commandFinished;
	}

	public void setEnd(boolean commandFinished) {
		this.commandFinished = commandFinished;
	}

	public boolean isCooling() {
		return cooling;
	}
	
	public void setCooling(boolean cooling) {
		this.cooling = cooling;
	}
	
	public String getRotation() {
		return rotation;
	}
	
	public void setRotation(String rotation) {
		this.rotation = rotation;
	}
	
	public int getPauseStop() {
		return pauseStop;
	}

	public void setPauseStop(int pauseStop) {
		this.pauseStop = pauseStop;
	}

	public boolean isSpindleStatus() {
		return spindleStatus;
	}

	public void setSpindleStatus(boolean spindleStatus) {
		this.spindleStatus = spindleStatus;
	}

}
