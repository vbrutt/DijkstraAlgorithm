package de.heuboe.ausbildung.DijkstraAlgorithm;

import java.util.*;

import org.junit.*;

public class MultipleNodesTest {

	@Test
	public void parallelogramm() {
		String source = "C:\\Users\\verab\\Documents\\Dijkstra-Algorithmus\\Kanten_Tabelle.csv";
		Algorithmus alg = new Algorithmus(source, "1", "4");

		List<Node> path = alg.run(alg);
		for (int i = 0; i < path.size(); i++) {
			System.out.print(path.get(i).getId() + ",");
		}
		System.out.println(" Total distance: " + path.get(path.size() - 1).getDistance());
	}

	//@Test
	public void parallelogrammAndereRichtung() {
		String source = "C:\\Users\\verab\\Documents\\Dijkstra-Algorithmus\\Kanten_Tabelle.csv";
		Algorithmus alg = new Algorithmus(source, "4", "1");

		List<Node> path = alg.run(alg);
		for (int i = 0; i < path.size(); i++) {
			System.out.print(path.get(i).getId() + ",");
		}
		System.out.println(" Total distance: " + path.get(path.size() - 1).getDistance());
	}

	@Test
	public void weiterePunkte() {
		String source = "C:\\Users\\verab\\Documents\\Dijkstra-Algorithmus\\Kanten_Tabelle2.csv";
		Algorithmus alg = new Algorithmus(source, "1", "8");

		List<Node> path = alg.run(alg);
		for (int i = 0; i < path.size(); i++) {
			System.out.print(path.get(i).getId() + ",");
		}
		System.out.println(" Total distance: " + path.get(path.size() - 1).getDistance());
	}

	@Test
	public void weiterePunkte2() {
		String source = "C:\\Users\\verab\\Documents\\Dijkstra-Algorithmus\\Kanten_Tabelle3.csv";
		Algorithmus alg = new Algorithmus(source, "1", "9");

		List<Node> path = alg.run(alg);
		for (int i = 0; i < path.size(); i++) {
			System.out.print(path.get(i).getId() + ",");
		}
		System.out.println(" Total distance: " + path.get(path.size() - 1).getDistance());
	}

	@Test
	public void gleicheAbstaende() {
		String source = "C:\\Users\\verab\\Documents\\Dijkstra-Algorithmus\\Kanten_Tabelle_alleGleich.csv";
		Algorithmus alg = new Algorithmus(source, "1", "4");

		List<Node> path = alg.run(alg);
		for (int i = 0; i < path.size(); i++) {
			System.out.print(path.get(i).getId() + ",");
		}
		System.out.println(" Total distance: " + path.get(path.size() - 1).getDistance());
	}

	//@Test
	public void keineVerbindung() {
		String source = "C:\\Users\\verab\\Documents\\Dijkstra-Algorithmus\\Kanten_Tabelle.csv";
		Algorithmus alg = new Algorithmus(source, "1", "6"); // Keine Verbindung zwischen Endknoten und die andere
																// Knoten

		List<Node> path = alg.run(alg);
		for (int i = 0; i < path.size(); i++) {
			System.out.print(path.get(i).getId() + ",");
		}
		System.out.println(" Total distance: " + path.get(path.size() - 1).getDistance());
	}

	@Test
	public void aussenPunkt() {
		String source = "C:\\Users\\verab\\Documents\\Dijkstra-Algorithmus\\Kanten_Tabelle4.csv";
		Algorithmus alg = new Algorithmus(source, "1", "8");

		List<Node> path = alg.run(alg);
		for (int i = 0; i < path.size(); i++) {
			System.out.print(path.get(i).getId() + ",");
		}
		System.out.println(" Total distance: " + path.get(path.size() - 1).getDistance());
	}
}
