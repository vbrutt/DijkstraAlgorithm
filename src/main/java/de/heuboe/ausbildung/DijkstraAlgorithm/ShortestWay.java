package de.heuboe.ausbildung.DijkstraAlgorithm;

import java.io.*;
import java.util.*;

import org.apache.commons.csv.*;

public class ShortestWay {

	private List<Edge> edges = new ArrayList<>();
	private List<Node> unvisitedNodes = new ArrayList<>();
	private Map<Node, Node> predecessor = new HashMap<>();
	private Map<String, Node> nodeMap = new HashMap<>();
	private Node targetNode;
	private Node initialNode;
	protected double time;

	private Node getNode(String id) {
		Node node = nodeMap.get(id);
		if (node == null) {
			node = new Node(id);
			nodeMap.put(id, node);
		}
		return node;
	}

	/**
	 * Initializes the class, by reading the csv-file and setting the edges and
	 * nodes
	 * 
	 * @param source
	 *            from the csv-file
	 */
	public ShortestWay(String source, String initialNodeId, String targetNodeId) {
		CSVFormat format = CSVFormat.EXCEL.withHeader().withDelimiter(';');
		List<Edge> edgeList = new ArrayList<>();
		List<Node> nodeList = new ArrayList<>();
		Set<Node> nodeSet = new HashSet<>();

		try (FileReader reader = new FileReader(source)) {
			CSVParser parser = new CSVParser(reader, format);
			for (CSVRecord record : parser.getRecords()) {
				Node node1 = getNode(record.get("von"));
				Node node2 = getNode(record.get("bis"));
				Edge edge = new Edge(node1, node2, record.get("id"), Double.parseDouble(record.get("Abstand")),
						record.get("Typ"));

				nodeSet.add(node1);
				nodeSet.add(node2);
				edgeList.add(edge);
			}
			nodeList.addAll(nodeSet);

			setEdges(edgeList);
			this.edges = edgeList;
			this.unvisitedNodes = nodeList;
			this.targetNode = getNode(targetNodeId);
			this.initialNode = getNode(initialNodeId);
			parser.close();
			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adds the edges to the respective nodes
	 * 
	 * @param edgeList
	 */
	private static void setEdges(List<Edge> edgeList) {
		for (Edge edge : edgeList) {
			Node node = edge.getOrigin();
			node.addEdge(edge);
		}
	}

	/**
	 * Intializes the node's distance with infinity and the predecessor with null.
	 * The initial node's distance is set to 0.0
	 */
	private void initialize() {
		for (Node node : unvisitedNodes) {
			node.setDistance(Double.POSITIVE_INFINITY);
			predecessor.put(node, null);
		}
		initialNode.setDistance(0.0);
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

	private Edge getEdge(Node origin, Node destination) {
		for (Edge edge : edges) {
			if (edge.getOrigin() == origin && edge.getDestination() == destination) {
				return edge;
			}
		}
		return null;
	}

	private void calculateRoute(List<Node> path) {
		for (int i = 0; i < path.size() - 1; i++) {
			Edge e = getEdge(path.get(i), path.get(i + 1));
			time += (e.getDistance() / e.getSpeedLimit());
		}
	}

	private List<Node> buildPath() {
		Node node = targetNode;
		List<Node> path = new ArrayList<>();
		path.add(node);

		if (node.getDistance() == Double.POSITIVE_INFINITY) {
			throw new RuntimeException("Keine Verbindung zwischen Startknoten und Endknoten!");
		}

		while (predecessor.get(node) != null) {
			node = predecessor.get(node);
			path.add(0, node);
		}
		calculateRoute(path);
		return path;
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