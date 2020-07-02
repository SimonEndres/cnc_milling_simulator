package cnc_frase_testing;

import java.util.ArrayList;

/**
 * 
 * Class for calculating all drill coordinates triggered by g-codes. Coordinates
 * are stored on ArrayList coordinates.
 *
 * @author Simon, Tim und Jonas
 */
public class Drill {

	/**
	 * Arraylist containing x and y values and additional information regarding each
	 * point, such as milling status, cooling status, etc.
	 */
	protected ArrayList<Coordinates> coordinates;
	final private String farbe;
	private boolean spindelStatus;
	private String drehrichtung;
	private boolean kuehlmittel;
	private boolean speedMode;

	/**
	 * Constructor for drill class. 
	 * @param coordinates
	 */
	public Drill(ArrayList<Coordinates> coordinates) {
		this.farbe = "rot";
		this.coordinates = coordinates;
		this.coordinates.add(new Coordinates(0, 0, false));
		this.spindelStatus = false;
		this.kuehlmittel = false;
		this.speedMode = false;
		setDrehrichtung("rechts");
	}

	public String getFarbe() {
		return farbe;
	}

	public boolean isStatus() {
		return spindelStatus;
	}

	public void setSpindelStatus(boolean status) {
		this.spindelStatus = status;
	}

	public String getDrehrichtung() {
		return drehrichtung;
	}

	public void setDrehrichtung(String drehrichtung) {
		if (drehrichtung.equals("rechts") || drehrichtung.equals("links")) {
			this.drehrichtung = drehrichtung;
		} else {
			System.out.println("Die eingegebene Drehrichtung existiert nicht. Drehrichtung auf rechts gesetzt");
			this.drehrichtung = "rechts";
		}
	}

	public boolean isKuehlmittel() {
		return kuehlmittel;
	}

	public void setKühlmittel(boolean kuehlmittel) {
		this.kuehlmittel = kuehlmittel;
		if (kuehlmittel) {
			this.speedMode = true;
		} else {
			this.speedMode = false;
		}
	}

	public boolean isSpeedMode() {
		return speedMode;
	}

	// evtl unnötig, je nach Verhalten von Speedmode
	public void setSpeedMode(boolean speedMode) {
		this.speedMode = speedMode;
		if (speedMode) {
			if (isKuehlmittel()) {
				this.speedMode = speedMode;
			} else {
				System.out.println("SpeedMode ohne Kühlmittel nicht möglich");
				this.speedMode = false;
			}
		}
	}

	/**
	 * Saves end of MCode for later use for CommandProcessing
	 * 
	 * @author Jonas, Tim
	 */
	public void writeM() {
		if (coordinates.size() > 0) {
			Coordinates hilf = coordinates.get(coordinates.size() - 1);
			coordinates.add(new Coordinates(hilf.getX(), hilf.getY(), true, true));
		} else {
			coordinates.add(new Coordinates(0, 0, true, true));
		}

	}

	/**
	 * 
	 * Method for calculating and store values of a line. Can be adjusted for milling to be reusable.
	 * 
	 * @author Simon und Jonas
	 * @param x2   - target value for x coordinate
	 * @param y2   - target value for y coordinate
	 * @param mill - true -> milling on; false -> milling off
	 * 
	 */
	public void drawLine(int x2, int y2, boolean mill) {

		Coordinates startPoint = coordinates.get(coordinates.size() - 1);
		double deltaX = x2 - startPoint.getX();
		double deltaY = y2 - startPoint.getY();

		int distance = (int) (Math.sqrt(deltaX * deltaX + deltaY * deltaY));

		if (distance == 0) {
			// throw exception

		}

		for (int i = 1; i <= distance; i++) {
			double x = startPoint.getX() + (deltaX / distance * i);
			double y = startPoint.getY() + (deltaY / distance * i);

			coordinates.add(new Coordinates((int) x, (int) y, mill));
			System.out.println("( " + x + " / " + y + " )");
		}

//		Wird für Befehlstatus im UI gebraucht
		Coordinates hilf = coordinates.get(coordinates.size() - 1);
		coordinates.add(new Coordinates(hilf.getX(), hilf.getY(), true, true));

	}

	/**
	 * 
	 * Method for calculating and store values of a circle or part of a circle.
	 * 
	 * @author Simon und Jonas
	 * @param x2              - target value for x coordinate
	 * @param y2              - target value for y coordinate
	 * @param i               - relative offset of x value for circlecenter
	 * @param j               - relative offset of y value for circlecenter
	 * @param circleDirection (true -> clockwise direction; false -> counter
	 *                        clockwise direction
	 */
	public void drawCircle(int x2, int y2, int i, int j, boolean circleDirection) {

		int mX = (int) coordinates.get(coordinates.size() - 1).getX() + i; // x coordinate of circle center
		int mY = (int) coordinates.get(coordinates.size() - 1).getY() + j; // y coordinate of circle center

		double deltaY = mY - coordinates.get(coordinates.size() - 1).getY();
		double deltaX = mX - coordinates.get(coordinates.size() - 1).getX();
		int radius = (int) Math.sqrt(deltaY * deltaY + deltaX * deltaX);

		double begingAngle = calcAngle(mX, mY, coordinates.get(coordinates.size() - 1).getX(),
				coordinates.get(coordinates.size() - 1).getY());
		double targetAngle = calcAngle(mX, mY, x2, y2);
		System.out.println("Begin: " + begingAngle + " / end: " + targetAngle);

		if (circleDirection) { // gegen den Uhrzeigersinn
			if (begingAngle < targetAngle) {

				double distance = radius * (targetAngle - begingAngle);

				for (double alpha = begingAngle * 180 / Math.PI; alpha < targetAngle * 180
						/ Math.PI; alpha += (10 / distance)) {
					int x = (int) Math.round((mX + radius * Math.cos(alpha * Math.PI / 180)));
					int y = (int) Math.round((mY + radius * Math.sin(alpha * Math.PI / 180)));

					coordinates.add(new Coordinates(x, y, true));
					System.out.println("( " + x + " / " + y + " )");
				}
			} else {

				double distance = radius * (begingAngle - (2 * Math.PI + targetAngle));

				for (double alpha = begingAngle * 180 / Math.PI; alpha < (targetAngle * 180 / Math.PI
						+ 360); alpha += (10 / distance)) {
					int x = (int) Math.round((mX + radius * Math.cos(alpha * Math.PI / 180)));
					int y = (int) Math.round((mY + radius * Math.sin(alpha * Math.PI / 180)));

					coordinates.add(new Coordinates(x, y, true));
					System.out.println("( " + x + " / " + y + " )");
				}
			}
		} else { // im Uhrzeigersinn
			if (begingAngle < targetAngle) {

				double distance = radius * ((2 * Math.PI + begingAngle) - targetAngle);

				for (double alpha = begingAngle * 180 / Math.PI; alpha > (targetAngle * 180 / Math.PI
						- 360); alpha -= (10 / distance)) {
					int x = (int) Math.round((mX + radius * Math.cos(alpha * Math.PI / 180)));
					int y = (int) (mY + radius * Math.sin(alpha * Math.PI / 180));

					coordinates.add(new Coordinates(x, y, true));
					System.out.println("( " + x + " / " + y + " )");
				}
			} else {

				double distance = radius * (begingAngle - targetAngle);

				for (double alpha = begingAngle * 180 / Math.PI; alpha > (targetAngle * 180 / Math.PI); alpha -= (10
						/ distance)) {
					int x = (int) Math.round((mX + radius * Math.cos(alpha * Math.PI / 180)));
					int y = (int) Math.round((mY + radius * Math.sin(alpha * Math.PI / 180)));

					coordinates.add(new Coordinates(x, y, true));
					System.out.println("( " + x + " / " + y + " )");
				}
			}

		}
		Coordinates hilf = coordinates.get(coordinates.size() - 1);
		coordinates.add(new Coordinates(hilf.getX(), hilf.getY(), true, true));
	}

	/**
	 * @author Simon
	 * 
	 * @param mX   - x coordinate of circlecenter
	 * @param mY   - y coordinate of circlecenter
	 * @param posX - x coordinate of angle that is calculated
	 * @param posY - y coordinate of angle that is calculated
	 * @return returns value of angle between straight line parallel to x coordinate
	 *         on circlecenter point
	 */
	private double calcAngle(double mX, double mY, double posX, double posY) {
		// rechts von der Y-Achse
		if (mX < posX) {
			if (mY < posY) { // Oberhalb der X-Achse
				return (Math.atan((posY - mY) / posX - mX));
			} else if (posY < mY) { // Unterhalb der X-Achse
				return (2 * Math.PI - Math.atan((mY - posY) / (posX - mX)));
			}
		} else if (posX < mX) { // Links von der Y-Achse
			if (mY < posY) { // Oberhalb der X-Achse
				return (Math.PI - Math.atan((mY - posY) / (mX - posX)));
			} else if (mY < posY) {// Unterhalb der X-Achse
				return (Math.PI + Math.atan((mY - posY) / (mX - posX)));
			}
		} else if (mX == posX) {// Auf der Y-Achse
			if (mY < posY) { // Oberhalb der X-Achse
				return (0.5 * Math.PI);
			} else { // Unterhalb der X-Achse
				return (1.5 * Math.PI);
			}
		} else if (mY == posY) { // Auf der X-Achse
			if (posX < mX) { // Links von Y-Achse
				return Math.PI;
			}
		}
		return 0;
	}

}
