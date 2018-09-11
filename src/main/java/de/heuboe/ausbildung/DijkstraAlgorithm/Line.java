package de.heuboe.ausbildung.DijkstraAlgorithm;

import java.util.*;

import de.heuboe.geo.*;
import de.heuboe.geo.impl.*;

/**
 * @author verab
 *
 */
public class Line {
    private int id;
    private Coordinate startPoint;
    private Coordinate endPoint;
    private Gerade gerade;  
    private double weight;

    public Line(int id, Coordinate p1, Coordinate p2) {
        this.setStartPoint(p1);
        this.setEndPoint(p2);
        this.id = id;
        gerade = new Gerade(p1, p2);
    }

    public Coordinate getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Coordinate startPoint) {
        this.startPoint = startPoint;
    }

    public Coordinate getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(Coordinate endPoint) {
        this.endPoint = endPoint;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    private static double getAbsoluteValue(Coordinate p) {
        double xQuadrat = Math.pow(p.getX(), 2);
        double yQuadrat = Math.pow(p.getY(), 2);

        return Math.sqrt(xQuadrat + yQuadrat);
    }

    private static double kreuzProdukt(Coordinate p1, Coordinate p2) {
        return Math.abs(p1.getX() * p2.getY() - p2.getX() * p1.getY());
    }

    // berechnet den Abstand zwischen dem gegebenen Punkt und dem Punkt auf der Gerade
    public double calculateDistance(Coordinate point) {
        Coordinate ortsvektor = getVector(point, startPoint);
        double betragZaehler = kreuzProdukt(gerade.getRichtungsvector(), ortsvektor);
        double betragNenner = getAbsoluteValue(gerade.getRichtungsvector());

        return betragZaehler / betragNenner;
    } 
    
    static class IdComparator implements Comparator<Line> {
        @Override
        public int compare(Line l1, Line l2) {
            return Integer.compare(l1.id, l2.id);
        }
    }
    
    static Comparator<Line> getIdComparator() {
        return new IdComparator();
    }
}
