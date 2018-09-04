package de.heuboe.ausbildung.DijkstraAlgorithm;

import java.util.*;

import org.geotools.referencing.*;
import org.opengis.referencing.*;
import org.opengis.referencing.crs.*;
import org.opengis.referencing.operation.*;

import de.heuboe.ausbildung.subwayPlan.process.*;
import de.heuboe.data.*;
import de.heuboe.data.Factory;
import de.heuboe.data.shp.*;
import de.heuboe.geo.*;
import de.heuboe.geo.data.*;
import de.heuboe.geo.impl.*;
import de.heuboe.geo.utils.*;

public class Rarefaction {
    private int dstSrid;
    private int srcSrid;
    private GeometryFactory geoFactory = new DefaultGeometryFactory();
    private Type type;
    private DataWriter writer;
    private Properties props = new Properties();
    private Factory factory = Factory.Singleton.getInstance();
    private Coordinate startPoint;
    private Coordinate endPoint;
    private List<Coordinate> allPoints;
    private double maxAbstand;

    public Rarefaction(Coordinate startPoint, Coordinate endPoint, List<Coordinate> allPoints, double maxAbstand) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.allPoints = allPoints;
        this.maxAbstand = maxAbstand;
    }

    private Line setNewLine(Coordinate startPoint, Coordinate endPoint) {
        Line line = new Line(startPoint, endPoint);
        line.setGerade(getLine(startPoint, endPoint));
        line.setWeight(getLength(startPoint, endPoint));

        return line;
    }

    private List<Coordinate> getPointsFromLine(List<Coordinate> allPoints, Coordinate startPoint, Coordinate endPoint) {
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

    public List<Coordinate> run() throws FactoryException {
        List<Coordinate> allPointsCopy = allPoints;
        Line line = setNewLine(startPoint, endPoint);

        List<Coordinate> newLine = new ArrayList<>();

        Map<Coordinate, Double> distances = new LinkedHashMap<>();

        distances = getDistances(allPointsCopy, line, distances);

        while (checkDistances(distances, allPointsCopy, maxAbstand) && line.getWeight() > maxAbstand) {
            Coordinate extraPoint = getMaxDistance(allPointsCopy, distances);

            line = setNewLine(startPoint, extraPoint);
            newLine.add(line.getStartPoint());
            newLine.add(line.getEndPoint());

            allPointsCopy = getPointsFromLine(allPoints, line.getStartPoint(), line.getEndPoint());
            distances = new LinkedHashMap<>();

            distances = getDistances(allPointsCopy, line, distances);

            line = setNewLine(extraPoint, endPoint);
            newLine.add(line.getStartPoint());
            newLine.add(line.getEndPoint());

            allPointsCopy = getPointsFromLine(allPoints, line.getStartPoint(), line.getEndPoint());

            distances = getDistances(allPointsCopy, line, distances);
        }
        return newLine;
    }

    private static double getLength(Coordinate point1, Coordinate point2) {
        double deltaX = Tools.getDelta(point1.getX(), point2.getX());
        double deltaY = Tools.getDelta(point1.getY(), point2.getY());

        deltaX *= deltaX;
        deltaY *= deltaY;

        double sum = deltaX + deltaY;

        return Math.sqrt(sum);
    }

    // returns den Punkt wo der Abstand am größten ist. Das wird dann unser extra
    // Punkt sein
    private static Coordinate getMaxDistance(List<Coordinate> allPoints, Map<Coordinate, Double> distances) {
        Double max = null;
        Coordinate maxPoint = null;
        for (Coordinate point : allPoints) {
            if (distances.get(point) != null) {
                if (max == null || distances.get(point) > max) {
                    max = distances.get(point);
                    maxPoint = point;
                }
            }
        }
        return maxPoint;
    }

    // guckt, ob die, schon gemessenen, Abstände größer sind als unser maximaler
    // Abstand
    private static boolean checkDistances(Map<Coordinate, Double> distances, List<Coordinate> allPoints,
            double maxAbstand) {
        for (Coordinate point : allPoints) {
            if (distances.get(point) != null) {
                if (distances.get(point) > maxAbstand) {
                    return true;
                }
            }
        }
        return false;
    }

    // Alle Abstände berechnen und in einer Map, mit dem Punkt als key, speichern
    public static Map<Coordinate, Double> getDistances(List<Coordinate> allPoints, Line line,
            Map<Coordinate, Double> distances) {
        for (Coordinate coordinate : allPoints) {
            if (coordinate != line.getEndPoint() && coordinate != line.getStartPoint()
                    && coordinate.getY() != line.getEndPoint().getY()
                    && coordinate.getY() != line.getStartPoint().getY()) {
                double distance = calculateDistance(coordinate, line);
                distances.put(coordinate, distance);
            }
        }
        return distances;
    }

    private static CoordinateImpl getVector(Coordinate p1, Coordinate p2) {
        double x = p2.getX() - p1.getX();
        double y = p2.getY() - p1.getY();
        return new CoordinateImpl(x, y);
    }

    public static double getBetrag(Coordinate p) {
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
        double betragNenner = getBetrag(line.getGerade()[1]);

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

    public void outputPoints(int dstSrid, int srcSrid, String path, List<Coordinate> allPoints)
            throws FactoryException {
        this.dstSrid = dstSrid;
        this.srcSrid = srcSrid;

        new ShpFactory();
        Map<String, Type> members = new LinkedHashMap<>();

        type = factory.getType("shp", "Linie", members, "", "");

        props.put("de.heuboe.data.shp.geotype", "MULTI_POLYLINE");
        props.put("de.heuboe.data.shp.srid", "" + srcSrid);

        Coordinate[] coords = new Coordinate[allPoints.size()];
        coords = getCoords(allPoints, coords);
        coords = transform(coords);

        DataStore store = factory.createNewDataStore(type, path, props);
        writer = store.getWriter();

        for (int i = 1; i < coords.length; i++) {
            GeoData record = (GeoData) type.createData();
            // Koordinaten
            List<Coordinate> coordinates = new ArrayList<>();
            coordinates.add(coords[i - 1]);
            coordinates.add(coords[i]);
            Geometry line = geoFactory.createPolyline(coordinates, dstSrid);
            record.setGeometry(line);
            writer.add(record);
        }
        writer.close();

    }

    private Coordinate[] getCoords(List<Coordinate> allPoints, Coordinate[] coords) {
        int count = 0;
        for (Coordinate point : allPoints) {
            double x = point.getX();
            double y = point.getY();
            coords[count] = new CoordinateImpl(x, y);
            count++;
        }
        return coords;
    }

    public void outputLine(int dstSrid, int srcSrid, String path, List<Coordinate> newLine) throws FactoryException {
        this.dstSrid = dstSrid;
        this.srcSrid = srcSrid;

        new ShpFactory();
        Map<String, Type> members = new LinkedHashMap<>();

        type = factory.getType("shp", "Linie", members, "", "");

        props.put("de.heuboe.data.shp.geotype", "MULTI_POLYLINE");
        props.put("de.heuboe.data.shp.srid", "" + srcSrid);

        Coordinate[] coords = new Coordinate[newLine.size()];
        coords = getCoords(newLine, coords);
        coords = transform(coords);

        DataStore store = factory.createNewDataStore(type, path, props);
        writer = store.getWriter();

        for (int i = 1; i < coords.length; i++) {
            GeoData record = (GeoData) type.createData();
            // Koordinaten
            List<Coordinate> coordinates = new ArrayList<>();
            coordinates.add(coords[i - 1]);
            coordinates.add(coords[i]);
            Geometry line = geoFactory.createPolyline(coordinates, dstSrid);
            record.setGeometry(line);
            writer.add(record);
        }
        writer.close();
    }

    private Coordinate[] transform(Coordinate[] coords) throws FactoryException {
        CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG: " + srcSrid);
        CoordinateReferenceSystem targetCRS = CRS.decode("EPSG: " + dstSrid);
        MathTransform mt = CRS.findMathTransform(sourceCRS, targetCRS, true);
        CoordinateTransformer ct = new TransformerImpl(mt);

        return ct.transform(coords);
    }
}
