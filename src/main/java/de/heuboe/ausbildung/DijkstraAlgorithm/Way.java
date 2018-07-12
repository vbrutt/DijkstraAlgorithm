package de.heuboe.ausbildung.DijkstraAlgorithm;

import java.io.*;
import java.util.*;

import org.apache.commons.csv.*;
import org.opengis.referencing.*;

public class Way {
	protected List<Edge> edges = new ArrayList<>();
	protected List<Node> unvisitedNodes = new ArrayList<>();
	protected Map<Node, Node> predecessor = new HashMap<>();
	protected Map<String, Node> nodeMap = new HashMap<>();
	protected Node targetNode;
	protected Node initialNode;
	protected double duration;

	public Node getNode(String id) {
		Node node = Node.nodes.get(id);
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
	 * @param initialNodeId
	 *            ID-number from the first node
	 * @param targetNodeId
	 *            ID-number from the last node
	 */
	public Way(String source, String initialNodeId, String targetNodeId) {
		CSVFormat format = CSVFormat.EXCEL.withHeader().withDelimiter(';');
		List<Edge> edgeList = new ArrayList<>();
		List<Node> nodeList = new ArrayList<>();
		Set<Node> nodeSet = new HashSet<>();

		try (FileReader reader = new FileReader(source)) {
			CSVParser parser = new CSVParser(reader, format);
			for (CSVRecord record : parser.getRecords()) {
				Node node1 = getNode(record.get("von"));
				Node node2 = getNode(record.get("bis"));
				Edge edge = new Edge(node1, node2);

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

	public Way(String initialNodeId, String targetNodeId) throws IOException, FactoryException {
		int n = Input2.chooseTypeOfWay();
	
		
		switch (n) {
		case 1:
			ShortestWay sW = new ShortestWay();
			sW.initialNode = getNode(initialNodeId);
			sW.targetNode = getNode(targetNodeId);
			List<Node> path = sW.run();
			
			Output.outputLCL(path, sW.duration);
			break;

		case 2:
			new Way();
			QuickestWay qW = new QuickestWay(initialNodeId, targetNodeId);
			qW.run();
			break;

		}

	}

	public Way() throws IOException, FactoryException {

		Graph graph = Input2.getNetFormLCL("C:\\Users\\verab\\Documents\\Dijkstra-Algorithmus\\LCL16.0.D.csv");
		this.edges = graph.getEdges();
		this.unvisitedNodes = graph.getNodes(); // TODO ALG Punkte miteinbauen (bis jetzt: 4423 Knoten;
												// soll: 4955)
		
	}

	/**
	 * Intializes the node's distance with infinity and the predecessor with null.
	 * The initial node's distance and duration are set to 0.0
	 */
	protected void initialize() {
		for (Node node : unvisitedNodes) {
			node.setDistance(Double.POSITIVE_INFINITY);
			predecessor.put(node, null);
			node.setDuration(Double.POSITIVE_INFINITY);
		}
		initialNode.setDistance(0.0);
		initialNode.setDuration(0.0);
	}

	protected Edge getEdge(Node origin, Node destination) {
		for (Edge edge : edges) {
			if (edge.getOrigin() == origin && edge.getDestination() == destination
					|| edge.getOrigin() == destination && edge.getDestination() == origin) {
				return edge;
			}
		}
		return null;
	}

	protected void calculateRouteDuration(List<Node> path) {
		for (int i = 0; i < path.size() - 1; i++) {
			Edge e = getEdge(path.get(i), path.get(i + 1));
			duration += ((e.getDistance() / 1000) / e.getSpeedLimit());
		}
	}

	protected List<Node> buildPath() {
		Node node = targetNode;
		List<Node> path = new ArrayList<>();
		path.add(node);

		double distance = 0;
		while (predecessor.get(node) != null) {
			Edge edge = getEdge(node, predecessor.get(node));
			distance += edge.getDistance();
			node = predecessor.get(node);
			path.add(0, node);
		}
		path.get(path.size() - 1).setDistance(distance);
		calculateRouteDuration(path);
		return path;
	}
}
