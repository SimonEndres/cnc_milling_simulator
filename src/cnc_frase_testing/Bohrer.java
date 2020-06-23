package cnc_frase_testing;

//Tim
public class Bohrer {

	final private String farbe;
	public double[] position;
	private boolean spindelStatus;
	private String drehrichtung;
	private boolean kuehlmittel;
	private boolean speedMode;
	final DrawingBoard arbeitsFlaeche;

	public Bohrer(DrawingBoard arbeitsFlaeche) {
		this.farbe = "rot";
		this.position = new double[] { 0.0, 0.0 };
		this.spindelStatus = false;
		this.kuehlmittel = false;
		this.speedMode = false;
		this.arbeitsFlaeche = arbeitsFlaeche;
		setDrehrichtung("rechts");
	}

	public Bohrer(DrawingBoard arbeitsFlaeche, String farbe, boolean status, String drehrichtung, boolean kuehlmittel,
			boolean speedMode) {
		this.farbe = farbe;
		this.position = new double[] { 0.0, 0.0 };
		this.spindelStatus = status;
		this.kuehlmittel = kuehlmittel;
		this.speedMode = speedMode;
		this.arbeitsFlaeche = arbeitsFlaeche;

		setDrehrichtung(drehrichtung);
	}

	public String getFarbe() {
		return farbe;
	}

	public double[] getPosition() {
		return position;
	}

	public void setPosition(int x, int y) {
		this.position[0] = x;
		this.position[1] = y;
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

	public void setK�hlmittel(boolean kuehlmittel) {
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

	// evtl unn�tig, je nach Verhalten von Speedmode
	public void setSpeedMode(boolean speedMode) {
		this.speedMode = speedMode;
		if (speedMode) {
			if (isKuehlmittel()) {
				this.speedMode = speedMode;
			} else {
				System.out.println("SpeedMode ohne K�hlmittel nicht m�glich");
				this.speedMode = false;
			}
		}
	}

//Simon, Jonas vorl�ufiges Ergebnis --> Effiziens muss angepasst werden+
//Boolean fraesen true -> Linie wird gezeichnet; false -> bohrkopf bewegt sich lediglich 
	public void drawLine(int x2, int y2, boolean fraesen) {
		double deltaY = y2 - position[1];
		double deltaX = x2 - position[0];
		double tmpPositionY = position[1];
		if (deltaX != 0) {
			double m = (deltaY) / (deltaX);
			for (double x = position[0]; x <= x2; x += 0.001) {
				double y = Math.round(m * x + tmpPositionY);
				arbeitsFlaeche.drawPoint((int) x, (int) y, fraesen);
				this.position[0] = x;
				this.position[1] = y;
			}
		} else {
			for (double y = position[1]; y <= y2; y += 0.001) {
				arbeitsFlaeche.drawPoint((int) position[0], (int) y, fraesen);
				this.position[1] = y;
			}
		}
	}

//	G-Codes:
//	Simon und Jonas
//	Circle Direction true -> gegen den Uhrzeigersinn; false -> mit dem Uhrzeigersinn
	public void drawCircle(int x2, int y2, int i, int j, boolean circleDirection) {
		int mX = (int) position[0] + i; // x coordinate of circle center
		int mY = (int) position[1] + j; // y coordinate of circle center
		double deltaY = mY - position[1];
		double deltaX = mX - position[0];
		int radius = (int) Math.sqrt(deltaY * deltaY + deltaX * deltaX);
		double begingAngle = calcAngle(mX, mY, position[0], position[1]);
		double targetAngle = calcAngle(mX, mY, x2, y2);
		if (circleDirection) { //gegen den Uhrzeigersinn
			for (double alpha = begingAngle*180/Math.PI; alpha < targetAngle*180/Math.PI ; alpha++) {
				int x = (int) (mX + radius * Math.cos(alpha * Math.PI / 180));
				int y = (int) (mY - radius * Math.sin(alpha * Math.PI / 180));
				arbeitsFlaeche.drawPoint(x, y, true);
				this.position[0] = x;
				this.position[1] = y;
				//System.out.println("( "+ x + " / " + y + " )");
			}
		} else { //im Uhrzeigersinn
			for (double alpha = begingAngle*180/Math.PI; alpha > (targetAngle*180/Math.PI - 360); alpha--) {
				int x = (int) (mX + radius * Math.cos(alpha * Math.PI / 180));
				int y = (int) (mY - radius * Math.sin(alpha * Math.PI / 180));
				arbeitsFlaeche.drawPoint(x, y, true);
				this.position[0] = x;
				this.position[1] = y;
				//System.out.println("( "+ x + " / " + y + " )");
			}
		}
	}

//	Simon - Randf�lle m�ssen nochmal �berarbeitet werden -> M gleich Position muss abgefangen werden
	private double calcAngle(double mX, double mY, double posX, double posY) {
		// rechts von der Y-Achse
		if (mX < posX) {
			if (posY < mY) { //Oberhalb der X-Achse
				return (Math.atan((mY - posY) / posX - mX));
			} else if (mY < posY) { //Unterhalb der X-Achse
				return (2 * Math.PI - Math.atan((posY - mY) / (posX - mX)));
			}
		} else if (posX < mX) { //Links von der Y-Achse
			if (posY < mY) { // Oberhalb der X-Achse
				return (Math.PI - Math.atan((mY - posY) / (mX - posX)));
			} else if (mY < posY) {//Unterhalb der X-Achse
				return (Math.PI + Math.atan((posY - mY) / (mX - posX)));
			}
		} else if (mX == posX) {// Auf der Y-Achse
			if (posY < mY) { //Oberhalb der X-Achse
				return (0.5 * Math.PI);
			} else { // Unterhalb der X-Achse
				return (1.5 * Math.PI);
			}
		} else if (mY == posY) { // Auf der X-Achse
			if (posX < mX) { //Links von Y-Achse
				return Math.PI;
			}
		}
		return 0;
	}

}
