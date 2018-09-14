package de.heuboe.ausbildung.DijkstraAlgorithm;

public class Gerade {

    private final Node ortsvector;
    private final Node richtungsvector;

    public Gerade(Line line) {
        this(line.getStartPoint(), line.getEndPoint());
    }

    public Gerade(Node p1, Node p2) {
        // Ortsvektor
        this.ortsvector = new Node(p1.getX(), p1.getY());
        // Richtungsvektor
        this.richtungsvector = new Node(p2.getX() - p1.getX(), p2.getY() - p1.getY());
    }

    public Node getOrtsvector() {
        return ortsvector;
    }

    public Node getRichtungsvector() {
        return richtungsvector;
    }
}
