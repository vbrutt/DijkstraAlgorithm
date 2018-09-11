package de.heuboe.ausbildung.DijkstraAlgorithm;

import java.util.*;
import java.util.Map.*;

import de.heuboe.ausbildung.DijkstraAlgorithm.Line;
import de.heuboe.ausbildung.subwayPlan.process.*;
import de.heuboe.geo.*;
import de.heuboe.geo.impl.*;

public class Rarefaction {
    private Coordinate startPoint;
    private Coordinate endPoint;
    private List<Coordinate> allPoints;
    private double maxAbstand;
    private boolean biggerDistance = false;
    private Map<Coordinate, Integer> ids = new HashMap<>();

    public Rarefaction(Coordinate startPoint, Coordinate endPoint, List<Coordinate> allPoints, double maxAbstand) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.allPoints = allPoints;
        this.maxAbstand = maxAbstand;
        int i = 0;
        for (Coordinate point : allPoints) {
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
    private Line setNewLine(Coordinate startPoint, Coordinate endPoint) {
        Line line = new Line(ids.get(startPoint), startPoint, endPoint);
        return line;
    }

    /**
     * @param allPoints
     *            list with all the points from the graph
     * @param startPoint
     * 
     * @param endPoint
     * 
     * @return all points between the start point and end point
     */
    private static List<Coordinate> getPointsFromLine(List<Coordinate> allPoints, Coordinate startPoint,
            Coordinate endPoint) {
        List<Coordinate> newPoints = new ArrayList<>();
        boolean check = false;
        for (int i = 0; i < allPoints.size(); i++) {
            if (allPoints.get(i) == startPoint || allPoints.get(i) == endPoint || check) {
                newPoints.add(allPoints.get(i));
                if (allPoints.get(i) == endPoint) {
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
        List<Coordinate> linePoints;
        List<Line> unfinishedLines = new ArrayList<>(); // Linien, die NICHT ok sind
        List<Line> finishedLines = new ArrayList<>(); // Linien, die ok sind

        Line line = setNewLine(startPoint, endPoint);
        firstCheck(line, unfinishedLines, finishedLines);

        // TODO id an den Punkten setzen

        Line line1 = null;
        Line line2 = null;

        for (int i = 0; i < unfinishedLines.size(); i++) {
            biggerDistance = false;
            line = unfinishedLines.get(i);
            linePoints = getPointsFromLine(allPoints, line.getStartPoint(), line.getEndPoint());
            Map<Coordinate, Double> distances = getDistances(linePoints, line);
            Coordinate extraPoint = getMaxDistance(distances);

            line1 = setNewLine(line.getStartPoint(), extraPoint); // Erste Linie bauen
            line2 = setNewLine(extraPoint, line.getEndPoint()); // Zweite Linie bauen
            checkLines(unfinishedLines, finishedLines, line1, line2);

        }

        // TODO Methode, die Linien nach ID-Größe sortiert
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
     * @param point1
     *            first point
     * @param point2
     *            second point
     * @return the length between two points
     */
    private static double getLength(Coordinate point1, Coordinate point2) {
        double deltaX = Tools.getDelta(point1.getX(), point2.getX());
        double deltaY = Tools.getDelta(point1.getY(), point2.getY());

        deltaX *= deltaX;
        deltaY *= deltaY;

        return Math.sqrt(deltaX + deltaY);
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
    private static Coordinate getMaxDistance(Map<Coordinate, Double> distances) {
        Double max = null;
        Coordinate maxPoint = null;
        for (Entry<Coordinate, Double> entry : distances.entrySet()) {
            Coordinate point = entry.getKey();
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
     * @param allPoints
     *            list with all points of the graph
     * @param line
     *            line to be processed
     * @param distances
     *            map with all distances
     * @return a map with a point as key and its distance as the value
     */
    private Map<Coordinate, Double> getDistances(List<Coordinate> allPoints, Line line) {
        Map<Coordinate, Double> distances = new HashMap<>();
        for (Coordinate coordinate : allPoints) {
            double distance = line.calculateDistance(coordinate);
            checkDistance(distance);
            distances.put(coordinate, distance);
        }
        return distances;
    }

    // Gerade zwischen Start- und Endpunkt bauen
    private static Coordinate[] getLine(Coordinate p1, Coordinate p2) {
        Coordinate[] gerade = new Coordinate[2];
        // Ortsvektor
        gerade[0] = new CoordinateImpl(p1.getX(), p1.getY());
        // Richtungsvektor
        gerade[1] = new CoordinateImpl(p2.getX() - p1.getX(), p2.getY() - p1.getY());

        return gerade;
    }
}
