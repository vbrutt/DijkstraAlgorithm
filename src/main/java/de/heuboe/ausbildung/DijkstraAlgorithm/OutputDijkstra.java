package de.heuboe.ausbildung.DijkstraAlgorithm;

import java.util.*;
import java.util.Properties;

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

public class OutputDijkstra {
    private static int iSrid = 31463;
    private static String srid = "" + iSrid;
    private static GeometryFactory geoFactory = new DefaultGeometryFactory();

    public static void writePointsToShp(Coordinate[] points, String path) {
        new ShpFactory();
        Factory factory = Factory.Singleton.getInstance();
        Map<String, Type> members = new LinkedHashMap<String, Type>();

        members.put("ID", factory.getStringType(64));
        Type type = factory.getType("shp", "Shapefiles", members, "", "");
        Properties props = new Properties();

        String geotype = "MULTI_POLYLINE";
        props.put("de.heuboe.data.shp.geotype", geotype);
        props.put("de.heuboe.data.shp.srid", srid);
        DataStore store = factory.createNewDataStore(type, path, props);
        DataWriter writer = store.getWriter();

        for (int i = 1; i < points.length; i++) {
            GeoData record = (GeoData) type.createData();
            // Attribute
            record.getMember("ID").setFromString((i - 1) + ":" + i);
            // Koordinaten
            List<Coordinate> coordinates = new ArrayList<Coordinate>();
            coordinates.add(points[i - 1]);
            coordinates.add(points[i]);
            Geometry line = geoFactory.createPolyline(coordinates, iSrid);
            record.setGeometry(line);
            writer.add(record);
        }
        writer.close();
    }

    public static void writeShapeFile(List<Node> pathAlg, String path)
            throws NoSuchAuthorityCodeException, FactoryException {
        // Koordinaten erstellen
        Coordinate[] coords = new Coordinate[pathAlg.size()];
        int i = 0;
        for (Node node : pathAlg) {
            double x = node.getX();
            double y = node.getY();

            coords[i] = new CoordinateImpl(x, y);
            i++;
        }

        // transformieren
        CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:31463");
        CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:31467");
        MathTransform mt = CRS.findMathTransform(sourceCRS, targetCRS, true);
        CoordinateTransformer ct = new TransformerImpl(mt);
        coords = ct.transform(coords);

        // schreiben
        writePointsToShp(coords, path);
    }

}
