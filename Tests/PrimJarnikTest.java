import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PrimJarnikTest {

    private Graph g;
    private List<Vertex> vertices;

    @Before
    public void setUp() throws Exception {
        vertices = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            vertices.add(new Vertex(i,new Ride(Integer.toString(i),0,0,0,0,"","","","","","",0,0,0,0)));
        }
        g = new Graph(false);
        g.addEdge(vertices.get(0),vertices.get(1),1); //             0
        g.addEdge(vertices.get(0), vertices.get(2),1); //           / \
        g.addEdge(vertices.get(1),vertices.get(2),4); //           1---2
        g.addEdge(vertices.get(1), vertices.get(3),1); //         /     \
        g.addEdge(vertices.get(2),vertices.get(4),1); //         3       4
    }

    @After
    public void tearDown() throws Exception {
        g = null;
        vertices = null;
        assertNull(g);
        assertNull(vertices);
    }

    @Test
    public void primJarnik() {
        List<Edge> mst = PrimJarnik.primJarnik(g);
        assertEquals(mst.size(),4);
        // this prim should start with node 0, then 1, then 2
        assertEquals(mst.get(0).toString(),"0 1 1"); // source target weight
        assertEquals(mst.get(1).toString(),"0 2 1");
        assertEquals(mst.get(2).toString(),"1 3 1");
        assertEquals(mst.get(3).toString(),"2 4 1");
    }
}