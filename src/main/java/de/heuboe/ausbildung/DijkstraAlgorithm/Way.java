package de.heuboe.ausbildung.DijkstraAlgorithm;

import java.io.*;
import java.util.*;

import org.opengis.referencing.*;

public class Way {
    private List<Edge> edges = new ArrayList<>();
    private List<Node> unvisitedNodes = new ArrayList<>();
    private Map<Node, Node> predecessor = new HashMap<>();
    private Map<String, Node> nodeMap = new HashMap<>();
    private Node targetNode;
    private Node initialNode;
    private double duration;

    public Way(String initialNodeId, String targetNodeId, int n) throws IOException, FactoryException {
        Node.setGeneralDistance(n);

        Graph graph = Input.getNetFormLCL("C:\\Users\\verab\\Documents\\Dijkstra-Algorithmus\\LCL16.0.D.csv");
        this.initialNode = getNode(initialNodeId);
        this.targetNode = getNode(targetNodeId);
        this.edges = graph.getEdges();
        this.unvisitedNodes = graph.getNodes();
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    private Node getNode(String id) {
        Node node = Node.nodes.get(id);
        if (node == null) {
            node = new Node(id);
            nodeMap.put(id, node);
        }
        return node;
    }

    private void distanceUpdate(Edge edge, Node node) {
        if (edge.getDestination().getDistanceType() > node.getDistanceType()) {
            edge.getDestination().setDistance(edge, node);
            predecessor.put(edge.getDestination(), edge.getOrigin());
        }
    }

    /**
     * If the node's neighbours haven't been checked yet, then the distance will be
     * set as well as the predecessor to this node
     * 
     * @param node
     */
    private void setDistances(Node node) {
        for (Edge edge : node.getEdges()) {
            if (!(edge.getDestination().isChecked())) {
                if (edge.getDestination().getDistanceType().isInfinite()) {
                    edge.getDestination().setDistance(edge, node);
                    predecessor.put(edge.getDestination(), edge.getOrigin());
                } else {
                    distanceUpdate(edge, node);
                }
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
                min = node.getDistanceType();
                minNode = node;
            } else if (node.getDistanceType() < min) {
                min = node.getDistanceType();
                minNode = node;
            }
        }
        return minNode;
    }

    /**
     * Intializes the node's distance with infinity and the predecessor with null.
     * The initial node's distance and duration are set to 0.0
     */
    private void initialize() {
        for (Node node : unvisitedNodes) {
            node.setDistance(Double.POSITIVE_INFINITY);
            predecessor.put(node, null);
        }
        initialNode.setDistance(0.0);
    }

    private Edge getEdge(Node origin, Node destination) {
        for (Edge edge : edges) {
            if (edge.getOrigin() == origin && edge.getDestination() == destination
                    || edge.getOrigin() == destination && edge.getDestination() == origin) {
                return edge;
            }
        }
        throw new RuntimeException("Edge doesn't exist");
    }

    private void calculateRouteDuration(List<Node> path) {
        for (int i = 0; i < path.size() - 1; i++) {
            Edge e = getEdge(path.get(i), path.get(i + 1));
            setDuration(getDuration() + ((e.getDistance() / 1000) / e.getSpeedLimit()));
        }
    }

    private List<Node> buildPath() {
        Node node = targetNode;
        List<Node> path = new ArrayList<>();
        path.add(node);

        double distance = 0;
        while (predecessor.get(node) != null) {
            Edge edge = getEdge(predecessor.get(node), node);
            distance += edge.getDistance();
            node = predecessor.get(node);
            path.add(0, node);
        }
        path.get(path.size() - 1).setDist(distance);
        calculateRouteDuration(path);
        return path;
    }

    /**
     * @param currentNode
     * @return true if the target node is reached and there's no shorter way. Else
     *         returns false and keeps running the algorithm
     */
    private boolean canTerminate(Node currentNode) {
        for (Node node : unvisitedNodes) {
            if (node.getDistanceType() < currentNode.getDistanceType()) {
                return false;
            }
        }
        return true;
    }

    /**
     * runs the algorithm until the target node is reached
     * 
     * @return the path as list
     */
    public List<Node> run() {
        initialize();

        while (!(unvisitedNodes.isEmpty())) {
            Node currentNode = getMinimalNode();
            unvisitedNodes.remove(currentNode);
            if (currentNode != null && !(currentNode == targetNode && canTerminate(currentNode))) {
                setDistances(currentNode);
                currentNode.setChecked(true);
                continue;
            }
            unvisitedNodes.clear();
        }
        return buildPath();
    }
}