package de.heuboe.ausbildung.DijkstraAlgorithm;

import java.util.*;

import org.apache.commons.csv.*;

import de.heuboe.ausbildung.subwayPlan.interfaces.*;

public class Node {

	public static int numKnoten = 0;

	private String id;
	private List<Edge> edges = new ArrayList<>();
	private Double distance;
	private Double duration;
	private double xCoord;
	private double yCoord;
	private List<String> predecessors = new ArrayList<>();
	private List<String> successors = new ArrayList<>();
	private List<String> intersections = new ArrayList<>();
	private boolean checked;
	private String roadType;

	public static Map<String, Node> nodes = new HashMap<>();

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
		setSucc(record.get("POSITIVE OFFSET"));
		setPred(record.get("POSITIVE OFFSET"));
		setSucc(record.get("NEGATIVE OFFSET"));
		setPred(record.get("NEGATIVE OFFSET"));
		setRoadType(record.get("(SUB)TYPE"));
		setChecked(false);
		if (!(record.get("INTERSECTION REFS") == "")) {
			setIntersections(record.get("INTERSECTION REFS"));
			// for (String string : intersections) {
			// if (!(string.equals(this.id))) {
			// setSucc(string);
			// setPred(string);
			// }
			// }
		}
		this.xCoord = junction.getX();
		this.yCoord = junction.getY();
	}

	public static Node getNode(String id, Junction junction) {
		Node node = nodes.get(id);
		if (node == null) {
			node = new Node(junction);
			nodes.put(id, node);
		}
		return node;
	}

	public void setPred(String id) {
		this.predecessors.add(id);
	}

	public void setSucc(String id) {
		this.successors.add(id);
	}

	public List<String> getPred() {
		return predecessors;
	}

	public List<String> getSucc() {
		return successors;
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

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
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
}
