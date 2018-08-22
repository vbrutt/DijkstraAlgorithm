package de.heuboe.ausbildung.DijkstraAlgorithm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.*;
import java.util.*;

import org.junit.*;
import org.opengis.referencing.*;

import de.heuboe.ausbildung.netzpan.eingabe.Input;
import de.heuboe.ausbildung.netzplan.interfaces.Net;
import de.heuboe.ausbildung.netzplan.interfaces.Road;

public class OutputTest {
    // @Test
    public void outputLCL() throws FactoryException, IOException {
        Net net = Input.getNetFormLCL("C:\\Users\\verab\\Documents\\Dijkstra-Algorithmus\\LCL16.0.D.csv");
        Set<Road> roads = net.getRoads();

        Output output = new Output(31463, 31467);
        output.outputLCL("./ShapeFiles/LCLAusgabe.shp", roads);
    }

    // @Test
    public void outputDijkstra() throws IOException, FactoryException {
        Way way = new Way("11769", "12903", 1); // 11104 Aachen // 10141 Hamburg // 12903 München-Ost // 11769 Köln //
        List<Node> path = new ArrayList<>();

        long t0 = System.currentTimeMillis();
        int count = 0;
        while (System.currentTimeMillis() - t0 < 15000) {
            count++;
            path = way.run();
        }
        System.out.println("In 15 Sekunden haben wir " + count + " Aufrufe geschafft");

        Output output = new Output(31463, 31467);
        output.outputDijkstra("./ShapeFiles/DijkstraLong.shp", path);
    }

    // @Test
    public void keineVerbindung() throws IOException, FactoryException {
        Way way = new Way("11769", "54969", 1);
        List<Node> path = new ArrayList<>();

        path = way.run();

        assertFalse("Zwischen Start- und Endknoten ist keine Verbindung möglich", way.getTargetNode().isChecked());

        Output output = new Output(31463, 31467);
        output.outputDijkstra("./ShapeFiles/Dijkstra123.shp", path);
    }

    // @Test
    public void andereRichtung() throws IOException, FactoryException {
        Way wayHin = new Way("11769", "12903", 1); // Köln - München
        List<Node> path = new ArrayList<>();
        path = wayHin.run();

        Output output = new Output(31463, 31467);
        output.outputDijkstra("./ShapeFiles/Hinweg.shp", path);

        System.out.println(wayHin.getDistance() / 1000);
        for (int i = path.size() - 1; i > 0; i--) {
            System.out.print(path.get(i).getId() + " , ");
        }

        Way wayZurueck = new Way("12903", "11769", 1); // München - Köln
        List<Node> path2 = new ArrayList<>();
        path2 = wayZurueck.run();

        output.outputDijkstra("./ShapeFiles/Rueckweg.shp", path2);

        assertEquals(path.get(path.size() - 1).getDistance() / 1000, path2.get(path2.size() - 1).getDistance() / 1000,
                5);

        System.out.println();
        System.out.println(wayZurueck.getDistance() / 1000);
        for (int i = 0; i < path2.size(); i++) {
            System.out.print(path2.get(i).getId() + " , ");
        }
    }

    @Test
    public void dijkstraOptimiert() throws IOException, FactoryException {
        Way way = new Way("10141", "11104", 1); // 11104 Aachen // 10141 Hamburg // 12903 München-Ost // 11769 Köln //
        List<Node> path = new ArrayList<>();

        path = way.run();

        Output output = new Output(31463, 31467);
        output.outputDijkstra("./ShapeFiles/DijkstraTEST.shp", path);

        output.outputNodes("./ShapeFiles/WolkeTEST.shp", way.getAllVisitedNodes());
        System.out.println(way.getAllVisitedNodes().size());
        System.out.println(path.size());
        System.out.println(way.getDistance() / 1000);
    }

    // @Test
    public void dijkstraOhneOptimierung() throws IOException, FactoryException {
        Way way = new Way("10141", "11104", 1); // 11104 Aachen // 10141 Hamburg // 12903 München-Ost // 11769 Köln //
        List<Node> path = new ArrayList<>();

        path = way.run();

        Output output = new Output(31463, 31467);
        output.outputDijkstra("./ShapeFiles/Dijkstra.shp", path);

        output.outputNodes("./ShapeFiles/Wolke.shp", way.getAllVisitedNodes());
    }

    @Test
    public void inputFiltriert() throws IOException, FactoryException {
        Way way = new Way(1, "10141", "11104"); // 11104 Aachen // 10141 Hamburg // 12903 München-Ost // 11769 Köln //
        List<Node> path = new ArrayList<>();

        path = way.run();

        Output output = new Output(31463, 31467);
        output.outputDijkstra("./ShapeFiles/DijkstraFiltriert.shp", path);

        output.outputNodes("./ShapeFiles/WolkeFiltriert.shp", way.getAllVisitedNodes());
        System.out.println(way.getDistance() / 1000);

    }

}