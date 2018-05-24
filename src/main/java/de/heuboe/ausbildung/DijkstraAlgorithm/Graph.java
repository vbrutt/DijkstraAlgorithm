package de.heuboe.ausbildung.DijkstraAlgorithm;

import java.util.*;

public class Graph {

	private List<Knoten> knoten;
	private List<Kante> kanten;

	public Graph(List<Kante> kanten, List<Knoten> knoten) {
		this.kanten = kanten;
		this.knoten = knoten;
	}

	public List<Knoten> getKnoten() {
		return knoten;
	}

	public List<Kante> getKanten() {
		return kanten;
	}

}
