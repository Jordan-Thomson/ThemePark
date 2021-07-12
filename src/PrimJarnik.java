import java.util.*;

/**
 * Class to use Prim Jarnik algorithm to return minimum spanning tree of a graph
 * @author Jordan - fjb19170
 */
public class PrimJarnik {

    public static List<Edge> primJarnik(Graph g) {
        int cost =0;
        Vertex s = g.getVertex(0);
        Set<Vertex> unvisited = new HashSet<>(g.getVertices());
        unvisited.remove(s);

        List<Edge> path = new ArrayList<>();
        Queue<Edge> available = new PriorityQueue<>((o1, o2) -> Integer.compare(o1.weight(),o2.weight()));

        // while there are still unvisited vertices.
        while(!unvisited.isEmpty()) {
            for (Edge e : g.outgoingEdges(s)) {
                // if the target of the edge is unvisited mark as available
                if (unvisited.contains(e.target())) {
                    available.add(e);
                }
            }

            Edge e = available.remove();

            cost += e.weight();
            if (unvisited.contains(e.target())) {
                path.add(e);
            }

            s = e.target();
            unvisited.remove(s);
        }

        return path;
    }
}
