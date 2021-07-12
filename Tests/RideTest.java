import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * Test class for a Ride
 * @author Jordan Thomson - fjb19170
 */
public class RideTest {

    private Ride r1;

    @Before
    public void setUp() throws Exception {
        r1 = new Ride("Test Ride",0,999,0,999,"Y","Y","Y","Y","Y","TestTheme",10,20,30,40);
    }

    @After
    public void tearDown() throws Exception {
        r1 = null;
        assertNull(r1);
    }

    @Test
    public void testGetters() {
        assertEquals("Y",r1.getHorror());
        assertEquals("Y",r1.getAdrenaline());
        assertEquals("Y",r1.getKids());
        assertEquals("Y",r1.getWater());
        assertEquals("Y",r1.getWheelchair());
        assertEquals(0,r1.getMinHeight(),0);
        assertEquals(999,r1.getMaxHeight(),0);
        assertEquals(0,r1.getMinGroup());
        assertEquals(999,r1.getMaxGroup());
        assertEquals("TestTheme",r1.getTheme());
        assertEquals(10,r1.getxPos());
        assertEquals(20,r1.getyPos());
        assertEquals(30,r1.getWait());
    }

    @Test
    public void testDataTypes() {
        assertEquals("Y",r1.getSpecificType(0));
        assertEquals("Y",r1.getSpecificType(1));
        assertEquals("Y",r1.getSpecificType(2));
        assertEquals("Y",r1.getSpecificType(3));
        assertEquals("Y",r1.getSpecificType(4));
    }
}