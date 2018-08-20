package de.heuboe.ausbildung.DijkstraAlgorithm;

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
     * creates an edge, that has an origin node and a destination node
     * 
     * @param origin
     *            node
     * @param destination
     *            node
     */
    public Edge(Node origin, Node destination) {
        this.origin = origin;
        this.destination = destination;
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

    public int getSpeedLimit() {
        return SPEEDLIMIT;
    }

    public double getDistance() {
        setDistance();
        return distance;
    }

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
}