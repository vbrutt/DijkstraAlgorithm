package de.heuboe.ausbildung.DijkstraAlgorithm;

import java.util.*;

/**
 * Runs the algorithm in a recursive way
 * 
 * @author verab
 *
 */
public class Recursion {
    private List<Node> allPoints;
    private double maxAbstand;
    private boolean biggerDistance = false;
    private Map<Node, Integer> ids = new HashMap<>();
    private List<Line> finishedLines = new ArrayList<>(); // Linien, die ok sind

    public Recursion(List<Node> allPoints, double maxAbstand) {
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
     */
    private Line setNewLine(Node startPoint, Node endPoint) {
        return new Line(ids.get(startPoint), startPoint, endPoint);
    }

    /**
     * Runs the algorithm until all lines are smaller than the max distance
     * 
     * @param iStartPoint
     *            index of the start point in the nodes' list
     * @param iEndPoint
     *            index of the end point in the nodes' list
     */
    public void run(int iStartPoint, int iEndPoint) {
        biggerDistance = false;
        Line line = setNewLine(allPoints.get(iStartPoint), allPoints.get(iEndPoint));

        setDistances(line, allPoints.subList(iStartPoint, iEndPoint));
        if (biggerDistance) {
            int iExtraPoint = getMaxDistance(iStartPoint, iEndPoint);
            run(iStartPoint, iExtraPoint);
            run(iExtraPoint, iEndPoint);
        } else {
            finishedLines.add(line);
        }
    }

    /**
     * This point will be used to fraction our, already built, line into two lines.
     * The extra point inbetween the original line will be this one
     * 
     * @param allPoints
     *            list with all points from the graph
     * @return the index of the point, which has the biggest distance to the line
     */
    private int getMaxDistance(int iStartPoint, int iEndPoint) {
        Double max = null;
        int maxPointIndex = 0;
        for (int i = iStartPoint; i < iEndPoint; i++) {
            Node node = allPoints.get(i);
            double distance = node.getLineDistance();
            if (max == null || distance > max) {
                max = distance;
                maxPointIndex = i;
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
    private void checkDistance(double distance) {
        if (distance > maxAbstand) {
            biggerDistance = true;
        }
    }

    /**
     * Calculates the distance between the line and the points inbetween. Saves
     * those values and points in a map.
     * 
     * @param line
     *            line to be processed
     * @param allPoints
     *            list with all the points from the graph
     */
    private void setDistances(Line line, List<Node> allPoints) {
        for (Node node : allPoints) {
            double distance = line.calculateDistance(node);
            checkDistance(distance);
            node.setLineDistance(distance);
        }
    }

    public List<Line> getFinishedLines() {
        return finishedLines;
    }
}
