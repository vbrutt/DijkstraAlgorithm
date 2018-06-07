package de.heuboe.ausbildung.DijkstraAlgorithm;

import java.io.*;
import java.util.*;

import org.apache.commons.csv.*;

public class Algorithmus {

	private List<Edge> edges = new ArrayList<>();
	private List<Node> nodes = new ArrayList<>();
	private List<Node> unvisitedNodes = new ArrayList<>();
	private List<Node> visitedNodes = new ArrayList<>();
	private Map<Node, Node> predecessor = new HashMap<>();
	private Map<String, Node> allenode = new HashMap<>();

	private Node getNode(String id) {
		Node node = allenode.get(id);
		if (node == null) {
			node = new Node(id);
			allenode.put(id, node);
		}
		return node;
	}

	/**
	 * initializes the class, by reading the csv file and setting the edges and
	 * nodes
	 * 
	 * @param source
	 *            from the csv-file
	 */
	public Algorithmus(String source) {
		CSVFormat format = CSVFormat.EXCEL.withHeader().withDelimiter(';');
		List<Edge> edgeList = new ArrayList<>();
		List<Node> nodeList = new ArrayList<>();
		Set<Node> nodeSet = new HashSet<>();

		try (FileReader reader = new FileReader(source)) {
			CSVParser parser = new CSVParser(reader, format);
			for (CSVRecord record : parser.getRecords()) {
				Node node1 = getNode(record.get("von"));
				Node node2 = getNode(record.get("bis"));
				Edge kante = new Edge(node1, node2, record.get("id"), Double.parseDouble(record.get("Abstand")));

				nodeSet.add(node1);
				nodeSet.add(node2);
				edgeList.add(kante);
			}
			nodeList.addAll(nodeSet);

			edgeList = setEdges(edgeList, nodeList);
			this.edges = edgeList;
			this.nodes = nodeList;
			parser.close();
			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		// System.out.println(node.numnode);
	}

	private List<Edge> setEdges(List<Edge> edgeList, List<Node> nodeList) {
		for (Edge edge : edgeList) {
			for (Node node : nodeList) {
				if (edge.getDeparture().equals(node)) {
					node.setEdge(edge);
				}
			}
		}
		return edgeList;
	}

	/**
	 * intializes the nodes. Aside from the star node, all the nodes will be
	 * initialized with infinity as distance and with null as predecessor
	 * 
	 * @param graph
	 * @param startNode
	 */
	private void initialize(Graph graph, Node startNode) {
		for (Node node : graph.getNodes()) {
			if (node.equals(startNode)) {
				node.setDistance(0.0);
				predecessor.put(node, null);
			} else {
				node.setDistance(Double.POSITIVE_INFINITY);
				predecessor.put(node, null);
			}
		}

	}

	/**
	 * gets the neighbours and sets the nodes' distance as well
	 * 
	 * @param node
	 * @return all the neighbours from this node as a list
	 */
	private List<Node> getNeighbours(Node node) {
		List<Node> neighbours = new ArrayList<>();
		if (node.getEdges() == null) {
			return null;
		}
		for (Edge edge : node.getEdges()) {
			if (edge.getDestination().getDistance() == Double.POSITIVE_INFINITY) {
				edge.getDestination().setDistance(node.getDistance() + edge.getDistance());
			}
			neighbours.add(edge.getDestination());
		}
		return neighbours;
	}

	/**
	 * compares the distance's value
	 * 
	 * @param node
	 * @return the way with the smallest distance
	 */
	private double getSmallestWay(Node node) {
		Double min = null;
		for (Edge kante : node.getEdges()) {
			if (min == null) {
				min = kante.getDistance();
			} else {
				if (kante.getDistance() < min) {
					min = kante.getDistance();
				}
			}
		}
		return min + node.getDistance();
	}

	/**
	 * @param neighbours
	 * @return the node with the smallest distance from the neighbours list
	 */
	private Node getMinNode(List<Node> neighbours) {
		Double min = null;
		Node actualNode = null;
		for (Node node : neighbours) {
			if (min == null) {
				min = node.getDistance();
				actualNode = node;
			} else {
				if (node.getDistance() < min) {
					actualNode = node;
				}
			}
		}
		return actualNode;
	}

	/**
	 * @return the node with the smallest distance from the unvisitedNodes list
	 */
	private Node getFirstNode() {
		Double min = null;
		Node actualNode = null;
		for (Node node : unvisitedNodes) {
			if (min == null) {
				min = node.getDistance();
				actualNode = node;
			} else {
				if (node.getDistance() < min) {
					min = node.getDistance();
					actualNode = node;
				}
			}
		}
		return actualNode;
	}

	private double distanzUpdate(Node actualNode, double currentWay, double alternativeWay) {
		List<Node> neighbours = getNeighbours(actualNode);
		if (neighbours.size() > 0) {
			currentWay = getSmallestWay(actualNode);
		}

		if (actualNode.getDistance() == 0.0) {
			Node k = getMinNode(neighbours);
			neighbours.remove(k);
			if (neighbours.size() > 0) {
				alternativeWay = neighbours.get(0).getDistance();
			}
			k.setDistance(currentWay);
			predecessor.put(k, actualNode);
			unvisitedNodes.add(k);

		} else {
			if (alternativeWay < currentWay) {
				// if (checkDistance(alternativeWay)) {
				setVorganger(checkNode(alternativeWay));
				alternativeWay = currentWay;
				// }
			} else {
				predecessor.put(getMinNode(neighbours), actualNode);
			}
		}
		return alternativeWay;
	}

	private void setVorganger(Node node) {
		for (Edge kante : edges) {
			if (kante.getDestination().equals(node) /* && kante.getDistance() == node.getAbstand() */) {
				predecessor.put(node, kante.getDeparture());
				break;
			}
		}
	}

	private Node checkNode(double alternativeWay) {
		for (Node node : nodes) {
			if (node.getDistance() == alternativeWay) {
				unvisitedNodes.add(node);
				return node;
			}
		}
		return null;
	}

	private List<Node> buildPath() {
		Node node = getTargetNode();
		List<Node> path = new ArrayList<>();
		path.add(node);

		while (predecessor.get(node) != null) {
			node = predecessor.get(node);
			path.add(0, node);
		}

		return path;
	}

	private List<Node> run(Algorithmus alg, Node startNode) {
		Graph graph = new Graph(alg.edges, alg.nodes);

		initialize(graph, startNode);
		unvisitedNodes.add(startNode);
		double currentWay = 0.0;
		double alternativeWay = 0.0;

		while (unvisitedNodes.size() > 0) {
			Node actualNode = getFirstNode();
			unvisitedNodes.remove(actualNode);
			visitedNodes.add(actualNode);
			alternativeWay = distanzUpdate(actualNode, currentWay, alternativeWay);
		}
		return buildPath();
	}

	private Node getTargetNode() {
		for (Node node : nodes) {
			if (node.getId().equals("4")) {
				return node;
			}
		}
		return null;
	}

	private Node getFirstnode(String id) {
		for (int i = 0; i < nodes.size() - 1; i++) {
			if (nodes.get(i).getId().equals(id)) {
				return nodes.get(i);
			}
		}
		return null;
	}

	public static void main(String[] args) {
		String source = "C:\\Users\\verab\\Documents\\Dijkstra-Algorithmus\\Kanten_Tebelle.csv";
		Algorithmus alg = new Algorithmus(source);
		Node startNode = alg.getFirstnode("1");

		List<Node> path = alg.run(alg, startNode);
		for (int i = 0; i < path.size(); i++) {
			System.out.print(path.get(i).getId() + ",");
		}
	}
}