package de.heuboe.ausbildung.DijkstraAlgorithm;

public class Edge {

	private Node origin;
	private Node destination;
	private String id;
	private double distance;
	private double speedLimit;
	private String roadType;

	public Edge(Node origin, Node destination, String id, double distance, String roadType) {
		this.origin = origin;
		this.destination = destination;
		this.id = id;
		this.distance = distance;
		this.setRoadType(roadType);
		this.speedLimit = setSpeedLimit(roadType);
	}

	public Node getOrigin() {
		return origin;
	}

	public void setOrigin(Node origin) {
		this.origin = origin;
	}

	public Node getDestination() {
		return destination;
	}

	public void setDestination(Node destination) {
		this.destination = destination;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public double getSpeedLimit() {
		return speedLimit;
	}

	public void setSpeedLimit(double speedLimit) {
		this.speedLimit = speedLimit;
	}

	public int setSpeedLimit(String roadType) {
		Road r = new Road();
		return r.getSpeedLimit(roadType);
	}

	public String getRoadType() {
		return roadType;
	}

	public void setRoadType(String roadType) {
		this.roadType = roadType;
	}
}
