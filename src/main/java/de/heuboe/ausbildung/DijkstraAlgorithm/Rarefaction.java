package de.heuboe.ausbildung.DijkstraAlgorithm;

import java.util.*;

import de.heuboe.ausbildung.subwayPlan.process.*;
import de.heuboe.geo.*;
import de.heuboe.geo.impl.*;

public class Rarefaction {
    private Coordinate startPoint;
    private Coordinate endPoint;
    private List<Coordinate> allPoints;
    private double maxAbstand;
    private boolean biggerDistance = false;

    public Rarefaction(Coordinate startPoint, Coordinate endPoint, List<Coordinate> allPoints, double maxAbstand) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.allPoints = allPoints;
        this.maxAbstand = maxAbstand;
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
    private static Line setNewLine(Coordinate startPoint, Coordinate endPoint) {
        Line line = new Line(startPoint, endPoint);
        line.setGerade(getLine(startPoint, endPoint));
        line.setWeight(getLength(startPoint, endPoint));

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
        Map<Coordinate, Double> distances = new HashMap<>();

        unfinishedLines.add(setNewLine(startPoint, endPoint));

        Line line1 = null;
        Line line2 = null;

        for (int i = 0; i < unfinishedLines.size(); i++) {
            biggerDistance = false;
            Line line = unfinishedLines.get(i);
            linePoints = getPointsFromLine(allPoints, line.getStartPoint(), line.getEndPoint());
            distances = getDistances(linePoints, line, distances);

            Coordinate extraPoint = getMaxDistance(linePoints, distances);

            line1 = setNewLine(line.getStartPoint(), extraPoint); // Erste Linie bauen
            line2 = setNewLine(extraPoint, line.getEndPoint()); // Zweite Linie bauen
            checkLines(unfinishedLines, finishedLines, line1, line2);

        }
        return finishedLines;
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
    private static Coordinate getMaxDistance(List<Coordinate> allPoints, Map<Coordinate, Double> distances) {
        Double max = null;
        Coordinate maxPoint = null;
        for (Coordinate point : allPoints) {
            if (distances.get(point) != null && (max == null || distances.get(point) > max)) {
                max = distances.get(point);
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
    private Map<Coordinate, Double> getDistances(List<Coordinate> allPoints, Line line,
            Map<Coordinate, Double> distances) {
        for (Coordinate coordinate : allPoints) {
            double distance = calculateDistance(coordinate, line);
            checkDistance(distance);
            distances.put(coordinate, distance);
        }
        return distances;
    }

    /**
     * @param p1
     *            first point
     * @param p2
     *            second point
     * @return a vector from point 1 and point 2
     */
    private static CoordinateImpl getVector(Coordinate p1, Coordinate p2) {
        double x = p2.getX() - p1.getX();
        double y = p2.getY() - p1.getY();

        return new CoordinateImpl(x, y);
    }

    public static double getAbsoluteValue(Coordinate p) {
        double xQuadrat = Math.pow(p.getX(), 2);
        double yQuadrat = Math.pow(p.getY(), 2);

        return Math.sqrt(xQuadrat + yQuadrat);
    }

    // berechnet den Abstand zwischen dem gegebenen Punkt und dem Punkt auf der
    // Gerade
    public static double calculateDistance(Coordinate point, Line line) {
        Coordinate p1 = new CoordinateImpl(point.getX(), point.getY());
        Coordinate p2 = new CoordinateImpl(line.getGerade()[0].getX(), line.getGerade()[0].getY());
        Coordinate ortsvektor = getVector(p1, p2);
        double betragZaehler = kreuzProdukt(line.getGerade()[1], ortsvektor);
        double betragNenner = getAbsoluteValue(line.getGerade()[1]);

        return betragZaehler / betragNenner;
    }

    public static double kreuzProdukt(Coordinate p1, Coordinate p2) {
        return Math.abs(p1.getX() * p2.getY() - p2.getX() * p1.getY());
    }

    // Gerade zwischen Start- und Endpunkt bauen
    public static Coordinate[] getLine(Coordinate p1, Coordinate p2) {
        Coordinate[] gerade = new Coordinate[2];
        // Ortsvektor
        gerade[0] = new CoordinateImpl(p1.getX(), p1.getY());
        // Richtungsvektor
        gerade[1] = new CoordinateImpl(p2.getX() - p1.getX(), p2.getY() - p1.getY());

        return gerade;
    }
}
