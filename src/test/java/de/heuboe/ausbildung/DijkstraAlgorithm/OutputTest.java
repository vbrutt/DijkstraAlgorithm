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
import de.heuboe.geo.*;
import de.heuboe.geo.impl.*;

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
        output.outputDijkstra("./ShapeFiles/Dijkstra15Sec.shp", path);
    }

    @Test
    public void keineVerbindung() throws IOException, FactoryException {
        Way way = new Way("11769", "54969", 1);
        List<Node> path = new ArrayList<>();

        path = way.run();

        assertFalse("Zwischen Start- und Endknoten ist keine Verbindung möglich", way.getTargetNode().isChecked());

        Output output = new Output(31463, 31467);
        output.outputDijkstra("./ShapeFiles/DijkstraKeineVerbindung.shp", path);
    }

    @Test
    public void hinUndZurueck() throws IOException, FactoryException {
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
        Way way = new Way("11769", "11104", 1); // 11104 Aachen // 10141 Hamburg // 12903 München-Ost // 11769 Köln //
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

    @Test
    public void testVerdünnung() throws FactoryException {
        Coordinate p1 = new CoordinateImpl(-3.0, -2.0);
        Coordinate p2 = new CoordinateImpl(-3.0, 0.0);
        Coordinate p3 = new CoordinateImpl(-2.0, 2.0);
        Coordinate p4 = new CoordinateImpl(0.0, 3.0);
        Coordinate p5 = new CoordinateImpl(2.0, 2.0);
        Coordinate p6 = new CoordinateImpl(3.0, 0.0);
        Coordinate p7 = new CoordinateImpl(3.0, -2.0);

        // Die Liste ist sortiert
        List<Coordinate> allPoints = new ArrayList<>();
        allPoints.add(p1);
        allPoints.add(p2);
        allPoints.add(p3);
        allPoints.add(p4);
        allPoints.add(p5);
        allPoints.add(p6);
        allPoints.add(p7);

        // Rarefaction rarefaction = new Rarefaction(p1, p7, allPoints, 9);
        // List<Line> newLine = rarefaction.run();

        Output output = new Output(31467, 31463);

        // output.outputLine("./ShapeFiles/LineTEST1.shp", newLine);
        output.outputPoints("./ShapeFiles/Points1.shp", allPoints);
    }

    @Test
    public void testVerdünnung2() throws FactoryException {
        Coordinate p1 = new CoordinateImpl(-7.0, 2.0);
        Coordinate p2 = new CoordinateImpl(-6.0, 4.0);
        Coordinate p3 = new CoordinateImpl(-5.0, 5.0);
        Coordinate p4 = new CoordinateImpl(-4.0, 5.0);
        Coordinate p5 = new CoordinateImpl(-3.0, 3.0);
        Coordinate p6 = new CoordinateImpl(-1.0, -1.0);
        Coordinate p7 = new CoordinateImpl(0.0, -2.0);
        Coordinate p8 = new CoordinateImpl(1.0, -1.0);
        Coordinate p9 = new CoordinateImpl(2.0, 1.0);
        Coordinate p10 = new CoordinateImpl(3.0, 2.0);
        Coordinate p11 = new CoordinateImpl(8.0, 8.0);

        // Die Liste ist sortiert
        List<Coordinate> allPoints = new ArrayList<>();
        allPoints.add(p1);
        allPoints.add(p2);
        allPoints.add(p3);
        allPoints.add(p4);
        allPoints.add(p5);
        allPoints.add(p6);
        allPoints.add(p7);
        allPoints.add(p8);
        allPoints.add(p9);
        allPoints.add(p10);
        allPoints.add(p11);

        // Rarefaction rarefaction = new Rarefaction(p1, p11, allPoints, 7);
        // List<Line> line = rarefaction.run();

        Output output = new Output(31467, 31463);

        // output.outputLine("./ShapeFiles/LineTEST2.shp", line);
        output.outputPoints("./ShapeFiles/Points2.shp", allPoints);
    }

    @Test
    public void testVerdünnung3() throws FactoryException {
        Coordinate p1 = new CoordinateImpl(-3.0, 2.0);
        Coordinate p2 = new CoordinateImpl(-2.0, -3.0);
        Coordinate p3 = new CoordinateImpl(-1.0, 3.0);
        Coordinate p4 = new CoordinateImpl(1.0, 2.0);
        Coordinate p5 = new CoordinateImpl(4.0, 1.0);
        Coordinate p6 = new CoordinateImpl(8.5, 3.0);
        Coordinate p7 = new CoordinateImpl(11.0, 2.0);
        Coordinate p8 = new CoordinateImpl(12.0, 2.0);
        Coordinate p9 = new CoordinateImpl(13.0, 2.0);
        Coordinate p10 = new CoordinateImpl(13.0, 2.0);
        Coordinate p11 = new CoordinateImpl(13.0, 4.0);

        // Die Liste ist sortiert
        List<Coordinate> allPoints = new ArrayList<>();
        allPoints.add(p1);
        allPoints.add(p2);
        allPoints.add(p3);
        allPoints.add(p4);
        allPoints.add(p5);
        allPoints.add(p6);
        allPoints.add(p7);
        allPoints.add(p8);
        allPoints.add(p9);
        allPoints.add(p10);
        allPoints.add(p11);

        // Rarefaction rarefaction = new Rarefaction(p1, p11, allPoints, 4);
        // List<Line> newLine = rarefaction.run();

        Output output = new Output(31467, 31463);
        // output.outputLine("./ShapeFiles/LineTEST3.shp", newLine);
        output.outputPoints("./ShapeFiles/Points3.shp", allPoints);
    }

    @Test
    public void dijkstraUndVerduennung() throws IOException, FactoryException {
        Way way = new Way(1, "10141", "11104"); // 11104 Aachen // 10141 Hamburg // 12903 München-Ost // 11769 Köln //
        List<Node> path = new ArrayList<>();

        path = way.run();

        Output output = new Output(31463, 31467);
        // output.outputDijkstra("./ShapeFiles/Dijkstra.shp", path);

        Rarefaction r = new Rarefaction(path.get(0), path.get(path.size() - 1), path, 10000);
        List<Line> line = r.run();
        output.outputLine("./ShapeFiles/LineTEST3.shp", line);
    }
}