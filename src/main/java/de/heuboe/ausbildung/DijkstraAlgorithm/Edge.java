package de.heuboe.ausbildung.DijkstraAlgorithm;

import de.heuboe.ausbildung.subwayPlan.process.*;

public class Edge {

	private Node origin;
	private Node destination;
	private String id;
	
	private double distance;
	private double speedLimit;
	// private String roadType;

	public Edge(Node origin, Node destination) {
		this.origin = origin;
		this.destination = destination;
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

	public double getSpeedLimit() {
		return setSpeedLimit(origin.getRoadType());
	}

	public void setSpeedLimit(double speedLimit) {
		this.speedLimit = speedLimit;
	}

	public int setSpeedLimit(String roadType) {
		Road2 r = new Road2();
		return r.getSpeedLimit(roadType);
	}

	public double getDistance() {
		Node a = this.origin;
		Node b = this.destination;

		double deltaX = Tools.getDelta(a.getX(), b.getX());
		double deltaY = Tools.getDelta(a.getY(), b.getY());

		deltaX = Math.pow(deltaX, 2);
		deltaY = Math.pow(deltaY, 2);

		double sum = deltaX + deltaY;
		double distance = Math.sqrt(sum);

		return this.distance = distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}
}
