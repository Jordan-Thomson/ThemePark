import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class DijkstraAlgorithmTest {

    private DijkstraAlgorithm da;
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
        da = new DijkstraAlgorithm(g);
    }

    @After
    public void tearDown() throws Exception {
        vertices = null;
        g = null;
        da = null;
        assertNull(vertices);
        assertNull(g);
        assertNull(da);
    }

    @Test
    public void getDistances() {
        da.execute(vertices.get(1));
        Map<Vertex,Integer> dist = da.getDistances();
        assertEquals(dist.get(vertices.get(0)),(Integer)1);
        assertEquals(dist.get(vertices.get(1)),(Integer)0);
        assertEquals(dist.get(vertices.get(2)),(Integer)2);
        assertEquals(dist.get(vertices.get(3)),(Integer)1);
        assertEquals(dist.get(vertices.get(4)),(Integer)3);
    }

    @Test
    public void getPath() {
        da.execute(vertices.get(3));
        List<Vertex> path = da.getPath(vertices.get(4));
        assertEquals(path.size(),4);
        assertEquals(path.get(0).getRide().getName(),"1");
        assertEquals(path.get(1).getRide().getName(),"0");
        assertEquals(path.get(2).getRide().getName(),"2");
        assertEquals(path.get(3).getRide().getName(),"4");
    }
}