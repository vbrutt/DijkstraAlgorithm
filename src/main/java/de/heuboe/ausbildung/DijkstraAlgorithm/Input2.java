package de.heuboe.ausbildung.DijkstraAlgorithm;

import java.io.*;
import java.util.*;
import java.util.Map.*;

import org.apache.commons.csv.*;
import org.geotools.referencing.*;
import org.opengis.referencing.*;
import org.opengis.referencing.crs.*;
import org.opengis.referencing.operation.*;

import de.heuboe.ausbildung.subwayPlan.interfaces.*;
import de.heuboe.ausbildung.subwayPlan.io.*;
import de.heuboe.geo.*;
import de.heuboe.geo.impl.*;

public class Input2 {
	private static final String[] TYPES = { "P1.3", "L2.1", "L1.1", "R", "P" };
	private static final String[] HEADER = { "POSITION" };
	private static CoordinateTransformer ct;
	private static Map<String, Street> roads = new HashMap<>();
	private static List<Road> finalRoads = new ArrayList<>();

	public Input2() {
	}

	public static int chooseTypeOfWay() {
		System.out.println("1 - SHORTEST WAY \n2 - QUICKEST WAY");
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		sc.close();

		return n;
	}

	/**
	 * reads the LCL and fills the net
	 * 
	 * @param net
	 * @param path
	 * @return the full net
	 * @throws FactoryException
	 * @throws IOException
	 */
	public static Graph getNetFormLCL(String source) throws FactoryException, IOException {
		roads.clear();
		finalRoads.clear();
		CSVFormat format = CSVFormat.EXCEL.withHeader().withDelimiter(';');
		try (FileReader reader = new FileReader(source)) {
			CSVParser parser = new CSVParser(reader, format);
			for (CSVRecord record : parser.getRecords()) {
				Point point = new Point(record);
				Point.everything.put(point.getLocationCode(), point);
			}
			parser.close();
			reader.close();
		} catch (IOException e) {
			throw e;
		}

		tranformCoordinates();
		linkPoints();
		createRoads();
		List<Node> nodes = transform();
		List<Edge> edges = addEdges(nodes);

		Graph graph = new Graph(nodes, edges);
		return graph;

	}

	public static List<Edge> addEdges(List<Node> nodeList) {
		List<Edge> allEdges = new ArrayList<>();
		for (Node node : nodeList) {
			for (String idSucc : node.getSucc()) {
				Node node1 = Node.nodes.get(idSucc);
				if (node1 == null) {
					continue;
				}
				Edge edge = new Edge(node, node1);
				node.addEdge(edge);
				allEdges.add(edge);
			}
			for (String id : node.getIntersections()) {
				if (id.equals(node.getId())) {
					continue;
				}
				Node node1 = Node.nodes.get(id);
				if (node1 == null) {
					continue;
				}
				Edge edge = new Edge(node, node1);
				// edge.setDistance(0);
				node.addEdge(edge);
				allEdges.add(edge);
			}
		}
		return allEdges;
	}

	/**
	 * reads data from the CSVfile, which already has the points from the LCL and
	 * possibly points inserted by the user
	 * 
	 * @param path
	 *            where the CSVfile is saved
	 * @param net
	 * @return net updated net
	 * @throws IOException
	 */
	public static Net readFromCSV(Net net) throws IOException {
		CSVFormat format = CSVFormat.EXCEL.withHeader().withDelimiter(';');
		try (FileReader reader = new FileReader(net.getSource())) {
			CSVParser parser = new CSVParser(reader, format);
			Map<String, Road> roads = new HashMap<>();
			List<CSVRecord> junctionRecords = new ArrayList<>();
			for (CSVRecord record : parser.getRecords()) {
				if (record.get("TYPE").equals(TYPES[3])) {
					Road road = new RoadImpl(record);
					roads.put(road.getId(), road);
				} else {
					junctionRecords.add(record);
				}
			}

			for (CSVRecord entry : junctionRecords) {
				Junction junction = new JunctionImpl(entry, false);
				Road road = roads.get(junction.getLineRef());
				List<Junction> junctions = road.getJunctions();
				junctions.set(Integer.parseInt(entry.get(HEADER[0])), junction);
			}
			List<Road> finalRoads = new ArrayList<>();
			for (Entry<String, Road> entry : roads.entrySet()) {
				Road road = entry.getValue();
				finalRoads.add(road);
			}

			net.setRoads(finalRoads);
			parser.close();
			reader.close();
			return net;
		} catch (IOException e) {
			throw e;
		}
	}

	private static List<Node> transform() {
		List<Node> nodes = new ArrayList<>();
		for (Entry<String, Street> entry : roads.entrySet()) {
			Street road = entry.getValue();
			Point point = road.getFirst();
			Point first = point;
			List<Point> points = new ArrayList<>();
			List<Junction> junctions = new ArrayList<>();
			String id = road.segment;
			String name = first.getRoadNumber();
			String lclLocCode = road.id;
			while (point != null) {
				points.add(point);
				point = point.getSucc();
				if (points.contains(point)) {
					points.add(first);
					break;
				}
			}
			for (Point p : points) {
				Junction junction = new JunctionImpl(p.record, true);
				changeCoordinates(junction);
				if (needTypeChange(p)) {
					junction.setType(junction.getTypes()[0]);
				}
				junctions.add(junction);
				junction.setLineRef(id);

				Node node = new Node(p.record, junction);
				nodes.add(node);
				Node.nodes.put(node.getId(), node);
			}
			finalRoads.add(new RoadImpl(junctions, id, name, lclLocCode));
		}
		return nodes;
	}

	private static boolean needTypeChange(Point p) {
		if (!p.getType().equals(TYPES[0])) {
			return false;
		}
		if (p.getIntersection().isEmpty()) {
			return false;
		}
		if (roads.get(p.getIntersection().get(0).getSegment()) == null) {
			return false;
		}
		return true;
	}

	private static void createRoads() {
		Set<Point> finish = new HashSet<>();
		for (Entry<Long, Point> entry : Point.everything.entrySet()) {
			Point point = entry.getValue();
			if (isStartPoint(finish, point) && isRightType(point)) {
				createRoad(point, finish);
			}
		}
	}

	private static boolean isRightType(Point point) {
		if (point.getLineRef().getType().equals(TYPES[1])) {
			return true;
		}
		if (point.isFirst() && point.getLineRef().getType().equals(TYPES[2])) {
			return true;
		}
		return false;
	}

	private static boolean isStartPoint(Set<Point> finish, Point point) {
		if (!point.getType().startsWith(TYPES[4])) {
			return false;
		}
		if (point.getLineRef() == null) {
			return false;
		}
		return !finish.contains(point);
	}

	private static Set<Point> createRoad(Point point, Set<Point> finish) {
		List<Point> points = new ArrayList<>();
		while (!points.contains(point) && point != null) {
			points.add(point);
			finish.add(point);
			point = point.getSucc();
		}

		Street s = new Street(points);
		roads.put(s.segment, s);
		for (Point p : points) {
			p.setSegment(s.segment);
		}
		return finish;
	}

	private static void linkPoints() {
		for (Entry<Long, Point> entry : Point.everything.entrySet()) {
			Point point = entry.getValue();
			point.getLineRef();
			point.getSucc();
			point.getPred();
			point.getIntersection();
			point.getHighestRef();
		}
	}

	private static void tranformCoordinates() throws FactoryException {
		CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:" + 4326); // z.B. 4326
		CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:" + 31467); // z.B. 31467
		MathTransform mt = CRS.findMathTransform(sourceCRS, targetCRS, true);
		ct = new TransformerImpl(mt);
	}

	private static void changeCoordinates(Junction point) {
		if ((int) point.getX() != 0 && (int) point.getY() != 0) {
			Coordinate[] coords = ct.transform(createCoordinate(point.getPoint()));
			point.setX(coords[0].getX());
			point.setY(coords[0].getY());
		}
		if ((int) point.getOriginalX() != 0 && (int) point.getOriginalY() != 0) {
			de.heuboe.map.datatypes.Point original = point.getOriginalPoint();
			Coordinate[] coords = ct.transform(createCoordinate(original));
			original.setX(coords[0].getX());
			original.setY(coords[0].getY());
		}
	}

	private static Coordinate createCoordinate(de.heuboe.map.datatypes.Point point) {
		return new CoordinateImpl(point.getX(), point.getY());
	}

}