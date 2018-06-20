package de.heuboe.ausbildung.DijkstraAlgorithm;

public class Road {

	private int a1 = 150;
	private int a2 = 130;
	private int b1 = 100;
	private int b2 = 50;

	public int getSpeedLimit(String roadType) {
		if (roadType.equals("a1")) {
			return a1;
		} else if (roadType.equals("a2")) {
			return a2;
		} else if (roadType.equals("b1")) {
			return b1;
		} else if (roadType.equals("b2")) {
			return b2;
		}
		throw new RuntimeException("Unvalid road type");
	}

}
