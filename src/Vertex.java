/**
 * Class to represent a node in a graph
 * @author Jordan - fjb19170
 */

public class Vertex {
    private int n;
    private Ride ride;
    private boolean visited;

    /**
     * Constructor
     * @param n a unique(preferably ID number)
     * @param ride Ride to be used as the Vertices element
     */
    public Vertex(int n, Ride ride) {
        this.n = n;
        this.ride = ride;
        this.visited = false;
    }

    //
    //Getters and Setters:
    //

    public String toString() {
        return ride.getName();
    }

    public Ride getRide() { return ride; }


}
