package de.heuboe.ausbildung.DijkstraAlgorithm;

import java.util.*;

import org.geotools.referencing.*;
import org.opengis.referencing.*;
import org.opengis.referencing.crs.*;
import org.opengis.referencing.operation.*;

import de.heuboe.data.*;
import de.heuboe.data.Factory;
import de.heuboe.data.shp.*;
import de.heuboe.geo.*;
import de.heuboe.geo.data.*;
import de.heuboe.geo.impl.*;
import de.heuboe.geo.utils.*;
import de.heuboe.map.datatypes.*;

public class Rarefaction {
    private int dstSrid;
    private int srcSrid;
    private GeometryFactory geoFactory = new DefaultGeometryFactory();
    private Type type;
    private DataWriter writer;
    private Properties props = new Properties();
    private Factory factory = Factory.Singleton.getInstance();

    public static void main(String[] args) throws FactoryException {
        Point p1 = new Point(-2.0, -3.0);
        Point p2 = new Point(0.0, -3.0);
        Point p3 = new Point(2.0, -2.0);
        Point p4 = new Point(3.0, 0.0);
        Point p5 = new Point(2.0, 2.0);
        Point p6 = new Point(0.0, 3.0);
        Point p7 = new Point(-2.0, 3.0);

        // Die Liste ist sortiert
        List<Point> allPoints = new ArrayList<>();
        allPoints.add(p1);
        allPoints.add(p2);
        allPoints.add(p3);
        allPoints.add(p4);
        allPoints.add(p5);
        allPoints.add(p6);
        allPoints.add(p7);

        // Coordinate statt Point (?)
        Line line = getLine(p1, p7);
        Map<Point, Double> distances = getDistances(allPoints, line);

        double maxAbstand = 1;

        boolean check = checkDistances(distances, allPoints, maxAbstand);
        Point p = getMaxDistance(allPoints, distances);

        Rarefaction rarefaction = new Rarefaction();
        // rarefaction.outputLine(4326, 31463, "./ShapeFiles/LineTEST.shp", linesList);
        rarefaction.outputPoints(4326, 31463, "./ShapeFiles/Points.shp", allPoints);
    }

    private static Point getMaxDistance(List<Point> allPoints, Map<Point, Double> distances) {
        Double max = null;
        Point maxPoint = null;
        for (Point point : allPoints) {
            if (distances.get(point) != null) {
                if (max == null || distances.get(point) > max) {
                    max = distances.get(point);
                    maxPoint = point;
                }
            }
        }
        return maxPoint;

    }

    private static boolean checkDistances(Map<Point, Double> distances, List<Point> allPoints, double maxAbstand) {
        for (Point point : allPoints) {
            if (distances.get(point) != null) {
                if (distances.get(point) > maxAbstand) {
                    return true;
                }
            }
        }
        return false;
    }

    // Alle Abstände berechnen und in einer Map speichern
    public static Map<Point, Double> getDistances(List<Point> allPoints, Line line) {
        Map<Point, Double> distances = new HashMap<>();

        for (Point point : allPoints) {
            if (point != line.getStartPoint() && point != line.getEndPoint()
                    && point.getY() != line.getStartPoint().getY()) {
                distances.put(point, calculateDistance(point, line));
            }
        }
        return distances;
    }

    private static CoordinateImpl getVector(Point p1, Point p2) {
        double x = p2.getX() - p1.getX();
        double y = p2.getY() - p1.getY();
        return new CoordinateImpl(x, y);
    }

    public static double getBetrag(CoordinateImpl produkt) {
        double xQuadrat = Math.pow(produkt.getX(), 2);
        double yQuadrat = Math.pow(produkt.getY(), 2);

        return Math.sqrt(xQuadrat + yQuadrat);
    }

    public static double calculateDistance(Point point, Line line) {
        CoordinateImpl vectorRed = getVector(point, line.getStartPoint());
        CoordinateImpl point1 = new CoordinateImpl(point.getX(), point.getY(), 0);
        CoordinateImpl produkt = produktRegel(vectorRed, point1);
        
        double betragOben = getBetrag(point1);
        double betragUnten = getBetrag(produkt);
        
        return betragOben / betragUnten;
    }

    public static CoordinateImpl produktRegel(CoordinateImpl vectorRed, CoordinateImpl point1) {
        double x = (point1.getX() * vectorRed.getX()) + (vectorRed.getY() * point1.getY());
        // double y = (point1.getZ() * vectorRed.getX()) - (point1.getX() *
        // vectorRed.getZ());
        return new CoordinateImpl(x, 0, 0);

    }

    // Gerade zwischen Start- und Endpunkt bauen
    public static Line getLine(Point p1, Point p2) {
        // List<Point> linePoints = new ArrayList<Point>();
        // linePoints.add(p1);
        // linePoints.add(p2);
        //
        // return new Line(linePoints);

        return new Line(p1, p2);
    }

    private void outputPoints(int dstSrid, int srcSrid, String path, List<Point> points) throws FactoryException {
        this.dstSrid = dstSrid;
        this.srcSrid = srcSrid;

        new ShpFactory();
        Map<String, Type> members = new LinkedHashMap<>();

        type = factory.getType("shp", "Linie", members, "", "");

        props.put("de.heuboe.data.shp.geotype", "MULTI_POLYLINE");
        props.put("de.heuboe.data.shp.srid", "" + srcSrid);

        Coordinate[] coords = new Coordinate[points.size()];
        coords = getCoords(points, coords);
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

    private Coordinate[] getCoords(List<Point> points, Coordinate[] coords) {
        int count = 0;
        for (Point point : points) {
            double x = point.getX();
            double y = point.getY();
            coords[count] = new CoordinateImpl(x, y);
            count++;
        }
        return coords;
    }

    public void outputLine(int dstSrid, int srcSrid, String path, List<Line> lines) throws FactoryException {
        this.dstSrid = dstSrid;
        this.srcSrid = srcSrid;

        new ShpFactory();
        Map<String, Type> members = new LinkedHashMap<>();

        type = factory.getType("shp", "Linie", members, "", "");

        props.put("de.heuboe.data.shp.geotype", "MULTI_POLYLINE");
        props.put("de.heuboe.data.shp.srid", "" + srcSrid);

        Coordinate[] coords = new Coordinate[lines.size() * 2];
        // coords = getCoords(lines, coords);
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

    // private Coordinate[] getCoords(Collection<org.geotools.math.Line> pathAlg,
    // Coordinate[] coords) {
    // int count = 0;
    //
    // for (org.geotools.math.Line linie : pathAlg) {
    // for (Point point : ((Line) linie).getLineAsList()) {
    // double x = point.getX();
    // double y = point.getY();
    // coords[count] = new CoordinateImpl(x, y);
    // count++;
    // }
    // }
    // return coords;
    // }

    private Coordinate[] transform(Coordinate[] coords) throws FactoryException {
        CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG: " + srcSrid);
        CoordinateReferenceSystem targetCRS = CRS.decode("EPSG: " + dstSrid);
        MathTransform mt = CRS.findMathTransform(sourceCRS, targetCRS, true);
        CoordinateTransformer ct = new TransformerImpl(mt);

        return ct.transform(coords);
    }
}
