package de.heuboe.ausbildung.DijkstraAlgorithm;

import de.heuboe.geo.*;

public class Line {
    private double slope;
    private double y0;
    private double x0;
    private Coordinate startPoint;
    private Coordinate endPoint;
    private Coordinate[] gerade;
    private double weight;

    public double getSlope() {
        return slope;
    }

    public void setSlope(double slope) {
        this.slope = slope;
    }

    public double getY0() {
        return y0;
    }

    public void setY0(double y0) {
        this.y0 = y0;
    }

    public double getX0() {
        return x0;
    }

    public void setX0(double x0) {
        this.x0 = x0;
    }

    public Line(Coordinate p1, Coordinate p2) {
        this.setStartPoint(p1);
        this.setEndPoint(p2);
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

    public Coordinate[] getGerade() {
        return gerade;
    }

    public void setGerade(Coordinate[] gerade) {
        this.gerade = gerade;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
