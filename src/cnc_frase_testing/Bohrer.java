package cnc_frase_testing;

public class Bohrer {
	
	private String farbe;
	private Integer [] position;
	private boolean status;
	private char drehrichtung;
	private boolean kühlmittel;
	private boolean speedMode;
	
	

	public Bohrer(String farbe, boolean status, char drehrichtung, boolean kühlmittel, boolean speedMode) {
		this.farbe = farbe;
		this.position = new Integer [] {0,0};
		this.status = status;
		this.kühlmittel = kühlmittel;
		this.speedMode = speedMode;
		
		setDrehrichtung(drehrichtung);
	}

	public String getFarbe() {
		return farbe;
	}

	public void setFarbe(String farbe) {
		this.farbe = farbe;
	}

	public Integer[] getPosition() {
		return position;
	}

	public void setPosition(int x, int y) {
		this.position[0] = x;
		this.position[1] = y;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public char getDrehrichtung() {
		return drehrichtung;
	}

	public void setDrehrichtung(char drehrichtung) {
		if (drehrichtung == 'r' || drehrichtung == 'l') {
			this.drehrichtung = drehrichtung;
		} else {
			System.out.println("Die eingegebene Drehrichtung existiert nicht. Drehrichtung auf r gesetzt");
			this.drehrichtung = 'r';
		}
	}

	public boolean isKühlmittel() {
		return kühlmittel;
	}

	public void setKühlmittel(boolean kühlmittel) {
		this.kühlmittel = kühlmittel;
	}

	public boolean isSpeedMode() {
		return speedMode;
	}

	public void setSpeedMode(boolean speedMode) {
		this.speedMode = speedMode;
		if (speedMode) {
			if (isKühlmittel()) {
				this.speedMode = speedMode;
			} else {
				System.out.println("SpeedMode ohne Kühlmittel nicht möglich");
				this.speedMode = false;
			}
		}
	}
}
