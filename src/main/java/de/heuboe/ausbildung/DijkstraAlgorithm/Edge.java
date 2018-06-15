package de.heuboe.ausbildung.DijkstraAlgorithm;

public class Edge {

	private Node origin;
	private Node destination;
	private String id;
	private double distance;

	public Edge(Node origin, Node destination, String id, double distance) {
		this.origin = origin;
		this.destination = destination;
		this.id = id;
		this.distance = distance;
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

}
