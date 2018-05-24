package de.heuboe.ausbildung.DijkstraAlgorithm;

public class Kante {

	private Knoten von;
	private Knoten bis;
	private String id;
	private double distance;
	
	public Kante(Knoten von, Knoten bis, String id, double distance) {
		this.von = von;
		this.bis = bis;
		this.id = id;
		this.distance = distance;		
	}
	
//	public Kante(Knoten von, Knoten bis, String id) {
//		this.von = von;
//		this.bis = bis;
//		this.id = id;
//		setDistance()
//		this.distance = getDistance(von, bis);
//	}

	public Knoten getVon() {
		return von;
	}

	public void setVon(Knoten von) {
		this.von = von;
	}

	public Knoten getBis() {
		return bis;
	}

	public void setBis(Knoten bis) {
		this.bis = bis;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}
	
}
