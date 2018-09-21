package de.heuboe.ausbildung.DijkstraAlgorithm;

import java.util.*;

/**
 * @author verab
 *
 */
public class Line {
    private int id;
    private Node startPoint;
    private Node endPoint;
    private Gerade gerade;
    private boolean biggerDistance;

    public Line(int id, Node p1, Node p2) {
        this.setStartPoint(p1);
        this.setEndPoint(p2);
        this.id = id;
        gerade = new Gerade(p1, p2);
        this.setBiggerDistance(false);
    }

    public Node getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Node startPoint) {
        this.startPoint = startPoint;
    }

    public Node getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(Node endPoint) {
        this.endPoint = endPoint;
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
    private static Node getVector(Node p1, Node p2) {
        double x = p2.getX() - p1.getX();
        double y = p2.getY() - p1.getY();

        return new Node(x, y);
    }

    private static double getAbsoluteValue(Node p) {
        double xQuadrat = Math.pow(p.getX(), 2);
        double yQuadrat = Math.pow(p.getY(), 2);

        return Math.sqrt(xQuadrat + yQuadrat);
    }

    private static double kreuzProdukt(Node p1, Node p2) {
        return Math.abs(p1.getX() * p2.getY() - p2.getX() * p1.getY());
    }

    // berechnet den Abstand zwischen dem gegebenen Punkt und dem Punkt auf der
    // Gerade
    public double calculateDistance(Node point) {
        Node ortsvektor = getVector(point, startPoint);
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

    public boolean isBiggerDistance() {
        return biggerDistance;
    }

    public void setBiggerDistance(boolean biggerDistance) {
        this.biggerDistance = biggerDistance;
    }
}
