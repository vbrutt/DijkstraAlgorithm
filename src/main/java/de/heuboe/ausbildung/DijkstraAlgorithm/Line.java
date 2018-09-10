package de.heuboe.ausbildung.DijkstraAlgorithm;

import de.heuboe.geo.*;

/**
 * @author verab
 *
 */
public class Line {
    private int id;
    protected static int idCount = 0;
    private Coordinate startPoint;
    private Coordinate endPoint;
    private Coordinate[] gerade;
    private double weight;

    public Line(Coordinate p1, Coordinate p2) {
        this.setStartPoint(p1);
        this.setEndPoint(p2);
        this.id = idCount;
        idCount++;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
