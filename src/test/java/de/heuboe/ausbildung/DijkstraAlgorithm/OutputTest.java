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
    @Test
    public void outputLCL() throws FactoryException, IOException {
        Net net = Input.getNetFormLCL("C:\\Users\\verab\\Documents\\Dijkstra-Algorithmus\\LCL16.0.D.csv");
        Set<Road> roads = net.getRoads();

        Output output = new Output(31463, 31467);
        output.outputLCL("./ShapeFiles/LCLAusgabe.shp", roads);
    }

    @Test
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

    @Test
    public void keineVerbindung() throws IOException, FactoryException {
        Way way = new Way("11769", "54969", 1);
        List<Node> path = new ArrayList<>();

        path = way.run();

        assertFalse("Zwischen Start- und Endknoten ist keine Verbindung möglich", way.getTargetNode().isChecked());

        Output output = new Output(31463, 31467);
        output.outputDijkstra("./ShapeFiles/Dijkstra123.shp", path);
    }

    @Test
    public void andereRichtung() throws IOException, FactoryException {
        Way wayHin = new Way("11769", "12903", 1); // Köln - München
        List<Node> path = new ArrayList<>();
        path = wayHin.run();

        Output output = new Output(31463, 31467);
        output.outputDijkstra("./ShapeFiles/Hinweg.shp", path);

        Way wayZurueck = new Way("12903", "11769", 1); // München - Köln
        List<Node> path2 = new ArrayList<>();
        path2 = wayZurueck.run();

        output.outputDijkstra("./ShapeFiles/Rueckweg.shp", path2);

        assertEquals(wayHin.getDistance(), wayZurueck.getDistance(), 1);
    }

    /**
     * Beim Einlesen der LCL wurden die Knoten nur aus Autobahnen gebildet. Der
     * Algorithmus wurde optimiert
     * 
     * @throws IOException
     * @throws FactoryException
     */
    @Test
    public void dijkstraOptimiert() throws IOException, FactoryException {
        Way way = new Way("10141", "11104", 1); // 11104 Aachen // 10141 Hamburg // 12903 München-Ost // 11769 Köln //
        List<Node> path = new ArrayList<>();

        path = way.run();

        Output output = new Output(31463, 31467);
        output.outputDijkstra("./ShapeFiles/DijkstraOptimiert.shp", path);
        output.outputNodes("./ShapeFiles/WolkeOptimiert.shp", way.getAllVisitedNodes());
    }

    /**
     * Beim Einlesen der LCL wurden die Knoten nur aus Autobahnen gebildet. Der
     * Algorithmus wurde nicht optimiert
     * 
     * @throws IOException
     * @throws FactoryException
     */
    @Test
    public void dijkstraOhneOptimierung() throws IOException, FactoryException {
        Way way = new Way("10141", "11104", 1); // 11104 Aachen // 10141 Hamburg // 12903 München-Ost // 11769 Köln //
        List<Node> path = new ArrayList<>();

        path = way.run();

        Output output = new Output(31463, 31467);
        output.outputDijkstra("./ShapeFiles/DijkstraOhneOptimierung.shp", path);
        output.outputNodes("./ShapeFiles/WolkeOhneOptimierung.shp", way.getAllVisitedNodes());
    }

    /**
     * alle Punkte von der LCL werden eingelesen und als Knoten gespeichert. Der
     * Algorithmus wurde optimiert
     * 
     * @throws IOException
     * @throws FactoryException
     */
    @Test
    public void inputUnfiltriert() throws IOException, FactoryException {
        Way way = new Way(1, "10141", "11104"); // 11104 Aachen // 10141 Hamburg // 12903 München-Ost // 11769 Köln //
        List<Node> path = new ArrayList<>();

        path = way.run();

        Output output = new Output(31463, 31467);
        output.outputDijkstra("./ShapeFiles/DijkstraUnfiltriert.shp", path);
        output.outputNodes("./ShapeFiles/WolkeUnfiltriert.shp", way.getAllVisitedNodes());
    }

    /**
     * alle Punkte von der LCL werden eingelesen und als Knoten gespeichert. Der
     * Algorithmus wurde nicht optimiert
     * 
     * @throws IOException
     * @throws FactoryException
     */
    @Test
    public void inputUnfiltriertOhneOptimierung() throws IOException, FactoryException {
        Way way = new Way(1, "10141", "11104"); // 11104 Aachen // 10141 Hamburg // 12903 München-Ost // 11769 Köln //
        List<Node> path = new ArrayList<>();

        path = way.run();

        Output output = new Output(31463, 31467);
        output.outputDijkstra("./ShapeFiles/DijkstraUnfiltriertNichtOpt.shp", path);
        output.outputNodes("./ShapeFiles/WolkeUnfiltriertNichtOpt.shp", way.getAllVisitedNodes());
    }
}