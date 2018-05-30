package de.heuboe.ausbildung.DijkstraAlgorithm;

import java.util.*;

public class Knoten {
	
	public static int numKnoten = 0;

	private String id;
	private List<Kante> kanten = new ArrayList<>();
	private Knoten vorgaenger;
	// private Map<Knoten, Double> abstandMap; // abstand vom Starknoten bis zu
	// diesem Knoten
	//private Double abstand;

	public Knoten(String id) {
		numKnoten++;
		
		this.id = id;
		//this.abstand = Double.POSITIVE_INFINITY;
		//this.kanten = getKanten();
		this.vorgaenger = null;
	}

	public Knoten(String id, Kante kanten) {
		numKnoten++;
		
		this.id = id;
		this.kanten.add(kanten);
		//this.abstand = Double.POSITIVE_INFINITY;
		this.vorgaenger = null;
	}
	
	public Knoten(String id, List<Kante> kanten) {
		numKnoten++;
		
		this.id = id;
		this.kanten = kanten;
		//this.abstand = Double.POSITIVE_INFINITY;
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

	// public double getAbstand() {
	// return abstand;
	// }

	// public void setAbstand(double abstand) {
	// this.abstand = abstand;
	// // this.abstand = abstand.get(knoten);
	// }
	//
	// public double getAbstand() {
	// return abstand;
	// }

	// public void setAbstand(double abstand) {
	// this.abstand = abstand;
	// }

	// public double getAbstanddouble(Knoten knoten) {
	// return knoten
	// }

}
