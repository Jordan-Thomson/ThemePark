import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Class to represent a Graph of rides in a theme park
 * @author Jordan - fjb19170
 */
public class Graph {
    private boolean directed;
    private List<Edge> edges;
    private List<Vertex> vertices;

    /**
     * Constructor
     * @param directed is the Graph directed?
     */
    public Graph(boolean directed) {
        this.directed = directed;
        this.edges = new ArrayList<>();
        this.vertices = new ArrayList<>();
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    /**
     * Pointlessly removes all edges and vertices from a graph returning a new disconnected graph
     * @param vs List of vertices to remove
     * @return new Graph with only the required vertices/edges
     */
    public Graph subgraphWithoutVertices(List<Vertex> vs) {
        Graph subgraph = this.makeCopy();

        subgraph.vertices.removeIf(v -> vs.contains(v));
        subgraph.edges.removeIf(e -> vs.contains(e.source()) || vs.contains(e.target()));

        return subgraph;
    }

    /**
     * returns an edge with the supplied vertices if one exists, returns null if not.
     * @param source the Edges source vertex
     * @param target the Edges target vertex
     * @return the Edge if it exists, null otherwise.
     */
    public Edge hasEdge(Vertex source, Vertex target) {
        for (Edge e : edges) {
            if (e.source() == source && e.target() == target) {
                return e;
            }
        }
        return null;
    }

    /**
     * returns incoming edges of a vertex
     * @param v the vertex which edges are to be found
     * @return List of incoming edges belonging to vertex v
     */
    public List<Edge> incomingEdges(Vertex v) {
        List<Edge> es = new LinkedList<>();
        for (Edge e : edges) {
            if (e.target() == v) {
                es.add(e);
            }
        }
        return es;
    }

    /**
     * returns outgoing edges of a vertex
     * @param v the vertex which edges are to be found
     * @return List of outgoing edges belonging to vertex v
     */
    public List<Edge> outgoingEdges(Vertex v) {
        List<Edge> es = new LinkedList<>();
        for (Edge e : edges) {
            if (e.source() == v) {
                es.add(e);
            }
        }
        return es;
    }

    /**
     * Adds an edge to the graph
     * @param source the start vertex of the edge
     * @param target the end vertex of the edge
     * @param weight the weight of the edge
     */
    public void addEdge(Vertex source, Vertex target, int weight) {
        addVertex(source);
        addVertex(target);

        edges.add(new Edge(source, target, weight));
        if (!directed) {
            edges.add(new Edge(target, source, weight));
        }
    }

    private void addVertex(Vertex v) {
        if (!vertices.contains(v)) {
            vertices.add(v);
        }
    }

    /**
     * Creates and returns a copy of this graph
     * @return a new graph like this graph
     */
    private Graph makeCopy() {
        Graph h = new Graph(this.directed);
        for (Edge e : this.edges) {
            // only add if not there already
            if (!h.vertices.contains(e.source())) {
                h.vertices.add(e.source());
            }
            if (!h.vertices.contains(e.target())) {
                h.vertices.add(e.target());
            }
            // add the edge as a new edge;
            h.edges.add(new Edge(e.source(), e.target(),e.weight()));
        }
        return h;
    }

    /**
     * Method to get a specifc vertex
     * @param v the index of the vertex
     * @return the Vertex at the supplied index
     */
    public Vertex getVertex(int v) {
        if (v > vertices.size() || v < 0 || vertices.isEmpty()) {
            return null;
        }
        return vertices.get(v);
    }

    /**
     * Method to get a vertex by its contents
     * @param ride The ride that the vertex is to represent
     * @return the vertex if found, null otherwise
     */
    public Vertex getVertex(Ride ride) {
        for(Vertex v : vertices) {
            if (v.getRide().equals(ride)) {
                return v;
            }
        }
        return null;
    }
}
