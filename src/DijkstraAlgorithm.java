import java.util.*;

/**
 * Class to implement the Dijkstra algorithm,
 * getting min distance from a specified vertex in a graph
 * @author Jordan - fjb19170
 */
public class DijkstraAlgorithm {

    private final List<Vertex> nodes;
    private final List<Edge> edges;
    private Set<Vertex> settledNodes;
    private Set<Vertex> unSettledNodes;
    private Map<Vertex, Vertex> predecessors;
    private Map<Vertex, Integer> distance;

    public DijkstraAlgorithm(Graph graph) {
        // create a copy of the array so that we can operate on this array
        this.nodes = new ArrayList<>(graph.getVertices());
        this.edges = new ArrayList<>(graph.getEdges());
    }

    /**
     * uses the Dijkstra algorithm to collate the list of distances from the source to all
     * other reachable nodes.
     * @param source the vertex to generate distances from
     */
    public void execute(Vertex source) {
        settledNodes = new HashSet<>();
        unSettledNodes = new HashSet<>();
        distance = new HashMap<>();
        predecessors = new HashMap<>();
        distance.put(source, 0);
        unSettledNodes.add(source);
        while (unSettledNodes.size() > 0) {
            Vertex node = getMinimum(unSettledNodes);
            settledNodes.add(node);
            unSettledNodes.remove(node);
            findMinimalDistances(node);
        }
    }

    /**
     * gets the distance to all adjacent vertices of a node, and adds them to the unsettled vertices to be
     * processed later.
     * @param node Vertex to get the distance to all adjacent vertices.
     */
    private void findMinimalDistances(Vertex node) {
        List<Vertex> adjacentNodes = getNeighbors(node);
        for (Vertex target : adjacentNodes) {
            if (getShortestDistance(target) > getShortestDistance(node)
                    + getDistance(node, target)) {
                distance.put(target, getShortestDistance(node)
                        + getDistance(node, target));
                predecessors.put(target, node);
                unSettledNodes.add(target);
            }
        }

    }

    /**
     * Returns the distance between two vertices if they share an edge
     * @param node the start Vertex
     * @param target the end Vertex
     * @return the weight of the edge
     */
    private int getDistance(Vertex node, Vertex target) {
        for (Edge edge : edges) {
            if (edge.source().equals(node)
                    && edge.target().equals(target)) {
                return edge.weight();
            }
        }
        throw new RuntimeException("Should not happen");
    }

    /**
     * returns all the Vertices next to the supplied vertex
     * @param node Vertex to find neighbors of
     * @return List of vertices next to the supplied vertex
     */
    private List<Vertex> getNeighbors(Vertex node) {
        List<Vertex> neighbors = new ArrayList<>();
        for (Edge edge : edges) {
            if (edge.source().equals(node)
                    && !isSettled(edge.target())) {
                neighbors.add(edge.target());
            }
        }
        return neighbors;
    }

    private Vertex getMinimum(Set<Vertex> vertexes) {
        Vertex minimum = null;
        for (Vertex vertex : vertexes) {
            if (minimum == null) {
                minimum = vertex;
            } else {
                if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
                    minimum = vertex;
                }
            }
        }
        return minimum;
    }

    /**
     * Helper method to identify vertices that have had their shortest distance found
     * @param vertex Vertex to check status of
     * @return true if the node has a shortest distance false otherwise
     */
    private boolean isSettled(Vertex vertex) {
        return settledNodes.contains(vertex);
    }

    /**
     * Helper method to return the current shortest distance to the destination
     * @param destination the target Vertex
     * @return the current shortest distance, or max value if not yet found.
     */
    private int getShortestDistance(Vertex destination) {
        Integer d = distance.get(destination);
        if (d == null) {
            return Integer.MAX_VALUE;
        } else {
            return d;
        }
    }

    /**
     * returns a map of vertices and integers where the integer mapped to the vertex represents the
     * shortest distance from the executed vertex to all other vertices.
     * @return Map of vertices and integers, integer representing distance.
     */
    public Map<Vertex, Integer> getDistances() {
        return distance;
    }

    /**
     * This method returns the path from the source to the selected target and
     * NULL if no path exists
     */
    public LinkedList<Vertex> getPath(Vertex target) {
        LinkedList<Vertex> path = new LinkedList<>();
        Vertex step = target;
        // check if a path exists
        if (predecessors.get(step) == null) {
            return null;
        }
        path.add(step);
        while (predecessors.get(step) != null) {
            step = predecessors.get(step);
            path.add(step);
        }
        // Put it into the correct order
        Collections.reverse(path);
        path.remove(0);
        return path;
    }

}
