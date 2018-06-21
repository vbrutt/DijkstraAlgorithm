package de.heuboe.ausbildung.DijkstraAlgorithm;

import java.util.*;

public class Node {

	public static int numKnoten = 0;

	private String id;
	private List<Edge> edges = new ArrayList<>();
	private Double distance;
	private Double duration;

	public Node(String id) {
		numKnoten++;
		this.id = id;
	}

	public Node(String id, Edge edges) {
		numKnoten++;
		this.id = id;
		this.edges.add(edges);
	}

	public Node(String id, List<Edge> edges) {
		numKnoten++;
		this.id = id;
		this.edges = edges;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Edge> getEdges() {
		return edges;
	}

	public void setEdges(List<Edge> edges) {
		this.edges = edges;
	}

	public void addEdge(Edge edge) {
		this.edges.add(edge);
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public Double getDuration() {
		return duration;
	}

	public void setDuration(Double duration) {
		this.duration = duration;
	}
}
