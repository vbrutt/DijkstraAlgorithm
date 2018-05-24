package de.heuboe.ausbildung.DijkstraAlgorithm;

import java.util.*;

public class Knoten {

	private String id;
	private List<Kante> kanten;
	private Knoten vorgaenger;
	//private Map<Knoten, Double> abstandMap; // abstand vom Starknoten bis zu diesem Knoten
	private double abstand;

	public Knoten(String id) {
		this.id = id;
	}

	public Knoten(String id, List<Kante> kanten) {
		this.id = id;
		this.kanten = kanten;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Kante> getKanten() {
		return kanten;
	}

	public void setKanten(List<Kante> kanten) {
		this.kanten = kanten;
	}

	public Knoten getVorgaenger() {
		return vorgaenger;
	}

	public void setVorgaenger(Knoten vorgaenger) {
		this.vorgaenger = vorgaenger;
	}

	// public double getAbstand() {
	// return abstand;
	// }

	public void setAbstand(double abstand) {
		this.abstand = abstand;
		// this.abstand = abstand.get(knoten);
	}

	public double getAbstand() {
		return abstand;
	}

	// public void setAbstand(double abstand) {
	// this.abstand = abstand;
	// }

	// public double getAbstanddouble(Knoten knoten) {
	// return knoten
	// }

}
