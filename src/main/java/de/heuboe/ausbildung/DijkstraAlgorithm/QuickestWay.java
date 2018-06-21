package de.heuboe.ausbildung.DijkstraAlgorithm;

import java.util.*;

public class QuickestWay extends Way {

	/**
	 * Initializes the class, by reading the csv-file and setting the edges and
	 * nodes
	 * 
	 * @param source
	 *            from the csv-file
	 * @param initialNodeId
	 *            ID-number from the first node
	 * @param targetNodeId
	 *            ID-number from the last node
	 */
	public QuickestWay(String source, String initialNodeId, String targetNodeId) {
		super(source, initialNodeId, targetNodeId);
	}

	private void distanceUpdate(Edge edge, Node node) {
		if (edge.getDestination().getDuration() > node.getDuration()) {
			edge.getDestination().setDuration((edge.getDistance() / edge.getSpeedLimit()) + node.getDuration());
			edge.getDestination().setDistance(edge.getDistance() + node.getDistance());
			predecessor.put(edge.getDestination(), edge.getOrigin());
		}
	}

	private void setDistances(Node node) {
		for (Edge edge : node.getEdges()) {
			if (edge.getDestination().getDuration() == Double.POSITIVE_INFINITY) {
				edge.getDestination().setDuration(edge.getDistance() / edge.getSpeedLimit());
				edge.getDestination().setDistance(edge.getDistance() + node.getDistance());
				predecessor.put(edge.getDestination(), edge.getOrigin());
			} else {
				distanceUpdate(edge, node);
			}
		}
	}

	/**
	 * @return the node with the smallest duration from the unvisited nodes list
	 */
	private Node getMinimalNode() {
		Double min = null;
		Node minNode = null;
		for (Node node : unvisitedNodes) {
			if (min == null) {
				min = node.getDuration();
				minNode = node;
			} else if (node.getDuration() < min) {
				min = node.getDuration();
				minNode = node;
			}
		}
		return minNode;
	}
	
	public List<Node> run() {
		initialize();

		while (unvisitedNodes.size() > 0) {
			Node currentNode = getMinimalNode();
			unvisitedNodes.remove(currentNode);
			setDistances(currentNode);
		}
		return buildPath();
	}
}
