import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class GraphTest {

    Graph g1 = new Graph(false);
    List<Vertex> vertices = new ArrayList<>();

    @Before
    public void setUp() {

        for (int i = 0; i < 5; i++) {
            vertices.add(new Vertex(i,new Ride(Integer.toString(i),0,0,0,0,"","","","","","",0,0,0,0)));
        }
    }

    @After
    public void tearDown() {
        g1 = null;
        vertices = null;
        assertNull(g1);
        assertNull(vertices);
    }

    @Test
    public void testAddEdge() {
        setUp();
        g1.addEdge(vertices.get(0),vertices.get(1),1); //             0
        g1.addEdge(vertices.get(0), vertices.get(2),1); //           / \
        g1.addEdge(vertices.get(1),vertices.get(2),4); //           1---2
        g1.addEdge(vertices.get(1), vertices.get(3),1); //         /     \
        g1.addEdge(vertices.get(2),vertices.get(4),1); //         3       4
        assertEquals(5,g1.getVertices().size());
        assertEquals(10,g1.getEdges().size());
        assertNotNull(g1.hasEdge(vertices.get(1), vertices.get(2)));
        assertNull(g1.hasEdge(vertices.get(4), vertices.get(1)));
    }

    @Test
    public void testIncomingOutgoing() {
        testAddEdge();
        List<Edge> in = g1.outgoingEdges(vertices.get(1));
        List<Edge> out = g1.incomingEdges(vertices.get(4));
        assertEquals(3, in.size());
        assertEquals(1,out.size());
        assertEquals("2 4 1", out.get(0).toString());
    }

    @Test
    public void testGetVertex() {
        testAddEdge();
        assertEquals(g1.getVertex(0),vertices.get(0));
        assertEquals(g1.getVertex(1),vertices.get(1));
        assertEquals(g1.getVertex(2),vertices.get(2));
        assertEquals(g1.getVertex(3),vertices.get(3));
        assertEquals(g1.getVertex(4),vertices.get(4));
    }

    @Test
    public void testSubGraph() {
        testAddEdge();                                              //       x
        List<Vertex> remove = new ArrayList<>();                    //      x x
        remove.add(vertices.get(0));                                //     1---2
        remove.add(vertices.get(4));                                //    /     x
        Graph g2 = g1.subgraphWithoutVertices(remove);              //   3       x
        assertEquals(3,g2.getVertices().size());
        assertEquals(4,g2.getEdges().size());
        assertNull(g2.hasEdge(vertices.get(0),vertices.get(1)));
        assertNull(g2.hasEdge(vertices.get(0),vertices.get(2)));
        assertNull(g2.hasEdge(vertices.get(2),vertices.get(4)));
        assertNotNull(g2.hasEdge(vertices.get(1),vertices.get(3)));
        assertNotNull(g2.hasEdge(vertices.get(1),vertices.get(2)));
    }

    // while i have a Graph, test PrimJarnik
    @Test
    public void testPrim() {
        testAddEdge();
        List<Edge> mst = PrimJarnik.primJarnik(g1);
        // edge of weight 4 will not be included (vertex 1 to vertex 2)
        assertFalse(mst.contains(g1.hasEdge(vertices.get(1), vertices.get(2))));
        assertTrue(mst.contains(g1.hasEdge(vertices.get(1), vertices.get(3))));
        assertTrue(mst.contains(g1.hasEdge(vertices.get(0), vertices.get(1))));
        assertTrue(mst.contains(g1.hasEdge(vertices.get(0), vertices.get(2))));
        assertTrue(mst.contains(g1.hasEdge(vertices.get(2), vertices.get(4))));
    }

}