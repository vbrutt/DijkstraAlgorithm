package de.heuboe.ausbildung.DijkstraAlgorithm;

public class Road2 {

	private int a1 = 130;	//Autobahn
	private int b1 = 100;	//Bundesstraße
	private int b2 = 50;	//Landesstraße

	public int getSpeedLimit(String roadType) {
		if (roadType.equals("L1.1") || roadType.startsWith("P")) {
			return a1;
		} else if (roadType.equals("L1.2")) {
			return b1;
		} else if (roadType.equals("L1.3")) {
			return b2;
		}
		throw new RuntimeException("Unvalid road type");
	}
}
