package de.heuboe.ausbildung.DijkstraAlgorithm;

import java.util.*;

import de.heuboe.ausbildung.subwayPlan.process.*;

/**
 * Describes an edge composed by two nodes
 * 
 * @author verab
 *
 */
public class Edge {
    private Node origin;
    private Node destination;
    private double distance;
    protected static final int SPEEDLIMIT = 130;

    /**
     * Creates an edge, that has an origin node and a destination node
     * 
     * @param origin
     *            node
     * @param destination
     *            node
     */
    public Edge(Node origin, Node destination) {
        setOrigin(origin);
        setDestination(destination);
    }

    public Node getOrigin() {
        return origin;
    }

    public void setOrigin(Node origin) {
        this.origin = origin;
    }

    public Node getDestination() {
        return destination;
    }

    public void setDestination(Node destination) {
        this.destination = destination;
    }

    public double getDistance() {
        setDistance();
        return distance;
    }

    /**
     * Calculates and sets the distance for this edge.
     * 
     */
    public void setDistance() {
        Node a = this.origin;
        Node b = this.destination;

        double deltaX = Tools.getDelta(a.getX(), b.getX());
        double deltaY = Tools.getDelta(a.getY(), b.getY());

        deltaX = Math.pow(deltaX, 2);
        deltaY = Math.pow(deltaY, 2);

        double sum = deltaX + deltaY;
        this.distance = Math.sqrt(sum);
    }

    /**
     * Adds the edge(s) to their node
     * 
     * @param nodeList
     *            list with all the nodes
     * @return list with all the edges
     */
    public static List<Edge> addEdges(List<Node> nodeList) {
        List<Edge> allEdges = new ArrayList<>();
        for (Node node : nodeList) {
            setNeighbours(node, allEdges);
            setIntersections(node, allEdges);
        }
        return allEdges;
    }

    /**
     * Builds an edge from the node's neighbours and sets it to the node
     * 
     * @param node
     *            actual node
     * @param allEdges
     *            list with all the edges for this node
     */
    public static void setNeighbours(Node node, List<Edge> allEdges) {
        for (String idNeighbour : node.getNeighbours()) {
            Node node1 = Node.nodes.get(idNeighbour);
            addEdge(node1, node, allEdges);
        }
    }

    /**
     * Builds an edge from the node's intersection and sets it to the node
     * 
     * @param node
     *            actual node
     * @param allEdges
     *            list with all the edges for this node
     */
    public static void setIntersections(Node node, List<Edge> allEdges) {
        for (String idIntersection : node.getIntersections()) {
            if (!(idIntersection.equals(node.getId()))) {
                Node node1 = Node.nodes.get(idIntersection);
                addEdge(node1, node, allEdges);
            }
        }
    }

    /**
     * If node1 isn't null, then an edge will be built and added to the list
     * 
     * @param node1
     *            destination node
     * @param node
     *            origin node
     * @param allEdges
     *            list with all edges
     */
    public static void addEdge(Node node1, Node node, List<Edge> allEdges) {
        if (node1 != null) {
            Edge edge = new Edge(node, node1);
            node.addEdge(edge);
            allEdges.add(edge);
        }
    }
}