package de.heuboe.ausbildung.DijkstraAlgorithm;

import de.heuboe.map.datatypes.*;

public class Line {
    private double slope;
    private double y0;
    private double x0;
    private Point startPoint;
    private Point endPoint;

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

    public Line(Point p1, Point p2) {
        this.setStartPoint(p1);
        this.setEndPoint(p2);
        setLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
    }

    private void setLine(final double x1, final double y1, final double x2, final double y2) {
        this.slope = (y2 - y1) / (x2 - x1);
        this.x0 = x2 - y2 / slope;
        this.y0 = y2 - slope * x2;
        if (Double.isNaN(x0) && slope == 0) {
            // Occurs for horizontal lines right on the x axis.
            x0 = Double.POSITIVE_INFINITY;
        }
        if (Double.isNaN(y0) && Double.isInfinite(slope)) {
            // Occurs for vertical lines right on the y axis.
            y0 = Double.POSITIVE_INFINITY;
        }
    }
    
    public Point getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Point startPoint) {
        this.startPoint = startPoint;
    }

    public Point getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(Point endPoint) {
        this.endPoint = endPoint;
    }
}
