package de.heuboe.ausbildung.DijkstraAlgorithm;

import java.util.*;

import org.geotools.referencing.*;
import org.opengis.referencing.*;
import org.opengis.referencing.crs.*;
import org.opengis.referencing.operation.*;

import de.heuboe.ausbildung.netzplan.interfaces.*;
import de.heuboe.data.*;
import de.heuboe.data.Factory;
import de.heuboe.data.shp.*;
import de.heuboe.geo.*;
import de.heuboe.geo.data.*;
import de.heuboe.geo.impl.*;
import de.heuboe.geo.utils.*;

public class Output {
    private int dstSrid;
    private int srcSrid;
    private GeometryFactory geoFactory = new DefaultGeometryFactory();
    private Type type;
    private DataWriter writer;
    private Properties props = new Properties();
    private Factory factory = Factory.Singleton.getInstance();

    public Output(int srcSrid, int dstSrid) {
        this.dstSrid = dstSrid;
        this.srcSrid = srcSrid;

        new ShpFactory();
        Map<String, Type> members = new LinkedHashMap<>();

        members.put("RoadNo", factory.getStringType(64));
        members.put("LocCode", factory.getStringType(64));
        members.put("Type", factory.getStringType(64));

        type = factory.getType("shp", "Linie", members, "", "");

        props.put("de.heuboe.data.shp.geotype", "MULTI_POLYLINE");
        props.put("de.heuboe.data.shp.srid", "" + srcSrid);

    }

    private static Coordinate twist(Coordinate coord) {
        return new CoordinateImpl(coord.getY(), coord.getX());
    }

    private void setCoords(Point p1, Point p2, Coordinate[] coords, List<Coordinate> coordinates) {
        double x = p1.getX();
        double y = p1.getY();
        coords[0] = new CoordinateImpl(x, y);
        coordinates.add(twist(coords[0]));
        x = p2.getX();
        y = p2.getY();
        coords[1] = new CoordinateImpl(x, y);
        coordinates.add(twist(coords[1]));
    }

    public void outputLCL(String path, Set<Road> roads) {
        DataStore store = factory.createNewDataStore(type, path, props);
        writer = store.getWriter();

        for (Road road : roads) {
            for (int i = 1; i < road.getUsedPoints().size(); i++) {
                Point p1 = road.getUsedPoints().get(i - 1);
                Point p2 = road.getUsedPoints().get(i);

                GeoData record = (GeoData) type.createData();
                // Fülle Attribut-Spalten
                record.getMember("RoadNo").setFromString(p1.getRoadNumber() + " | " + p2.getRoadNumber());
                record.getMember("LocCode").setFromString(p1.getLocationCode() + " --> " + p2.getLocationCode());
                record.getMember("Type").setFromString(p1.getType() + " --> " + p2.getType());

                // Koordinaten
                Coordinate[] coords = new Coordinate[2];
                List<Coordinate> coordinates = new ArrayList<>();

                setCoords(p1, p2, coords, coordinates);

                Geometry line = geoFactory.createPolyline(coordinates, dstSrid);
                record.setGeometry(line);
                writer.add(record);
            }
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

    private Coordinate[] getCoords(Collection<Node> pathAlg, Coordinate[] coords) {
        int count = 0;
        for (Node node : pathAlg) {
            double x = node.getX();
            double y = node.getY();

            coords[count] = new CoordinateImpl(x, y);
            count++;
        }
        return coords;
    }

    public void outputDijkstra(String path, List<Node> pathAlg) throws FactoryException {
        Coordinate[] coords = new Coordinate[pathAlg.size()];
        coords = getCoords(pathAlg, coords);

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

    public void outputNodes(String path, Set<Node> pathAlg) throws FactoryException {
        Coordinate[] coords = new Coordinate[pathAlg.size()];
        coords = getCoords(pathAlg, coords);
        coords = transform(coords);

        DataStore store = factory.createNewDataStore(type, path, props);
        writer = store.getWriter();

        for (int i = 0; i < coords.length; i++) {
            GeoData record = (GeoData) type.createData();
            // Koordinaten
            Geometry point = geoFactory.createPoint(coords[i], dstSrid);
            record.setGeometry(point);
            writer.add(record);
        }
        writer.close();
    }

}
