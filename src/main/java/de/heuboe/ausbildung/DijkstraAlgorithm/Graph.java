package de.heuboe.ausbildung.DijkstraAlgorithm;

import java.util.*;

public class Graph {

	private List<Node> nodes;
	private List<Edge> edges;

	public Graph(List<Edge> edges, List<Node> nodes) {
		this.edges = edges;
		this.nodes = nodes;
	}

	public List<Node> getNodes() {
		return nodes;
	}

	public List<Edge> getEdges() {
		return edges;
	}

}
