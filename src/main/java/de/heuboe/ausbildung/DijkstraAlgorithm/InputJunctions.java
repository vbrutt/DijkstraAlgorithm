package de.heuboe.ausbildung.DijkstraAlgorithm;

import java.io.*;
import java.util.*;
import java.util.Map.*;

import org.apache.commons.csv.*;
import org.geotools.referencing.*;
import org.opengis.referencing.*;
import org.opengis.referencing.crs.*;
import org.opengis.referencing.operation.*;

import de.heuboe.geo.*;
import de.heuboe.geo.impl.*;

/**
 * Reads the LCL file and builds a node out of every entry from the CSVRecord
 * 
 * @author verab
 *
 */
public class InputJunctions {
    private static CoordinateTransformer ct;

    private InputJunctions() {
    }

    /**
     * Reads the LCL and fills the graph
     * 
     * @param source
     *            of the LCL file
     * @return a graph with nodes and its edges
     * @throws FactoryException
     * @throws IOException
     */
    public static Graph getGraphFormLCL(String source) throws FactoryException, IOException {
        CSVFormat format = CSVFormat.EXCEL.withHeader().withDelimiter(';');
        try (FileReader reader = new FileReader(source)) {
            CSVParser parser = new CSVParser(reader, format);
            for (CSVRecord record : parser.getRecords()) {
                if(record.get("LATITUDE").isEmpty() || record.get("LONGITUDE").isEmpty()){
                    continue;
                }
                Node node = new Node(record);
                Node.nodes.put(node.getId(), node);
            }
            parser.close();
            reader.close();
        } catch (IOException e) {
            throw e;
        }

        transformCoordinates();
        List<Node> nodes = transform();
        List<Edge> edges = Edge.addEdges(nodes);

        return new Graph(nodes, edges);

    }

    private static List<Node> transform() {
        List<Node> nodes = new ArrayList<>();
        for (Entry<String, Node> entry : Node.nodes.entrySet()) {
            Node node = entry.getValue();

            changeCoordinates(node);
            nodes.add(node);
        }
        return nodes;
    }

    private static void transformCoordinates() throws FactoryException {
        CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:" + 4326); // z.B. 4326
        CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:" + 31467); // z.B. 31467
        MathTransform mt = CRS.findMathTransform(sourceCRS, targetCRS, true);
        ct = new TransformerImpl(mt);
    }

    private static void changeCoordinates(Node node) {
        if ((int) node.getX() != 0 && (int) node.getY() != 0) {
            Coordinate[] coords = ct.transform(createCoordinate(node));
            node.setX(coords[0].getX());
            node.setY(coords[0].getY());
        }
    }

    private static Coordinate createCoordinate(Node node) {
        return new CoordinateImpl(node.getX(), node.getY());
    }
}
