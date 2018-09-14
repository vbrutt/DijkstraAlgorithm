package de.heuboe.ausbildung.DijkstraAlgorithm;

import java.util.*;
import java.util.Map.*;

import de.heuboe.ausbildung.DijkstraAlgorithm.Line;

public class Rarefaction {
    private Node startPoint;
    private Node endPoint;
    private List<Node> allPoints;
    private double maxAbstand;
    private boolean biggerDistance = false;
    private Map<Node, Integer> ids = new HashMap<>();

    public Rarefaction(Node startPoint, Node endPoint, List<Node> allPoints, double maxAbstand) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.allPoints = allPoints;
        this.maxAbstand = maxAbstand;
        int i = 0;
        for (Node point : allPoints) {
            ids.put(point, i++);
        }
    }

    /**
     * Builds a line between start point and end point.
     * 
     * @param startPoint
     *            start point of the line
     * @param endPoint
     *            end point of the line
     * @return a line
     */
    private Line setNewLine(Node startPoint, Node endPoint) {
        return new Line(ids.get(startPoint), startPoint, endPoint);
    }

    /**
     * @param allPoints
     *            list with all the points from the graph
     * @param node1
     * 
     * @param node2
     * 
     * @return all points between the start point and end point
     */
    private static List<Node> getPointsFromLine(List<Node> allPoints, Node node1, Node node2) {
        List<Node> newPoints = new ArrayList<>();
        boolean check = false;
        for (int i = 0; i < allPoints.size(); i++) {
            if (allPoints.get(i) == node1 || allPoints.get(i) == node2 || check) {
                newPoints.add(allPoints.get(i));
                if (allPoints.get(i) == node2) {
                    check = false;
                } else {
                    check = true;
                }
            }
        }
        return newPoints;
    }

    /**
     * @param lines
     *            list with already added lines
     * @param line1
     *            first line
     * @param line2
     *            second line
     * @return list with the two new added lines
     */
    public static List<Line> addNewLines(List<Line> lines, Line line1, Line line2) {
        lines.add(line1);
        lines.add(line2);
        return lines;
    }

    /**
     * Runs the algorithm until all lines are smaller than the max distance
     * 
     * @return a list with the rarefactionfactioned line
     */
    public List<Line> run() {
        List<Node> linePoints;
        List<Line> unfinishedLines = new ArrayList<>(); // Linien, die NICHT ok sind
        List<Line> finishedLines = new ArrayList<>(); // Linien, die ok sind

        Line line = setNewLine(startPoint, endPoint);
        firstCheck(line, unfinishedLines, finishedLines);

        Line line1 = null;
        Line line2 = null;

        for (int i = 0; i < unfinishedLines.size(); i++) {
            biggerDistance = false;
            line = unfinishedLines.get(i);
            linePoints = getPointsFromLine(allPoints, line.getStartPoint(), line.getEndPoint());
            Map<Node, Double> distances = getDistances(linePoints, line);
            Node extraPoint = getMaxDistance(distances);

            line1 = setNewLine(line.getStartPoint(), extraPoint); // Erste Linie bauen
            line2 = setNewLine(extraPoint, line.getEndPoint()); // Zweite Linie bauen
            checkLines(unfinishedLines, finishedLines, line1, line2);
            if (biggerDistance) {
                run(); // Versuch Rekursion einzubauen
            }
        }

        Collections.sort(finishedLines, Line.getIdComparator());
        return finishedLines;
    }

    /**
     * If the distances between the line (from start point and end point) and the
     * points from the graph are smaller than the max distance, then the algorithm
     * is done and this line will be the solution. The solution will be added to the
     * finished lines list.
     * 
     * If the distance is bigger, then the line will be added to the unfisnihed
     * lines list and the algorithm will be run.
     * 
     * @param line
     *            between the start and end point from the graph
     * @param distances
     *            map with the distances between the line and the points
     * @param unfinishedLines
     *            list with lines that still need to be processed
     * @param finishedLines
     *            list with lines, whose distance is smaller than the max distance
     */
    public void firstCheck(Line line, List<Line> unfinishedLines, List<Line> finishedLines) {
        getDistances(allPoints, line);
        if (!(biggerDistance)) {
            finishedLines.add(line);
            return;
        }
        unfinishedLines.add(line);
    }

    /**
     * If the distance between the lines and the points from the graph are bigger
     * than the max distance, then the lines will be added to the unfinished lines
     * list in order for it to be dealt it. Else the lines will be added to the
     * finished line list, which means that the lines' distances are ok
     * 
     * @param unfinishedLines
     *            list with lines, which need to be dealt with
     * @param finishedLines
     *            list with lines, that are ok
     * @param line1
     *            first line
     * @param line2
     *            second line
     */
    private void checkLines(List<Line> unfinishedLines, List<Line> finishedLines, Line line1, Line line2) {
        if (biggerDistance) {
            unfinishedLines.add(line1);
            unfinishedLines.add(line2);
            return;
        }
        finishedLines.add(line1);
        finishedLines.add(line2);
    }

    /**
     * This point will be used to fraction our already built line into two lines.
     * The extra point inbetween the original line will be this one
     * 
     * @param allPoints
     *            a list with all points from the graph
     * @param distances
     *            a map with a point as key and its distance as value
     * @return the point, which has the biggest distance
     */
    private static Node getMaxDistance(Map<Node, Double> distances) {
        Double max = null;
        Node maxPoint = null;
        for (Entry<Node, Double> entry : distances.entrySet()) {
            Node point = entry.getKey();
            double distance = entry.getValue();
            if (max == null || distance > max) {
                max = distance;
                maxPoint = point;
            }
        }
        return maxPoint;
    }

    /**
     * Checks if the distance is bigger than the allowed distance. If so, the
     * "biggerDistance" variable will be set to true.
     * 
     * @param distance
     *            distance value
     */
    private void checkDistance(double distance) {
        if (distance > maxAbstand) {
            biggerDistance = true;
        }
    }

    /**
     * Calculates the distance between the line and the points inbetween. saves
     * those in a map
     * 
     * @param points
     *            list with all points of the graph
     * @param line
     *            line to be processed
     * @param distances
     *            map with all distances
     * @return a map with a point as key and its distance as the value
     */
    private Map<Node, Double> getDistances(List<Node> points, Line line) {
        Map<Node, Double> distances = new HashMap<>();
        for (Node node : points) {
            double distance = line.calculateDistance(node);
            checkDistance(distance);
            distances.put(node, distance);
        }
        return distances;
    }
}
