/**
 * Class to represent an edge between vertices in a graph
 * @author Jordan - fjb19170
 */

public class Edge {
    private Vertex source;
    private Vertex target;
    private int weight;

    /**
     * Standard constructor
     * @param source start Vertex
     * @param target end Vertex
     */
    public Edge(Vertex source, Vertex target) {
        this.source = source;
        this.target = target;
    }

    /**
     * Constructor including weight
     * @param source start Vertex
     * @param target end Vertex
     * @param weight Weight of the Edge
     */
    public Edge(Vertex source, Vertex target, int weight) {
        this(source, target);
        this.weight = weight;
    }

    //Getters:
    //
    public String toString() {
        return source + " " + target + " " + weight;
    }

    public Vertex source() {
        return this.source;
    }

    public Vertex target() {
        return this.target;
    }

    public int weight() {
        return this.weight;
    }
}
