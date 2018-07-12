package de.heuboe.ausbildung.DijkstraAlgorithm;

import java.io.*;
import java.util.*;

import org.junit.*;
import org.opengis.referencing.*;

public class MultipleNodesTest {

	// @Test
	public void parallelogramm() {
		String source = "C:\\Users\\verab\\Documents\\Dijkstra-Algorithmus\\Kanten_Tabelle1.csv";

		ShortestWay sW = new ShortestWay(source, "1", "4");
		List<Node> path = sW.run();

		QuickestWay qW = new QuickestWay(source, "1", "4");
		List<Node> path2 = qW.run();

		Output.output(path, path2, sW, qW);
	}

	// @Test
	public void parallelogrammAndereRichtung() {
		String source = "C:\\Users\\verab\\Documents\\Dijkstra-Algorithmus\\Kanten_Tabelle1.csv";
		ShortestWay sW = new ShortestWay(source, "4", "1");
		List<Node> path = sW.run();

		QuickestWay qW = new QuickestWay(source, "4", "1");
		List<Node> path2 = qW.run();

		Output.output(path, path2, sW, qW);
	}

	// @Test
	public void weiterePunkte() {
		String source = "C:\\Users\\verab\\Documents\\Dijkstra-Algorithmus\\Kanten_Tabelle2.csv";

		ShortestWay sW = new ShortestWay(source, "1", "8");
		List<Node> path = sW.run();

		QuickestWay qW = new QuickestWay(source, "1", "8");
		List<Node> path2 = qW.run();

		Output.output(path, path2, sW, qW);
	}

	// @Test
	public void weiterePunkte2() {
		String source = "C:\\Users\\verab\\Documents\\Dijkstra-Algorithmus\\Kanten_Tabelle3.csv";

		ShortestWay sW = new ShortestWay(source, "1", "9");
		List<Node> path = sW.run();

		QuickestWay qW = new QuickestWay(source, "1", "9");
		List<Node> path2 = qW.run();

		Output.output(path, path2, sW, qW);
	}

	// @Test
	public void gleicheAbstaende() {
		String source = "C:\\Users\\verab\\Documents\\Dijkstra-Algorithmus\\Kanten_Tabelle_alleGleich.csv";

		ShortestWay sW = new ShortestWay(source, "1", "4");
		List<Node> path = sW.run();

		QuickestWay qW = new QuickestWay(source, "1", "4");
		List<Node> path2 = qW.run();

		Output.output(path, path2, sW, qW);
	}

	// @Test // Keine Verbindung zwischen Endknoten und die andere Knoten
	public void keineVerbindung() {
		String source = "C:\\Users\\verab\\Documents\\Dijkstra-Algorithmus\\Kanten_Tabelle1.csv";

		ShortestWay sW = new ShortestWay(source, "1", "6");
		List<Node> path = sW.run();

		QuickestWay qW = new QuickestWay(source, "1", "6");
		List<Node> path2 = qW.run();

		Output.output(path, path2, sW, qW);
	}

	@Test
	public void newAlgorithm() throws IOException, FactoryException {
		// ShortestWay sW = new ShortestWay("10141", "11104");
		Way way = new Way("10141", "11104");

	}
}
