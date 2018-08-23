package de.heuboe.ausbildung.DijkstraAlgorithm;

import java.util.*;

import org.apache.commons.csv.*;
import org.apache.commons.lang.*;

import de.heuboe.ausbildung.subwayPlan.interfaces.*;
import de.heuboe.ausbildung.subwayPlan.process.*;

/**
 * @author verab
 * 
 *         Describes a node
 *
 */
public class Node {
    private String id;
    private List<Edge> edges = new ArrayList<>();
    private Double distance;
    private Double duration;
    private double xCoord;
    private double yCoord;
    private List<String> neighbours = new ArrayList<>();
    private List<String> intersections = new ArrayList<>();
    private boolean checked;
    private static int distanceType;
    private double distanceTarget;

    protected static Map<String, Node> nodes = new HashMap<>();

    public Node(String id) {
        this.id = id;
    }

    /**
     * Sets the x,y coordinates from the given junction
     * 
     * @param record
     *            CSV record
     * @param junction
     */
    public Node(CSVRecord record, Junction junction) {
        this.id = record.get("LOCATION CODE");
        setNeighbours(StringUtils.stripStart(record.get("POSITIVE OFFSET"), "0"));
        setNeighbours(StringUtils.stripStart(record.get("NEGATIVE OFFSET"), "0"));
        setChecked(false);
        if (!("").equals(record.get("INTERSECTION REFS"))) {
            setIntersections(record.get("INTERSECTION REFS"));
        }
        this.xCoord = junction.getX();
        this.yCoord = junction.getY();

    }

    /**
     * Sets the x,y coordinates from the LCL
     * 
     * @param record
     *            CSV record
     */
    public Node(CSVRecord record) {
        this.id = record.get("LOCATION CODE");
        setNeighbours(StringUtils.stripStart(record.get("POSITIVE OFFSET"), "0"));
        setNeighbours(StringUtils.stripStart(record.get("NEGATIVE OFFSET"), "0"));
        setChecked(false);
        if (!("").equals(record.get("INTERSECTION REFS"))) {
            setIntersections(record.get("INTERSECTION REFS"));
        }

        setXCoord((record.get("LATITUDE")));
        setYCoord(record.get("LONGITUDE"));
    }

    private void setXCoord(String string) {
        try {
            this.setX(getCoordinat(string));
        } catch (NumberFormatException e) {
            this.setX(0);
        }
    }

    private void setYCoord(String string) {
        try {
            this.setY(getCoordinat(string));
        } catch (NumberFormatException e) {
            this.setY(0);
        }
    }

    private double getCoordinat(String date) {
        String replaced = date.replace(',', '.');
        return Double.valueOf(replaced);
    }

    public void setNeighbours(String id) {
        this.neighbours.add(id);
    }

    public List<String> getNeighbours() {
        return neighbours;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

    public void addEdge(Edge edge) {
        if (!(edges.contains(edge))) {
            this.edges.add(edge);
        }
    }

    public static void setGeneralDistance(int i) {
        distanceType = i;
    }

    public Double getDistanceType() {
        switch (distanceType) {
        case 1:
            return getDistance();
        case 2:
            return getDuration();
        default:
            return null;
        }
    }

    public Double getDistance() {
        return distance;
    }

    public void setDist(double distance) {
        this.distance = distance;
    }

    public void shortestDistance(Edge edge, Node node) {
        edge.getDestination().setDist(edge.getDistance() + node.getDistance());
    }

    public void quickestDistance(Edge edge, Node node) {
        edge.getDestination().setDistance((edge.getDistance() / Edge.SPEEDLIMIT) + node.getDuration());
    }

    /**
     * Use of the switch case to decide what should be compared. Either the distance
     * between the two nodes or the duration of the route
     * 
     * @param edge
     * @param node
     */
    public void setDistance(Edge edge, Node node) {
        switch (distanceType) {
        case 1:
            shortestDistance(edge, node);
            break;
        case 2:
            quickestDistance(edge, node);
            break;
        default:
            break;
        }
    }

    /**
     * Sets the distance/duration for this node when there's already a concrete
     * value
     * 
     * @param distanceValue
     */
    public void setDistance(double distanceValue) {
        switch (distanceType) {
        case 1:
            this.distance = distanceValue;
            break;
        case 2:
            this.duration = distanceValue;
            break;
        default:
            break;
        }
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public double getX() {
        return xCoord;
    }

    public void setX(double xCoord) {
        this.xCoord = xCoord;
    }

    public double getY() {
        return yCoord;
    }

    public void setY(double yCoord) {
        this.yCoord = yCoord;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public List<String> getIntersections() {
        return intersections;
    }

    public void setIntersections(String ids) {
        String[] intersectionLocCodes = ids.split(",");
        for (String string : intersectionLocCodes) {
            this.intersections.add(string);
        }
    }

    public double getDistanceTarget() {
        return distanceTarget;
    }

    /**
     * Sets the distance value from the current node to the target node
     * 
     * @param a
     *            current node
     * @param b
     *            target node
     */
    public void setDistanceTarget(Node a, Node b) {
        this.distanceTarget = setDistance(a, b);
    }

    /**
     * Calculates the direct distance between the current node and the target node
     * 
     * @param a
     *            node1
     * @param b
     *            node2
     * @return distance
     */
    public static double setDistance(Node a, Node b) {
        double deltaX = Tools.getDelta(a.getX(), b.getX());
        double deltaY = Tools.getDelta(a.getY(), b.getY());

        deltaX = Math.pow(deltaX, 2);
        deltaY = Math.pow(deltaY, 2);

        double sum = deltaX + deltaY;
        return Math.sqrt(sum);
    }
}