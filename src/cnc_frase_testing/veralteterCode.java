//package cnc_frase_testing;
//
//public class veralteterCode {
//
////	G-Codes:
////	Simon und Jonas
////	Circle Direction true -> gegen den Uhrzeigersinn; false -> mit dem Uhrzeigersinn
//	public void drawCircle(int x2, int y2, int i, int j, boolean circleDirection) {
//
//		int mX = (int) coordinates.get(coordinates.size() - 1).getX() + i; // x coordinate of circle center
//		int mY = (int) coordinates.get(coordinates.size() - 1).getY() + j; // y coordinate of circle center
//
//		double deltaY = mY - coordinates.get(coordinates.size() - 1).getY();
//		double deltaX = mX - coordinates.get(coordinates.size() - 1).getX();
//		int radius = (int) Math.sqrt(deltaY * deltaY + deltaX * deltaX);
//
//		double begingAngle = calcAngle(mX, mY, coordinates.get(coordinates.size() - 1).getX(),
//				coordinates.get(coordinates.size() - 1).getY());
//		double targetAngle = calcAngle(mX, mY, x2, y2);
//
//		if (circleDirection) { // gegen den Uhrzeigersinn
//			for (double alpha = begingAngle * 180 / Math.PI; alpha < targetAngle * 180 / Math.PI; alpha++) {
//				int x = (int) (mX + radius * Math.cos(alpha * Math.PI / 180));
//				int y = (int) (mY - radius * Math.sin(alpha * Math.PI / 180));
//
//				coordinates.add(new Coordinates(x, y, true));
//				// System.out.println("( "+ x + " / " + y + " )");
//			}
//		} else { // im Uhrzeigersinn
//			for (double alpha = begingAngle * 180 / Math.PI; alpha > (targetAngle * 180 / Math.PI - 360); alpha--) {
//				int x = (int) (mX + radius * Math.cos(alpha * Math.PI / 180));
//				int y = (int) (mY - radius * Math.sin(alpha * Math.PI / 180));
//
//				coordinates.add(new Coordinates(x, y, true));
//				// System.out.println("( "+ x + " / " + y + " )");
//			}
//		}
//	}
//
////	Simon - Randfälle müssen nochmal überarbeitet werden -> M gleich Position muss abgefangen werden
//	private double calcAngle(double mX, double mY, double posX, double posY) {
//		// rechts von der Y-Achse
//		if (mX < posX) {
//			if (posY < mY) { // Oberhalb der X-Achse
//				return (Math.atan((mY - posY) / posX - mX));
//			} else if (mY < posY) { // Unterhalb der X-Achse
//				return (2 * Math.PI - Math.atan((posY - mY) / (posX - mX)));
//			}
//		} else if (posX < mX) { // Links von der Y-Achse
//			if (posY < mY) { // Oberhalb der X-Achse
//				return (Math.PI - Math.atan((mY - posY) / (mX - posX)));
//			} else if (mY < posY) {// Unterhalb der X-Achse
//				return (Math.PI + Math.atan((posY - mY) / (mX - posX)));
//			}
//		} else if (mX == posX) {// Auf der Y-Achse
//			if (posY < mY) { // Oberhalb der X-Achse
//				return (0.5 * Math.PI);
//			} else { // Unterhalb der X-Achse
//				return (1.5 * Math.PI);
//			}
//		} else if (mY == posY) { // Auf der X-Achse
//			if (posX < mX) { // Links von Y-Achse
//				return Math.PI;
//			}
//		}
//		return 0;
//	}
//
//}
