import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

/**
 * Test class for Party
 * @author Jordan Thomson - fjb19170
 */
public class PartyTest {

    Party p1;

    @Before
    public void setUp() throws Exception {
        p1 = new Party("name");
    }

    @After
    public void tearDown() throws Exception {
        p1 = null;
        assertNull(p1);
    }

    @Test
    public void setSize() {
        assertEquals(0,p1.getSize());
        p1.setSize(2);
        assertEquals(2,p1.getSize());
        p1.setSize(4);
        assertEquals(4,p1.getSize());
        p1.setSize(0);
        assertEquals(0,p1.getSize());
        p1.setSize(-1);
        assertEquals(0,p1.getSize());
    }

    @Test
    public void getName() {
        assertEquals("name", p1.getName());
        assertNotEquals("Smith",p1.getName());
    }

    @Test
    public void getSize() {
        assertEquals(0, p1.getSize());
        p1.setSize(2);
        assertEquals(2,p1.getSize());
        assertNotEquals(0,p1.getSize());
        assertNotEquals(6,p1.getSize());
        p1.setSize(5);
        assertEquals(5,p1.getSize());

    }

    @Test
    public void getPreference() {
        assertNull(p1.getPreference("Wheelchair",0));
        p1.setSize(2);
        p1.setPreference("Horror","Y",0);
        assertNull(p1.getPreference("Horror",1));
        assertNull(p1.getPreference("Water",0));
        p1.setPreference("Water","N",0);
        assertEquals("N",p1.getPreference("Water",0));
    }

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void setPreference() {
        exceptionRule.expect(IllegalArgumentException.class);
        exceptionRule.expectMessage("No person defined");
        p1.setPreference("Wheelchair","Y",1);
        p1.setSize(2);
        p1.setPreference("Wheelchair","Y",0);
        assertEquals("Y",p1.getPreference("Wheelchair",0));
        p1.setPreference("Horror","Y",1);
        assertEquals("Y",p1.getPreference("Horror",1));
    }

    @Test
    public void setEmail() {
        assertEquals("jordan.thomson.2019@uni.strath.ac.uk",p1.setEmail("jordan.thomson.2019@uni.strath.ac.uk"));
        assertEquals("",p1.setEmail("jordan.thomson.2019.uni.strath.ac.uk"));
        assertEquals("",p1.setEmail("jordan.thomson.2019@unistrathacuk"));
        assertEquals("jordan.thomson@alliedvehicles.co.uk",p1.setEmail("jordan.thomson@alliedvehicles.co.uk"));
        assertEquals("",p1.setEmail("jordan.thomson@allied.c"));
    }

    @Test
    public void getEmail() {
        p1.setEmail("jordan.thomson@somewhere");
        assertEquals("",p1.getEmail());
        p1.setEmail("jordan.thomson.2019@uni.strath.ac.uk");
        assertEquals("jordan.thomson.2019@uni.strath.ac.uk",p1.getEmail());
    }

    @Test
    public void testRidePrefs() {
        Ride r1 = new Ride("r1",0,0,0,0,"N","N","N","N","N","",0,0,0,0);
        Ride r2 = new Ride("r1",0,0,0,0,"N","N","N","N","N","",0,0,0,0);
        Ride r3 = new Ride("r1",0,0,0,0,"N","N","N","N","N","",0,0,0,0);
        p1.setLastRide(r1);
        p1.setFirstRide(r2);
        p1.addRepeatRide(r3);
        p1.addRepeatRide(r2);
        assertEquals(r2,p1.getFirstRide());
        assertEquals(r1,p1.getLastRide());
        assertTrue(p1.getRepeatRides().contains(r2));
        assertTrue(p1.getRepeatRides().contains(r3));
        assertFalse(p1.getRepeatRides().contains(r1));
    }
}