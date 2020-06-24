package cnc_frase_testing;

public class Coordinates {

	private int x, y;
	private boolean mill;
	public Coordinates(int x, int y, boolean mill) {
		this.x = x;
		this.y = y;
		this.mill = mill;
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
	
	
}
