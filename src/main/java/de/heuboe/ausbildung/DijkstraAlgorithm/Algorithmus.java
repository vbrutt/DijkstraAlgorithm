package de.heuboe.ausbildung.DijkstraAlgorithm;

import java.io.*;
import java.util.*;

import org.apache.commons.csv.*;

public class Algorithmus {

	private List<Kante> kanten = new ArrayList<>();
	private List<Knoten> knoten = new ArrayList<>();
	private Map<Knoten, Double> abstand;
	private Map<Knoten, Knoten> vorgaenger = new HashMap<>();
	private List<Knoten> unvisitedKnoten = new ArrayList<>();
	private List<Knoten> visitedKnoten = new ArrayList<>();

	/**
	 * initializes the class, reads the csv file and sets the Edges (Kanten) and the
	 * Nodes
	 * 
	 * @param source
	 */
	public Algorithmus(String source) {
		CSVFormat format = CSVFormat.EXCEL.withHeader().withDelimiter(';');
		List<Kante> kantenList = new ArrayList<>();
		List<Knoten> knotenList = new ArrayList<>();

		try (FileReader reader = new FileReader(source)) {
			CSVParser parser = new CSVParser(reader, format);
			for (CSVRecord record : parser.getRecords()) {
				Knoten knoten1 = new Knoten(record.get("von"));
				Knoten knoten2 = new Knoten(record.get("bis"));
				getDistance(knoten1, knoten2, record);
				Kante kante = new Kante(knoten1, knoten2, record.get("id"), Double.parseDouble(record.get("Abstand")));
				if (!(knotenList.isEmpty())) {
					if (kante.getVon().getId().equals(knotenList.get(knotenList.size() - 1).getId())) {
						Knoten knoten = knotenList.get(knotenList.size() - 1);
						Kante kante1 = new Kante(knoten, kante.getBis(), record.get("id"),
								Double.parseDouble(record.get("Abstand")));
						List<Kante> kanten = knoten.getKanten();
						kanten.add(kante1);
					}
				}
				kantenList.add(kante);
				List<Kante> kantenList2 = new ArrayList<>();
				kantenList2.add(kante);
				Knoten knoten = new Knoten(record.get("id"), kantenList2);
				// getDistance(knoten, kantenList2.get(kantenList2.size() - 1).getBis(),
				// record);
				// getDistance(knoten);
				knotenList.add(knoten);
			}
			knotenList.add(kantenList.get(knotenList.size() - 1).getBis());
			this.kanten = kantenList;
			this.knoten = knotenList;
			parser.close();
			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void getDistance(Knoten knoten1, Knoten knoten2, CSVRecord record) {
		Map<Knoten, Double> abstand = new HashMap<Knoten, Double>();
		abstand.put(knoten1, Double.valueOf(record.get("Abstand")));
		knoten1.setAbstand(abstand.get(knoten1));
		// abstand.clear();
		abstand = new HashMap<Knoten, Double>();
		abstand.put(knoten2, Double.valueOf(record.get("Abstand")));
		knoten2.setAbstand(abstand.get(knoten2));
		// abstand.clear();
	}

	private void initialisiere(Graph graph, Knoten startKnoten) {
		for (Knoten knoten : graph.getKnoten()) {
			abstand = new HashMap<>();
			if (knoten.equals(startKnoten)) {
				abstand.put(knoten, 0.0);
				knoten.setAbstand(abstand.get(knoten));
				vorgaenger.put(knoten, null);
				knoten.setVorgaenger(null);
			} else {
				vorgaenger = new HashMap<>();
				abstand.put(knoten, Double.POSITIVE_INFINITY);
				knoten.setAbstand(abstand.get(knoten));
				vorgaenger.put(knoten, null);
				knoten.setVorgaenger(null);
			}
		}
	}

	private List<Knoten> getNachbars(Knoten startknoten, List<Knoten> nachbars) {
		if (startknoten.getKanten() == null) {
			return null;
		}
		for (Kante actualKante : startknoten.getKanten()) {
			if (actualKante.getVon().getId().equals(startknoten.getId())) {
				nachbars.add(actualKante.getBis());
			}
		}
		return nachbars;
	}

	/**
	 * @return the node with the smallest distance until the start node
	 */
	private Knoten getAnfangsKnoten() {
		Double min = null;
		Knoten actualKnoten = null;
		for (Knoten knoten : unvisitedKnoten) {
			if (min == null) {
				min = knoten.getAbstand();
				actualKnoten = knoten;
			} else {
				if (knoten.getAbstand() < min) {
					actualKnoten = knoten;
				}
			}
		}
		return actualKnoten;
	}

	// private void distanz_update(Knoten actualKnoten, Knoten nachbar, double
	// aktuellerWeg) {
	// double alternative = actualKnoten.getAbstand() +
	// getKanteAbstand(actualKnoten, nachbar);
	// aktuellerWeg += actualKnoten.getAbstand() + nachbar.getAbstand();
	// if(alternative < aktuellerWeg) {
	// abstand.put(nachbar, alternative);
	// nachbar.setAbstand(alternative);
	//
	// vorgaenger.put(nachbar, actualKnoten);
	// nachbar.setVorgaenger(actualKnoten);
	// }
	// else {
	// abstand.put(nachbar, aktuellerWeg);
	// nachbar.setAbstand(aktuellerWeg);
	//
	// vorgaenger.put(nachbar, actualKnoten);
	// nachbar.setVorgaenger(actualKnoten);
	// }
	// }

	private Double getKanteAbstand(Knoten knotenVon, Knoten knotenBis) {
		for (Kante kante : kanten) {
			if (kante.getVon().getId().equals(knotenVon.getId()) && kante.getBis().getId().equals(knotenBis.getId())) {
				return kante.getDistance();
			}
		}
		return null; // D.h. die beide Knoten bilden keine Kante, sollte nicht passieren!!
	}

	private double distanzUpdate(Knoten actualKnoten, List<Knoten> nachbars, double aktuellerWeg) {
		// aktuellerWeg = actualKnoten.getKanten().get(0).getDistance();
		// for (Knoten nachbar : nachbars) {
		// distanz_update(actualKnoten, nachbar, aktuellerWeg);
		// }
		if (nachbars.size() <= 1) {
			Knoten nachbar = nachbars.get(0);
			Knoten vorherigerKnoten = vorgaenger.get(actualKnoten);
			nachbars = getNachbars(vorherigerKnoten, nachbars);

		} else if (nachbars.size() == 0) {
			unvisitedKnoten.clear();
		} else {
			for (int i = 0; i < nachbars.size() - 1; i++) {
				double alternative = nachbars.get(i + 1).getAbstand();
				aktuellerWeg += actualKnoten.getAbstand() + nachbars.get(i).getAbstand();
				if (alternative < aktuellerWeg) {
					abstand.put(nachbars.get(i + 1), alternative);
					nachbars.get(i).setAbstand(alternative);

					vorgaenger.put(nachbars.get(i + 1), actualKnoten);
					nachbars.get(i + 1).setVorgaenger(actualKnoten);
				} else {
					abstand.put(nachbars.get(i), aktuellerWeg);
					nachbars.get(i).setAbstand(aktuellerWeg);

					vorgaenger.put(nachbars.get(i), actualKnoten);
					nachbars.get(i).setVorgaenger(actualKnoten);
				}
			}

		}
		visitedKnoten.add(actualKnoten);

		return aktuellerWeg;
	}

	private Knoten getLastKnoten() {
		for (Knoten knoten : knoten) {
			if (knoten.getId().equals("4")) {
				return knoten;
			}
		}
		return null;
	}

	private List<Knoten> erstelleWeg() {
		Knoten zielKnoten = getLastKnoten();
		List<Knoten> weg = new ArrayList<>();
		weg.add(zielKnoten);

		while (!(vorgaenger.isEmpty())) {
			Knoten knoten = zielKnoten.getVorgaenger();
			weg.add(0, knoten);
		}

		return weg;
	}

	private List<Knoten> run(Algorithmus alg) {
		abstand = new HashMap<>();
		Graph graph = new Graph(alg.kanten, alg.knoten);

		unvisitedKnoten.addAll(alg.knoten);
		Knoten startKnoten = graph.getKnoten().get(0);
		initialisiere(graph, startKnoten);
		double aktuellerWeg = 0.0;
		while (unvisitedKnoten.size() > 0) {
			Knoten actualKnoten = getAnfangsKnoten();
			unvisitedKnoten.remove(actualKnoten);
			List<Knoten> nachbars = new ArrayList<>();
			getNachbars(actualKnoten, nachbars);

			aktuellerWeg += alg.distanzUpdate(actualKnoten, nachbars, aktuellerWeg);

		}
		return erstelleWeg();
	}

	public static void main(String[] args) {
		String source = "C:\\Users\\verab\\Documents\\Dijkstra-Algorithmus\\Kanten_Tebelle.csv";
		Algorithmus alg = new Algorithmus(source);

		List<Knoten> weg = alg.run(alg);
	}

}
