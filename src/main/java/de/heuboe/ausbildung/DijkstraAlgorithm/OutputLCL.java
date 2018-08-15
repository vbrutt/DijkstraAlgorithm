package de.heuboe.ausbildung.DijkstraAlgorithm;

import java.util.*;

import org.opengis.referencing.*;
import de.heuboe.ausbildung.netzplan.interfaces.*;
import de.heuboe.data.*;
import de.heuboe.data.Factory;
import de.heuboe.data.shp.*;
import de.heuboe.geo.*;
import de.heuboe.geo.data.*;
import de.heuboe.geo.impl.*;
import de.heuboe.geo.utils.*;

public class OutputLCL {
    private int dstSrid;
    private int srcSrid;
    private GeometryFactory geoFactory = new DefaultGeometryFactory();
    private Type type;
    private DataWriter writer;

    private static Coordinate twist(Coordinate coord) {
        return new CoordinateImpl(coord.getY(), coord.getX());
    }

    public OutputLCL(String path, int srcSrid, int dstSrid) throws NoSuchAuthorityCodeException, FactoryException {
        this.dstSrid = dstSrid;
        this.srcSrid = srcSrid;

        new ShpFactory();
        Factory factory = Factory.Singleton.getInstance();
        Map<String, Type> members = new LinkedHashMap<String, Type>();

        // Lege Attribut-Spalten an
        members.put("RoadNo", factory.getStringType(64));
        members.put("LocCode", factory.getStringType(64));
        members.put("Type", factory.getStringType(64));
        type = factory.getType("shp", "Linie", members, "", ""); // Warum sind die key indexes "random" gemacht?

        Properties props = new Properties();
        props.put("de.heuboe.data.shp.geotype", "MULTI_POLYLINE");
        props.put("de.heuboe.data.shp.srid", "" + dstSrid); // "Art" des Koordinatensystems

        DataStore store = factory.createNewDataStore(type, path, props);
        writer = store.getWriter();
    }

    private void closeWriter() {
        writer.close();
    }

    private void writeShapeFile(Road road) throws NoSuchAuthorityCodeException, FactoryException {
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
            List<Coordinate> coordinates = new ArrayList<Coordinate>();
            if (dstSrid == srcSrid) {
                double x = p1.getX();
                double y = p1.getY();
                coords[0] = new CoordinateImpl(x, y);
                coordinates.add(twist(coords[0]));
                x = p2.getX();
                y = p2.getY();
                coords[1] = new CoordinateImpl(x, y);
                coordinates.add(twist(coords[1]));

            } else {
                coordinates.add(twist(coords[0]));
                coordinates.add(twist(coords[1]));
            }

            Geometry line = geoFactory.createPolyline(coordinates, dstSrid);
            record.setGeometry(line);
            writer.add(record);
        }
    }

    public void writeShapeFile(Set<Road> roads) throws NoSuchAuthorityCodeException, FactoryException {
        for (Road road : roads) {
            writeShapeFile(road);
        }
        closeWriter();
    }
}
