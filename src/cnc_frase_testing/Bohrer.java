package cnc_frase_testing;

public class Bohrer {
	
	private String farbe;
	private Integer [] position;
	private boolean status;
	private String drehrichtung;
	private boolean k�hlmittel;
	private boolean speedMode;
	final DrawingBoard arbeitsFlaeche;
	
	

	public Bohrer(DrawingBoard arbeitsFlaeche,String farbe, boolean status, String drehrichtung, boolean k�hlmittel, boolean speedMode) {
		this.farbe = farbe;
		this.position = new Integer [] {0,0};
		this.status = status;
		this.k�hlmittel = k�hlmittel;
		this.speedMode = speedMode;
		this.arbeitsFlaeche = arbeitsFlaeche;
		drawLine(1,200,1,200);
		
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

	public boolean isK�hlmittel() {
		return k�hlmittel;
	}

	public void setK�hlmittel(boolean k�hlmittel) {
		this.k�hlmittel = k�hlmittel;
		if (!k�hlmittel) {
			this.speedMode = false;
		}
	}

	public boolean isSpeedMode() {
		return speedMode;
	}

	public void setSpeedMode(boolean speedMode) {
		this.speedMode = speedMode;
		if (speedMode) {
			if (isK�hlmittel()) {
				this.speedMode = speedMode;
			} else {
				System.out.println("SpeedMode ohne K�hlmittel nicht m�glich");
				this.speedMode = false;
			}
		}
	}
	public void drawLine(int x1, int x2, int y1, int y2)
	{
	   int m = (y2 - y1)/(x2 - x1);
	   for (int x  = x1; x <= x2; x++) 
	   {    
	      int y = Math.round(m * x + y1);    
	      arbeitsFlaeche.drawCircle(x, y);
	   }
	}
}
