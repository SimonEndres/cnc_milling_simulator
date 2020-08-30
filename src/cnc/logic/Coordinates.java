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
	private String rotation;

	private boolean end;

	public Coordinates(int x, int y, boolean mill, boolean cooling, String rotation) {
		this.x = x;
		this.y = y;
		this.mill = mill;
		this.cooling = cooling;
		this.rotation = rotation;
	}

	public Coordinates(int x, int y, boolean mill, boolean cooling, boolean end, String rotation) {
		this.x = x;
		this.y = y;
		this.mill = mill;
		this.cooling = cooling;
		this.end = end;
		this.rotation = rotation;
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
		return end;
	}

	public void setEnd(boolean end) {
		this.end = end;
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

}
