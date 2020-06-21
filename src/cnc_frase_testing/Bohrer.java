package cnc_frase_testing;

//Tim
public class Bohrer {

	final private String farbe;
	private double[] position;
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
	//evtl unnötig, je nach Verhalten von Speedmode
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

//Simon, Jonas vorläufiges Ergebnis --> Effiziens muss angepasst werden+
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

	// G-Codes:
	public void drawCircle(int x2, int y2, int i, int j) {
		int h = (int) position[0] + i; // x coordinate of circle center
		int k = (int) position[1] + j; // y coordinate of circle center
		double deltaY = k - position[1];
		double deltaX = h - position[0];
		int radius = (int)Math.sqrt(deltaY*deltaY + deltaX*deltaX);
//		int anfangsWinkel = 
		for (int alpha = 0; alpha < 360; alpha++) {
//			if (position[0]==x2 || position[1] == y2) //Funktioniert noch nicht, da die Koordinaten beim Kreis random berechnet werden
//				break;
			int x = (int) (h + radius * Math.cos(alpha*Math.PI/180));
			int y = (int) (k + radius * Math.sin(alpha*Math.PI/180));
			arbeitsFlaeche.drawPoint(x, y, true);
//			this.position[0] = x;
//			this.position[1] = y;
		}
	}

}
