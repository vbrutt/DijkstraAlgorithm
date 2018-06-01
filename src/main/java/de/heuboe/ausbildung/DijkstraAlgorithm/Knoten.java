package de.heuboe.ausbildung.DijkstraAlgorithm;

import java.util.*;

public class Knoten {

	public static int numKnoten = 0;

	private String id;
	private List<Kante> kanten = new ArrayList<>();
	private Knoten vorgaenger;	
	private Double abstand;

	public Knoten(String id) {
		numKnoten++;

		this.id = id;	
		this.vorgaenger = null;
	}

	public Knoten(String id, Kante kanten) {
		numKnoten++;

		this.id = id;
		this.kanten.add(kanten);
		this.vorgaenger = null;
	}

	public Knoten(String id, List<Kante> kanten) {
		numKnoten++;

		this.id = id;
		this.kanten = kanten;
		this.vorgaenger = null;
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

	public void setKante(Kante kante) {
		this.kanten.add(kante);
	}

	public Knoten getVorgaenger() {
		return vorgaenger;
	}

	public void setVorgaenger(Knoten vorgaenger) {
		this.vorgaenger = vorgaenger;
	}

	public Double getAbstand() {
		return abstand;
	}

	public void setAbstand(Double abstand) {
		this.abstand = abstand;
	}
}
