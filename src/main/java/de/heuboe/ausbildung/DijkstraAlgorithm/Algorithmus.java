package de.heuboe.ausbildung.DijkstraAlgorithm;

import java.io.*;
import java.util.*;

import org.apache.commons.csv.*;

public class Algorithmus {

	private List<Kante> kanten = new ArrayList<>();
	private List<Knoten> knoten = new ArrayList<>();
	private Map<Knoten, Double> abstand = new HashMap<>();
	private Map<Knoten, Knoten> vorgaenger = new HashMap<>(); // erster Knoten ist der betrachteter Knoten
	private List<Knoten> unvisitedKnoten = new ArrayList<>();
	private List<Knoten> visitedKnoten = new ArrayList<>();

	private Map<String, Knoten> alleKnoten = new HashMap<>();

	private Knoten getKnoten(String id) {
		Knoten knoten = alleKnoten.get(id);
		if (knoten == null) {
			knoten = new Knoten(id);
			alleKnoten.put(id, knoten);
		}
		return knoten;
	}

	/**
	 * initializes the class, by reading the csv file and setting the edges (Kanten)
	 * and nodes
	 * 
	 * @param source
	 */
	public Algorithmus(String source) {
		CSVFormat format = CSVFormat.EXCEL.withHeader().withDelimiter(';');
		List<Kante> kantenList = new ArrayList<>();
		List<Knoten> knotenList = new ArrayList<>();
		Set<Knoten> knotenSet = new HashSet<>();

		try (FileReader reader = new FileReader(source)) {
			CSVParser parser = new CSVParser(reader, format);
			for (CSVRecord record : parser.getRecords()) {
				Knoten knoten1 = getKnoten(record.get("von"));
				Knoten knoten2 = getKnoten(record.get("bis"));
				Kante kante = new Kante(knoten1, knoten2, record.get("id"), Double.parseDouble(record.get("Abstand")));

				knotenSet.add(knoten1);
				knotenSet.add(knoten2);
				kantenList.add(kante);
			}
			knotenList.addAll(knotenSet);

			kantenList = setKanten(kantenList, knotenList);
			this.kanten = kantenList;
			this.knoten = knotenList;
			parser.close();
			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println(Knoten.numKnoten);
	}

	private List<Kante> setKanten(List<Kante> kantenList, List<Knoten> knotenList) {
		for (Kante kanten : kantenList) {
			for (Knoten knoten : knotenList) {
				if (kanten.getVon().equals(knoten)) {
					knoten.setKante(kanten);
				}
			}
		}
		return kantenList;
	}

	private void initialisiere(Graph graph, Knoten startKnoten) {
		for (Knoten knoten : graph.getKnoten()) {
			if (knoten.equals(startKnoten)) {
				abstand.put(knoten, 0.0);
				vorgaenger.put(knoten, null);
			} else {
				abstand.put(knoten, Double.POSITIVE_INFINITY);
				vorgaenger.put(knoten, null);
			}
		}

	}

	private List<Knoten> getNachbars(Knoten knoten) {
		List<Knoten> nachbars = new ArrayList<>();
		if (knoten.getKanten() == null) {
			return null;
		}
		for (Kante kante : knoten.getKanten()) {
			abstand.put(kante.getBis(), (abstand.get(knoten) + kante.getDistance()));
			nachbars.add(kante.getBis());
		}
		return nachbars;
	}

	public double getMinNachbar(List<Knoten> nachbars) {
		Double min = null;
		for (Knoten knoten : nachbars) {
			if (min == null) {
				min = abstand.get(knoten);
			} else {
				if (abstand.get(knoten) < min) {
					min = abstand.get(knoten);

				}
			}
		}
		return min;
	}

	private Knoten getMinKnoten(List<Knoten> nachbars) {
		Double min = null;
		Knoten actualKnoten = null;
		for (Knoten knoten : nachbars) {
			if (min == null) {
				min = abstand.get(knoten);
				actualKnoten = knoten;
			} else {
				if (abstand.get(knoten) < min) {
					actualKnoten = knoten;
				}
			}
		}
		return actualKnoten;
	}

	/**
	 * @return the node with the smallest distance until the start node
	 */
	private Knoten getAnfangsKnoten() {
		Double min = null;
		Knoten actualKnoten = null;
		for (Knoten knoten : unvisitedKnoten) {
			if (min == null) {
				min = abstand.get(knoten);
				actualKnoten = knoten;
			} else {
				if (abstand.get(knoten) < min) {
					actualKnoten = knoten;
				}
			}
		}
		return actualKnoten;
	}

	@SuppressWarnings("unused")
	private Double getKanteAbstand(Knoten knotenVon, Knoten knotenBis) {
		for (Kante kante : kanten) {
			if (kante.getVon().getId().equals(knotenVon.getId()) && kante.getBis().getId().equals(knotenBis.getId())) {
				return kante.getDistance();
			}
		}
		return null; // D.h. die beide Knoten bilden keine Kante, sollte nicht passieren!!
	}

	@SuppressWarnings("unused")
	private Knoten getKnoten(double alternative) {
		for (Knoten knoten : knoten) {
			if (abstand.get(knoten) == alternative) {
				return knoten;
			}
		}
		return null;
	}

	private double distanzUpdate(Knoten actualKnoten, List<Knoten> nachbars, double aktuellerWeg, double alternative) {
		aktuellerWeg = getMinNachbar(nachbars); // kleinster Abstand zwischen alle
												// Nachbarn
		if (abstand.get(actualKnoten) == 0.0) {
			Knoten k = getMinKnoten(nachbars);
			nachbars.remove(k);
			if (nachbars.size() > 0) {
				alternative = abstand.get(nachbars.get(0));
			}
			abstand.put(k, aktuellerWeg);
			vorgaenger.replace(k, actualKnoten);

		} else {
			if (alternative < aktuellerWeg) {
				if (checkAbstand(alternative)) {
					alternative = aktuellerWeg;
				} else {

				}
			}
		}
		return alternative;
	}

	private boolean checkAbstand(double alternative) {
		for (Knoten knoten : knoten) {
			if (abstand.get(knoten) == alternative) {
				return true;
			}
		}
		return false;
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

	private List<Knoten> run(Algorithmus alg, Knoten startKnoten) {
		Graph graph = new Graph(alg.kanten, alg.knoten);

		initialisiere(graph, startKnoten);
		unvisitedKnoten.addAll(alg.knoten);
		double aktuellerWeg = 0.0;
		double alternative = 0.0;

		while (unvisitedKnoten.size() > 0) {
			Knoten actualKnoten = getAnfangsKnoten();
			unvisitedKnoten.remove(actualKnoten);
			visitedKnoten.add(actualKnoten);
			List<Knoten> nachbars = getNachbars(actualKnoten);
			alternative = distanzUpdate(actualKnoten, nachbars, aktuellerWeg, alternative);
		}
		return erstelleWeg();
	}

	private Knoten getFirstKnoten(String id) {
		for (int i = 0; i < knoten.size() - 1; i++) {
			if (knoten.get(i).getId().equals(id)) {
				return knoten.get(i);
			}
		}
		return null;
	}

	public static void main(String[] args) {
		String source = "C:\\Users\\verab\\Documents\\Dijkstra-Algorithmus\\Kanten_Tebelle.csv";
		Algorithmus alg = new Algorithmus(source);
		Knoten startKnoten = alg.getFirstKnoten("1");

		@SuppressWarnings("unused")
		List<Knoten> weg = alg.run(alg, startKnoten);
	}
}

// private double distanzUpdate(Knoten actualKnoten, List<Knoten> nachbars,
// double aktuellerWeg, Knoten nachbar1,
// double alternative) {
// if (alternative > 0 && nachbars.isEmpty()) {
// actualKnoten = check(alternative, aktuellerWeg, actualKnoten, nachbars);
// }
// if (nachbars.size() < 3) {
// for (int i = 0; i < nachbars.size() - 1; i++) {
// aktuellerWeg += abstand.get(nachbars.get(i).getId());
// alternative = abstand.get(nachbars.get(i + 1).getId());
// if (alternative < aktuellerWeg) {
// abstand.put(nachbars.get(i + 1).getId(),
// abstand.get(actualKnoten.getId()) + abstand.get(nachbars.get(i +1).getId()));
// vorgaenger.put(nachbars.get(i + 1).getId(), actualKnoten);
//
// } else {
// abstand.put(nachbars.get(i).getId(),
// (abstand.get(actualKnoten.getId()) + abstand.get(nachbars.get(i).getId())));
// vorgaenger.replace(nachbars.get(i).getId(), actualKnoten);
// }
// }
// } else {
// for (Knoten knoten : nachbars) {
// alternative = abstand.get(knoten.getId());
// if (alternative < aktuellerWeg) {
// abstand.put(knoten.getId(),
// abstand.get(actualKnoten.getId()) + getKanteAbstand(actualKnoten, knoten));
// vorgaenger.put(knoten.getId(), actualKnoten);
//
// } else {
// abstand.put(nachbar1.getId(),
// (abstand.get(actualKnoten.getId()) + abstand.get(nachbar1.getId())));
// vorgaenger.replace(nachbar1.getId(), actualKnoten);
// }
// }
//
// }
//
// return aktuellerWeg;
// }

// for (Knoten knoten : nachbars) {
// if (abstand.get(knoten.getId()) == aktuellerWeg) {
// // actualKnoten = knoten;
// vorgaenger.replace(knoten.getId(), actualKnoten);
// abstand.put(knoten.getId(), aktuellerWeg);
// }
// }
// abstand.put(nachbar1.getId(), alternative);