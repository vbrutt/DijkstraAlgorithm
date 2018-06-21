package de.heuboe.ausbildung.DijkstraAlgorithm;

import java.util.*;

public class ShortestWay extends Way {
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
	public ShortestWay(String source, String initialNodeId, String targetNodeId) {
		super(source, initialNodeId, targetNodeId);
	}

	private void distanceUpdate(Edge edge, Node node) {
		if (edge.getDestination().getDistance() > edge.getDistance() + node.getDistance()) {
			edge.getDestination().setDistance(edge.getDistance() + node.getDistance());
			predecessor.put(edge.getDestination(), edge.getOrigin());
		}
	}

	/**
	 * If the the node's neighbours are marked as unvisited, then the distance will
	 * be set as the edge's weight plus the node's distance. The predecessor to this
	 * node will also be set
	 * 
	 * @param node
	 *            the current node
	 */
	private void setDistances(Node node) {
		for (Edge edge : node.getEdges()) {
			if (edge.getDestination().getDistance() == Double.POSITIVE_INFINITY) {
				edge.getDestination().setDistance(edge.getDistance() + node.getDistance());
				predecessor.put(edge.getDestination(), edge.getOrigin());
			} else {
				distanceUpdate(edge, node);
			}
		}
	}

	/**
	 * @return the node with the smallest distance from the unvisited nodes list
	 */

	private Node getMinimalNode() {
		Double min = null;
		Node minNode = null;
		for (Node node : unvisitedNodes) {
			if (min == null) {
				min = node.getDistance();
				minNode = node;
			} else if (node.getDistance() < min) {
				min = node.getDistance();
				minNode = node;
			}
		}
		return minNode;
	}

	/**
	 * @param currentNode
	 * @return true if the distance set in the current node the smallest is.
	 *         Otherwise return false
	 */
	private boolean canTerminate(Node currentNode) {
		for (Node node : unvisitedNodes) {
			if (node.getDistance() < currentNode.getDistance()) {
				return false;
			}
		}
		return true;
	}

	public List<Node> run() {
		initialize();

		while (unvisitedNodes.size() > 0) {
			Node currentNode = getMinimalNode();
			unvisitedNodes.remove(currentNode);
			if (!(currentNode == targetNode && canTerminate(currentNode))) {
				setDistances(currentNode);
				continue;
			}
			unvisitedNodes.clear();
		}
		return buildPath();
	}
}