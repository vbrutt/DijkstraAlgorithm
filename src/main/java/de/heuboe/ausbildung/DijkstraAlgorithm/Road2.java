package de.heuboe.ausbildung.DijkstraAlgorithm;

public class Road2 {

    private final int a1 = 130; // Autobahn
    private final int b1 = 100; // Bundesstraße
    private final int b2 = 50; // Landesstraße

    public int getSpeedLimit(String roadType) {
        if (("L1.1").equals(roadType) || roadType.startsWith("P")) {
            return a1;
        } else if (("L1.2").equals(roadType)) {
            return b1;
        } else if (("L1.3").equals(roadType)) {
            return b2;
        }
        throw new RuntimeException("Unvalid road type");
    }
}
