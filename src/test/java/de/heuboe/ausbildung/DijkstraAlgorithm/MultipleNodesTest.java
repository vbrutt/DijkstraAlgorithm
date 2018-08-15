package de.heuboe.ausbildung.DijkstraAlgorithm;

import java.io.*;
import java.util.*;

import org.junit.*;
import org.opengis.referencing.*;
import de.heuboe.ausbildung.netzplan.interfaces.*;
import de.heuboe.ausbildung.netzpan.eingabe.Input;

public class MultipleNodesTest {
    // private static final Logger LOGGER =
    // Logger.getLogger(Input2.class.getName());

    // @Test
    public void newAlgorithm() throws IOException, FactoryException {
        // LOGGER.info("1 - SHORTEST WAY \n2 - QUICKEST WAY"); /* debug, info, warning,
        // error, fatal */
        // Scanner sc = new Scanner(System.in);
        // int n = sc.nextInt();
        // sc.close();

        Way way = new Way("10141", "11104", 1); // 11104 Aachen // 10141 Hamburg // 12903 München-Ost // 11769 Köln //
        List<Node> path = way.run();
        Output.outputLCL(path, way.getDuration());
    }

    @Test
    public void outputLCLTest() throws FactoryException, IOException {
        Net net = Input.getNetFormLCL("C:\\Users\\verab\\Documents\\Dijkstra-Algorithmus\\LCL16.0.D.csv");
        Set<Road> roads = net.getRoads();

        Output2 output = new Output2(31467, 31467);
        output.outputLCL("./ShapeFiles/LCLAusgabe.shp", roads);

    }

    @Test
    public void outputNewPath() throws IOException, FactoryException {
        Way way = new Way("11769", "12903", 1); // 11104 Aachen // 10141 Hamburg // 12903 München-Ost // 11769 Köln //
        List<Node> path = way.run();

        Output2 output = new Output2(31467, 31467);
        output.outputDijkstra("./ShapeFiles/Dijkstra.shp", path);
    }
}
