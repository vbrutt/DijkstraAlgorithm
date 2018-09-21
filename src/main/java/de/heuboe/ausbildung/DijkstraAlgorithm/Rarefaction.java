package de.heuboe.ausbildung.DijkstraAlgorithm;

import java.util.*;
import java.util.Map.*;

import javax.swing.event.*;

public class Rarefaction {
    private Node startPoint;
    private Node endPoint;
    private List<Node> allPoints;
    private double maxAbstand;
    private boolean biggerDistance = false;
    private Map<Node, Integer> ids = new HashMap<>();
    private List<Line> finishedLines = new ArrayList<>(); // Linien, die ok sind

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
    private static Set<Node> getPointsFromLine(List<Node> allPoints) {
        Set<Node> linePoints = new HashSet<>();

        for (Node node : allPoints) {
            for (String neighbourID : node.getNeighboursAndIntersections()) {
                Node neighbour = Node.nodes.get(neighbourID);
                if (allPoints.contains(neighbour)) {
                    linePoints.add(neighbour);
                }
            }
        }
        return linePoints;
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
     * @return a list with the rarefactioned line
     */
    public void run(int iStartPoint, int iEndPoint) {
        biggerDistance = false;
        Line line = setNewLine(allPoints.get(iStartPoint), allPoints.get(iEndPoint));

        setDistances(line, allPoints.subList(iStartPoint, iEndPoint));
        if (biggerDistance) {
            int iExtraPoint = getMaxDistance(allPoints.subList(iStartPoint, iEndPoint));
            run(iStartPoint, iExtraPoint);
            run(iExtraPoint, iEndPoint);
        } else {
            finishedLines.add(line);
        }
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
        setDistances(line, allPoints);
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
     * @return
     * @return the point, which has the biggest distance
     */
    private int getMaxDistance(List<Node> allPoints) {
        Double max = null;
        int maxPointIndex = 0;
        for (Node node : allPoints) {
            double distance = node.getDistance();
            if (max == null || distance > max) {
                max = distance;
                maxPointIndex = allPoints.indexOf(node);
            }
        }
        return maxPointIndex;
    }

    /**
     * Checks if the distance is bigger than the allowed distance. If so, the
     * "biggerDistance" variable will be set to true.
     * 
     * @param distance
     *            distance value
     */
    private boolean checkDistance(double distance, Line line) {
        if (distance > maxAbstand) {
            biggerDistance = true;
            line.setBiggerDistance(true);
            return true;
        }
        return false;
    }

    /**
     * Calculates the distance between the line and the points inbetween. saves
     * those in a map
     * 
     * @param allPoints2
     *            list with all points of the graph
     * @param line
     *            line to be processed
     * @param distances
     *            map with all distances
     * @return a map with a point as key and its distance as the value
     */
    private void setDistances(Line line, List<Node> allPoints) {
        for (Node node : allPoints) {
            double distance = line.calculateDistance(node);
            checkDistance(distance, line);
            node.setLineDistance(distance);
        }
    }

    public List<Line> getFinishedLines() {
        return finishedLines;
    }
}
