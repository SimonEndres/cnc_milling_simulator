package cnc_frase_testing;

import java.util.ArrayList;

//Tim
public class Bohrer {

	final private String farbe;
	protected ArrayList<Coordinates> coordinates;
	private boolean spindelStatus;
	private String drehrichtung;
	private boolean kuehlmittel;
	private boolean speedMode;

	public Bohrer(ArrayList<Coordinates> coordinates) {
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
	
	public void writeM() {
		if (coordinates.size() > 0) {
			Coordinates hilf = coordinates.get(coordinates.size()-1);
			coordinates.add(new Coordinates(hilf.getX(), hilf.getY(), true, true));
		} else {
			coordinates.add(new Coordinates(0, 0, true, true));
		}
		
		
	}

//Simon, Jonas vorläufiges Ergebnis --> Effiziens muss angepasst werden+
//Boolean fraesen true -> Linie wird gezeichnet; false -> bohrkopf bewegt sich lediglich 
	public void drawLine(int x2, int y2, boolean mill) {
		
		double deltaY = y2 - coordinates.get(coordinates.size() - 1).getY();
		double deltaX = x2 - coordinates.get(coordinates.size() - 1).getX();
		double tmpPositionY = coordinates.get(coordinates.size() - 1).getY();
		double distance = (Math.sqrt(deltaX*deltaX + deltaY*deltaY));
		
		if (deltaX != 0) {
			double m = (deltaY) / (deltaX);
			for (double x = coordinates.get(coordinates.size() - 1).getX(); x <= x2; x += 10/distance) {
				double y = Math.round(m * x + tmpPositionY);
				coordinates.add(new Coordinates((int)x, (int)y, mill));
			}
		} else {
			for (double y = coordinates.get(coordinates.size() - 1).getY(); y <= y2; y += 10/distance) {
				coordinates.add(new Coordinates(coordinates.get(coordinates.size() - 1).getX(), (int) y, mill));
			}
		}
		Coordinates hilf = coordinates.get(coordinates.size()-1);
		coordinates.add(new Coordinates(hilf.getX(), hilf.getY(), true, true));
	}

//	G-Codes:
//	Simon und Jonas
//	Circle Direction true -> gegen den Uhrzeigersinn; false -> mit dem Uhrzeigersinn
	public void drawCircle(int x2, int y2, int i, int j, boolean circleDirection) {

		int mX = (int) coordinates.get(coordinates.size() - 1).getX() + i; // x coordinate of circle center
		int mY = (int) coordinates.get(coordinates.size() - 1).getY() + j; // y coordinate of circle center
		
		double deltaY = mY - coordinates.get(coordinates.size() - 1).getY();
		double deltaX = mX - coordinates.get(coordinates.size() - 1).getX();
		int radius = (int) Math.sqrt(deltaY * deltaY + deltaX * deltaX);
		
		double begingAngle = calcAngle(mX, mY, coordinates.get(coordinates.size() - 1).getX(), coordinates.get(coordinates.size() - 1).getY());
		double targetAngle = calcAngle(mX, mY, x2, y2);
		System.out.println("Begin: " + begingAngle + " / end: " + targetAngle);
		
		if (circleDirection) { //gegen den Uhrzeigersinn
			if(begingAngle < targetAngle) {

				double distance = radius * (targetAngle - begingAngle);
				
				for (double alpha = begingAngle*180/Math.PI; alpha < targetAngle*180/Math.PI ; alpha += 10/distance) {
					int x = (int) (mX + radius * Math.cos(alpha * Math.PI / 180));
					int y = (int) (mY + radius * Math.sin(alpha * Math.PI / 180));	
					
					coordinates.add(new Coordinates(x, y, true));
					//System.out.println("( "+ x + " / " + y + " )");
				}			
			}else{
				
				double distance = radius * (begingAngle - (2*Math.PI + targetAngle));
				
				for (double alpha = begingAngle*180/Math.PI; alpha < (targetAngle*180/Math.PI + 360); alpha += 10/distance) {
					int x = (int) (mX + radius * Math.cos(alpha * Math.PI / 180));
					int y = (int) (mY + radius * Math.sin(alpha * Math.PI / 180));	
					
					coordinates.add(new Coordinates(x, y, true));
					//System.out.println("( "+ x + " / " + y + " )");
				}
			}
		} else { //im Uhrzeigersinn
			if(begingAngle < targetAngle) {
				
				double distance = radius * ((2*Math.PI + begingAngle) - targetAngle);
				
				for (double alpha = begingAngle*180/Math.PI; alpha > (targetAngle*180/Math.PI - 360); alpha -= 10/distance) {
					int x = (int) (mX + radius * Math.cos(alpha * Math.PI / 180));
					int y = (int) (mY + radius * Math.sin(alpha * Math.PI / 180));
					
					coordinates.add(new Coordinates(x, y, true));
					//System.out.println("( "+ x + " / " + y + " )");
				}			
			}else {
				
				double distance = radius * (begingAngle - targetAngle);
				
				for (double alpha = begingAngle*180/Math.PI; alpha > (targetAngle*180/Math.PI); alpha -= 10/distance) {
					int x = (int) (mX + radius * Math.cos(alpha * Math.PI / 180));
					int y = (int) (mY + radius * Math.sin(alpha * Math.PI / 180));
					
					coordinates.add(new Coordinates(x, y, true));
					//System.out.println("( "+ x + " / " + y + " )");
				}
			}
				
		}
		Coordinates hilf = coordinates.get(coordinates.size()-1);
		coordinates.add(new Coordinates(hilf.getX(), hilf.getY(), true, true));
	}

//	Simon - Randfälle müssen nochmal überarbeitet werden -> M gleich Position muss abgefangen werden
	private double calcAngle(double mX, double mY, double posX, double posY) {
		// rechts von der Y-Achse
		if (mX < posX) {
			if (mY < posY) { //Oberhalb der X-Achse
				return (Math.atan((posY - mY) / posX - mX));
			} else if (posY < mY) { //Unterhalb der X-Achse
				return (2 * Math.PI - Math.atan((mY - posY) / (posX - mX)));
			}
		} else if (posX < mX) { //Links von der Y-Achse
			if (mY < posY) { // Oberhalb der X-Achse
				return (Math.PI - Math.atan((mY - posY) / (mX - posX)));
			} else if (mY < posY) {//Unterhalb der X-Achse
				return (Math.PI + Math.atan((mY - posY) / (mX - posX)));
			}
		} else if (mX == posX) {// Auf der Y-Achse
			if (mY < posY) { //Oberhalb der X-Achse
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
