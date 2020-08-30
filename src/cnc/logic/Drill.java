package cnc.logic;

import java.util.ArrayList;

import cnc.exceptions.OutOfWorksurfaceException;
import cnc.exceptions.UndefinedAngleException;

/**
 * 
 * Class for calculating all drill coordinates triggered by g-codes. Coordinates
 * are stored on ArrayList coordinates.
 *
 * @author Simon, Tim und Jonas
 */
public class Drill {

	protected ArrayList<Coordinates> coordinates;
	private boolean spindleStatus;
	private String rotationDirection;
	private boolean cooling;
	private boolean speedMode;

	/**
	 * Constructor for drill class.
	 * 
	 * @param coordinates
	 */
	public Drill(ArrayList<Coordinates> coordinates) {
		this.rotationDirection = "right";
		this.coordinates = coordinates;
		this.spindleStatus = false;
		this.cooling = false;
		this.speedMode = false;
		this.coordinates.add(new Coordinates(0, 0, false, this.cooling, this.rotationDirection, 0, this.spindleStatus));
	}

	public boolean getSpindleStatus() {
		return spindleStatus;
	}

	public void setSpindleStatus(boolean status) {
		this.spindleStatus = status;
	}

	public String getRotationDirection() {
		return rotationDirection;
	}

	public void setRotationDirection(String rotationDirection) {
		this.rotationDirection = rotationDirection;
	}

	public boolean getCooling() {
		return cooling;
	}

	public void setCooling(boolean cooling) {
		this.cooling = cooling;
		if (cooling) {
			this.speedMode = true;
		} else {
			this.speedMode = false;
		}
	}

	public boolean isSpeedMode() {
		return speedMode;
	}

	/**
	 * Saves end of MCode for later use for CommandProcessing.
	 * 
	 * @author Jonas, Tim
	 * @param pauseStop		- Case 1: end simulation, 0: nothing, -1: pause
	 */
	public void writeM(int pauseStop) {
		if (coordinates.size() > 0) {
			Coordinates hilf = coordinates.get(coordinates.size() - 1);
			coordinates.add(new Coordinates(hilf.getX(), hilf.getY(), false, this.cooling, true, null, pauseStop, this.spindleStatus));
		} else {
			coordinates.add(new Coordinates(0, 0, false, this.cooling, true, null, pauseStop, this.spindleStatus));
		}
	}

	/**
	 * 
	 * Method for calculating and store values of a line. Can be adjusted for
	 * milling to be reusable.
	 * 
	 * @author Simon und Jonas
	 * @param x2   - target value for x coordinate
	 * @param y2   - target value for y coordinate
	 * @param mill - true -> milling on; false -> milling off
	 * @throws OutOfWorksurfaceException
	 * 
	 */
	public void drawLine(int x2, int y2, boolean mill) throws OutOfWorksurfaceException {

		if (x2 > 700 || x2 < -700 || y2 > 525 || y2 < -525) {
			throw new OutOfWorksurfaceException("Target coordinate out of worksurface");
		}
		Coordinates startPoint = coordinates.get(coordinates.size() - 1);
		double deltaX = x2 - startPoint.getX();
		double deltaY = y2 - startPoint.getY();

		int distance = (int) (Math.sqrt(deltaX * deltaX + deltaY * deltaY));

		if (distance != 0) {
			for (int i = 1; i <= distance; i++) {
				double x = startPoint.getX() + (deltaX / distance * i);
				double y = startPoint.getY() + (deltaY / distance * i);

				coordinates.add(new Coordinates((int) x, (int) y, mill, this.cooling, this.rotationDirection, 0, this.spindleStatus));
			}
		}

//		Wird für Befehlstatus im UI gebraucht
		Coordinates hilf = coordinates.get(coordinates.size() - 1);
		coordinates.add(new Coordinates(hilf.getX(), hilf.getY(), true, this.cooling, true, this.rotationDirection, 0, this.spindleStatus));

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
	 * @param circleDirection (true -> counter clockwise direction; false ->
	 *                        clockwise direction
	 * @throws OutOfWorksurfaceException
	 * @throws UndefinedAngleException 
	 */
	public void drawCircle(int x2, int y2, int i, int j, boolean circleDirection) throws OutOfWorksurfaceException, UndefinedAngleException {

		int mX = (int) coordinates.get(coordinates.size() - 1).getX() + i; // x coordinate of circle center
		int mY = (int) coordinates.get(coordinates.size() - 1).getY() + j; // y coordinate of circle center

		double deltaY = mY - coordinates.get(coordinates.size() - 1).getY();
		double deltaX = mX - coordinates.get(coordinates.size() - 1).getX();
		double radius = Math.sqrt(deltaY * deltaY + deltaX * deltaX);

		if (radius != 0) {
			double circumference = 2 * Math.PI * radius;

			double begingAngle = calcAngle(mX, mY, coordinates.get(coordinates.size() - 1).getX(),
					coordinates.get(coordinates.size() - 1).getY());
			double targetAngle = calcAngle(mX, mY, x2, y2);

			if (circleDirection) { // gegen den Uhrzeigersinn
				if (begingAngle < targetAngle) {

					double distance = radius * (targetAngle - begingAngle);
					double alpha = begingAngle;

					for (double n = distance; n >= 0; n--) {

						int x = (int) Math.round((mX + radius * Math.cos(alpha)));
						int y = (int) Math.round((mY + radius * Math.sin(alpha)));

						if (x > 700 || x < -700 || y > 525 || y < -525) {
							throw new OutOfWorksurfaceException("Calculated coordinate out of worksurface");
						}

						alpha = (-(n - 1) * 2 * Math.PI / circumference + targetAngle);

						coordinates.add(new Coordinates(x, y, true, this.cooling, this.rotationDirection, 0, this.spindleStatus));
					}

				} else {

					if (targetAngle == 0) {
						targetAngle = 2 * Math.PI;
					}

					double distance = radius * (2 * Math.PI - (begingAngle - targetAngle));
					double alpha = begingAngle;

					for (double n = distance; n >= 0; n--) {

						int x = (int) Math.round((mX + radius * Math.cos(alpha)));
						int y = (int) Math.round((mY + radius * Math.sin(alpha)));

						if (x > 700 || x < -700 || y > 525 || y < -525) {
							throw new OutOfWorksurfaceException("Calculated coordinate out of worksurface");
						}

						alpha = (-(n - 1) * 2 * Math.PI / circumference + targetAngle);

						coordinates.add(new Coordinates(x, y, true, this.cooling, this.rotationDirection, 0, this.spindleStatus));
					}

				}
			} else { // im Uhrzeigersinn
				if (begingAngle < targetAngle) {

					double distance = radius * (2 * Math.PI - (targetAngle - begingAngle));
					double alpha = begingAngle;

					for (double n = distance; n >= 0; n--) {

						int x = (int) Math.round((mX + radius * Math.cos(alpha)));
						int y = (int) Math.round((mY + radius * Math.sin(alpha)));

						if (x > 700 || x < -700 || y > 525 || y < -525) {
							throw new OutOfWorksurfaceException("Calculated coordinate out of worksurface");
						}

						alpha = (((n - 1) * 2 * Math.PI / circumference) + targetAngle);

						coordinates.add(new Coordinates(x, y, true, this.cooling, this.rotationDirection, 0, this.spindleStatus));
					}

				} else {
					double distance;
					// Circumference 360 degree
					if (begingAngle == targetAngle) {
						distance = circumference;
					} else {
						distance = radius * (begingAngle - targetAngle);
					}
					double alpha = begingAngle;

					for (double n = distance; n >= 0; n--) {

						int x = (int) Math.round((mX + radius * Math.cos(alpha)));
						int y = (int) Math.round((mY + radius * Math.sin(alpha)));

						if (x > 700 || x < -700 || y > 525 || y < -525) {
							throw new OutOfWorksurfaceException("Calculated coordinate out of worksurface");
						}

						alpha = ((n - 1) * 2 * Math.PI / circumference + targetAngle);

						coordinates.add(new Coordinates(x, y, true, this.cooling, this.rotationDirection, 0, this.spindleStatus));
					}
				}

			}
		}

		Coordinates hilf = coordinates.get(coordinates.size() - 1);
		coordinates.add(new Coordinates(hilf.getX(), hilf.getY(), true, this.cooling, true, this.rotationDirection, 0, this.spindleStatus));
	}

	/**
	 * 
	 * Method used to calculate the angle between a point and the relative beginning coordinate of a circle.
	 * 
	 * @author Simon
	 * 
	 * @param mX   - x coordinate of circlecenter
	 * @param mY   - y coordinate of circlecenter
	 * @param posX - x coordinate of angle that is calculated
	 * @param posY - y coordinate of angle that is calculated
	 * @return returns value of angle between straight line parallel to x coordinate
	 *         on circlecenter point
	 * @throws UndefinedAngleException 
	 */
	private double calcAngle(double mX, double mY, double posX, double posY) throws UndefinedAngleException {
		// rechts von der Y-Achse
		if (mX < posX) {
			if (mY < posY) { // Oberhalb der X-Achse
				return (Math.atan((posY - mY) / (posX - mX)));
			} else if (posY < mY) { // Unterhalb der X-Achse
				return (2 * Math.PI - Math.atan((mY - posY) / (posX - mX)));
			}
		} else if (posX < mX) { // Links von der Y-Achse
			if (mY < posY) { // Oberhalb der X-Achse
				return (Math.PI - Math.atan((mY - posY) / (mX - posX)));
			} else if (posY < mY) {// Unterhalb der X-Achse
				return (Math.PI + Math.atan((mY - posY) / (mX - posX)));
			} else if (mY == posY) { // Auf der X-Achse
				return Math.PI;
			}
		} else if (mX == posX) {// Auf der Y-Achse
			if (mY == posY) {
				throw new UndefinedAngleException("Targetangle and center are equal");
			}
			else if (mY < posY) { // Oberhalb der X-Achse
				return (0.5 * Math.PI);
			} else { // Unterhalb der X-Achse
				return (1.5 * Math.PI);
			}
		}
		return 0;
	}

}
