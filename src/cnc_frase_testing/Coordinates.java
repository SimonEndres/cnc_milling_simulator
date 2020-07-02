package cnc_frase_testing;

/**
 * Class used to store coordinates and regarding information needed for the UI.
 * 
 * @author Simon
 *
 */
public class Coordinates {

	private int x, y;
	private boolean mill;
	private boolean end;

	public Coordinates(int x, int y, boolean mill) {
		this.x = x;
		this.y = y;
		this.mill = mill;
	}

	public Coordinates(int x, int y, boolean mill, boolean end) {
		this.x = x;
		this.y = y;
		this.mill = mill;
		this.end = end;
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

}
