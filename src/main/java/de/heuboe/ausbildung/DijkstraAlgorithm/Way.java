package de.heuboe.ausbildung.DijkstraAlgorithm;

import java.io.*;
import java.util.*;

import org.geotools.referencing.operation.transform.*;
import org.opengis.referencing.*;

public class Way {
    private List<Node> nodes = new ArrayList<>();
    private List<Node> visitedNodes = new ArrayList<>();
    private Map<Node, Edge> predecessor = new HashMap<>();
    private Node targetNode;
    private Node initialNode;
    private double duration;
    private double distance;

    public Way(String initialNodeId, String targetNodeId, int n) throws IOException, FactoryException {
        Node.setGeneralDistance(n);

        Graph graph = Input.getNetFormLCL("C:\\Users\\verab\\Documents\\Dijkstra-Algorithmus\\LCL16.0.D.csv");
        this.initialNode = getNode(initialNodeId);
        this.targetNode = getNode(targetNodeId);
        this.nodes = graph.getNodes();
    }

    public Node getTargetNode() {
        return targetNode;
    }

    public double getDuration() {
        setDuration((getDistance() / 1000) / Edge.SPEEDLIMIT);
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public double getDistance() {
        setDistance(predecessor.get(targetNode).getDestination().getDistance());
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    private Node getNode(String id) {
        Node node = Node.nodes.get(id);
        if (node == null) {
            node = new Node(id);
        }
        return node;
    }

    private void distanceUpdate(Edge edge, Node node) {
        if (edge.getDestination().getDistanceType() > node.getDistanceType()) {
            edge.getDestination().setDistance(edge, node);
            predecessor.put(edge.getDestination(), edge);
            visitedNodes.add(edge.getDestination());
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
                    predecessor.put(edge.getDestination(), edge);
                    visitedNodes.add(edge.getDestination());
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
        for (Node node : visitedNodes) {
            if (min == null || node.getDistanceType() < min) {
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
        for (Node node : nodes) {
            node.setDistance(Double.POSITIVE_INFINITY);
            predecessor.put(node, null);
        }
        initialNode.setDistance(0.0);
        visitedNodes.add(initialNode);

    }

    private List<Node> buildPath() {
        Node node = targetNode;
        List<Node> path = new ArrayList<>();
        path.add(node);

        while (predecessor.get(node) != null) {
            Edge edge = predecessor.get(node);
            node = edge.getOrigin();
            path.add(0, node);
        }
        return path;
    }

    /**
     * @param currentNode
     * @return true if the target node is reached and there's no shorter way. Else
     *         returns false and keeps running the algorithm
     */
    private boolean canTerminate(Node currentNode) {
        for (Node node : visitedNodes) {
            if (node.getDistanceType() < currentNode.getDistanceType()) {
                return false;
            }
        }
        targetNode.setChecked(true);
        return true;
    }

    /**
     * runs the algorithm until the target node is reached
     * 
     * @return the path as list
     */
    public List<Node> run() {
        initialize();

        while (!(visitedNodes.isEmpty())) {
            Node currentNode = getMinimalNode();
            visitedNodes.remove(currentNode);
            if (currentNode != null && !(currentNode == targetNode && canTerminate(currentNode))) {
                setDistances(currentNode);
                currentNode.setChecked(true);
                continue;
            }
            visitedNodes.clear();
        }
        return buildPath();
    }
}