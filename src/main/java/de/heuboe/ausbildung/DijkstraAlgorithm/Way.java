package de.heuboe.ausbildung.DijkstraAlgorithm;

import java.io.*;
import java.util.*;

import org.opengis.referencing.*;

public class Way {
    private List<Node> nodes = new ArrayList<>();
    private Set<Node> visitedNodes = new HashSet<>();
    private Set<Node> allVisitedNodes = new HashSet<>();
    private Map<Node, Edge> predecessor = new HashMap<>();
    private Node targetNode;
    private Node initialNode;
    private double duration;
    private double distance;

    /**
     * Calls the input class, which filters the LCL file. That means, that the graph
     * only contains highways
     * 
     * @param initialNodeId
     *            Location code from the origin node
     * @param targetNodeId
     *            Location code from the destination node
     * @param n
     *            1 will return the shortest way and 2 will return the quickest way
     * @throws IOException
     * @throws FactoryException
     */
    public Way(String initialNodeId, String targetNodeId, int n) throws IOException, FactoryException {
        Node.setGeneralDistance(n);

        Graph graph = InputAlles.getGraphFormLCL("C:\\Users\\verab\\Documents\\Dijkstra-Algorithmus\\LCL16.0.D.csv");
        this.initialNode = getNode(initialNodeId);
        this.targetNode = getNode(targetNodeId);
        this.nodes = graph.getNodes();
    }

    /**
     * Calls the input class, which contains all entries from the LCL file.
     * 
     * @param n
     *            1 will return the sortest way and 2 will return the quickest way
     * @param initialNodeId
     *            Location code from the origin node
     * @param targetNodeId
     *            Location code from the destination node
     * @throws IOException
     * @throws FactoryException
     */
    public Way(int n, String initialNodeId, String targetNodeId) throws IOException, FactoryException {
        Node.setGeneralDistance(n);

        Graph graph = InputJunctions
                .getGraphFormLCL("C:\\Users\\verab\\Documents\\Dijkstra-Algorithmus\\LCL16.0.D.csv");
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
        if (edge.getDestination().getDistanceTarget() > node.getDistanceTarget()) {
            // if (edge.getDestination().getDistanceType() > node.getDistanceType()) {
            setDistance(edge, node);
        }
    }

    /**
     * Sets the distance for this node as well as the predecessor
     * 
     * @param edge
     *            as predecessor
     * @param node
     *            current node
     */
    private void setDistance(Edge edge, Node node) {
        edge.getDestination().setDistance(edge, node);
        predecessor.put(edge.getDestination(), edge);
        visitedNodes.add(edge.getDestination());
        allVisitedNodes.add(edge.getDestination());
    }

    /**
     * Chooses which method should be called depending if the distance is still set
     * to infinity or not
     * 
     * @param edge
     * @param node
     * @param value
     *            returns true if the distance value is infinity
     */
    private void evaluateDistances(Edge edge, Node node, boolean value) {
        if (value) {
            edge.getDestination().setDistanceTarget(edge.getDestination(), targetNode);
            // if ((edge.getDestination().getDistanceTarget() <= node.getDistanceTarget()))
            // {
            setDistance(edge, node);
            // }
        } else {
            distanceUpdate(edge, node);
        }
    }
   

    /**
     * Only considers the neighbours, which haven't been marked already
     * 
     * @param node
     *            current node
     */
    private void checkNeighbours(Node node) {
        for (Edge edge : node.getEdges()) {
            if (!(edge.getDestination().isChecked())) {
                evaluateDistances(edge, node, edge.getDestination().getDistanceType().isInfinite());
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
            node.setDistanceTarget(node, targetNode);
            if (min == null || node.getDistanceType() < min) {
                minNode = node;
                minNode.setDistanceTarget(minNode, targetNode);
                min = minNode.getDistanceType();
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
            node.setChecked(false);
            predecessor.put(node, null);
        }
        initialNode.setDistance(0.0);
        visitedNodes.clear();
        visitedNodes.add(initialNode);
        allVisitedNodes.clear();
        allVisitedNodes.add(initialNode);
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
     * Runs the algorithm until the target node is reached
     * 
     * @return the path as list
     */
    public List<Node> run() {
        initialize();

        while (!(visitedNodes.isEmpty())) {
            Node currentNode = getMinimalNode();
            visitedNodes.remove(currentNode);
            if (currentNode != null && !(currentNode == targetNode && canTerminate(currentNode))) {
                checkNeighbours(currentNode);
                currentNode.setChecked(true);
                continue;
            }
            visitedNodes.clear();
        }
        return buildPath();
    }

    public Set<Node> getAllVisitedNodes() {
        return allVisitedNodes;
    }
}