package de.heuboe.ausbildung.DijkstraAlgorithm;

public class Edge {

	private Node departure;
	private Node destination;
	private String id;
	private double distance;

	public Edge(Node departure, Node destination, String id, double distance) {
		this.departure = departure;
		this.destination = destination;
		this.id = id;
		this.distance = distance;
	}

	public Node getDeparture() {
		return departure;
	}

	public void setDeparture(Node departure) {
		this.departure = departure;
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

}
