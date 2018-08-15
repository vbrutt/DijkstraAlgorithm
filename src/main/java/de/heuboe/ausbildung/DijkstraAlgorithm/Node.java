package de.heuboe.ausbildung.DijkstraAlgorithm;

import java.util.*;

import org.apache.commons.csv.*;
import org.apache.commons.lang.*;

import de.heuboe.ausbildung.subwayPlan.interfaces.*;

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
    private String roadType;
    private String name;
    private static int generalDistance;

    protected static Map<String, Node> nodes = new HashMap<>();

    public Node(Junction junction) {
        this.xCoord = junction.getX();
        this.yCoord = junction.getY();
        this.id = junction.getId();
    }

    public Node(String id) {
        this.id = id;
    }

    public Node(CSVRecord record, Junction junction) {
        this.id = record.get("LOCATION CODE");
        setNeighbours(StringUtils.stripStart(record.get("POSITIVE OFFSET"), "0"));
        setNeighbours(StringUtils.stripStart(record.get("NEGATIVE OFFSET"), "0"));
        setRoadType(record.get("(SUB)TYPE"));
        setChecked(false);
        if (!("").equals(record.get("INTERSECTION REFS"))) {
            setIntersections(record.get("INTERSECTION REFS"));
        }
        this.xCoord = junction.getX();
        this.yCoord = junction.getY();
        setName(record.get("FIRST NAME"));
    }

    public static Node getNode(String id, Junction junction) {
        Node node = nodes.get(id);
        if (node == null) {
            node = new Node(junction);
            nodes.put(id, node);
        }
        return node;
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
        generalDistance = i;
    }

    public Double getSortOfDistance() {
        switch (generalDistance) {
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
        edge.getDestination().setDistance((edge.getDistance() / edge.getSpeedLimit()) + node.getDuration());
    }

    /**
     * use of the switch case to decide what should be compared. Either the distance
     * between the two nodes or the duration of the route
     * 
     * @param edge
     * @param node
     */
    public void setDistance(Edge edge, Node node) {
        switch (generalDistance) {
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
     * sets the distance/duration for this node when there's already a concrete
     * value
     * 
     * @param distanceValue
     */
    public void setDistance(double distanceValue) {
        switch (generalDistance) {
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

    public String getRoadType() {
        return roadType;
    }

    public void setRoadType(String roadType) {
        this.roadType = roadType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
