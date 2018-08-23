package de.heuboe.ausbildung.DijkstraAlgorithm;

import java.util.*;

/**
 * A graph described by nodes and edges
 * 
 * @author verab
 */
public class Graph {

    private List<Node> nodes;
    private List<Edge> edges;

    /**
     * Creates a graph
     * 
     * @param nodes
     * @param edges
     */
    public Graph(List<Node> nodes, List<Edge> edges) {
        this.setNodes(nodes);
        this.setEdges(edges);
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }
}
